package game2048;

import java.util.Formatter;
import java.util.Observable;

/** 表示一局 2048 游戏的状态。
 *  @author Yxr
 */
public class Model extends Observable
{
    /** 当前棋盘的内容。 */
    private Board board;
    /** 当前得分。 */
    private int score;
    /** 历史最高得分。游戏结束时会更新。 */
    private int maxScore;
    /** 如果游戏已结束则为 true。 */
    private boolean gameOver;

    /* 坐标系统说明：
     * 棋盘上的列 C、行 R（其中第 0 行、第 0 列是棋盘左下角）
     * 对应 board.tile(c, r)。
     * 注意！这类似于 (x, y) 坐标：列 = x，行 = y。
     */

    /** 最大方块的值（获胜条件）。 */
    public static final int MAX_PIECE = 2048;

    /** 创建一个空的 2048 游戏，棋盘大小为 SIZE，初始无方块，得分为 0。 */
    public Model(int size)
    {
        board = new Board(size);
        score = maxScore = 0;
        gameOver = false;
    }

    /** 用于测试的构造方法：
     *  使用 RAWVALUES 初始化棋盘（0 表示空位），
     *  RAWVALUES 按 [行][列] 索引，(0, 0) 对应棋盘左下角。
     */
    public Model(int[][] rawValues, int score, int maxScore, boolean gameOver)
    {
        int size = rawValues.length;
        board = new Board(rawValues, score);
        this.score = score;
        this.maxScore = maxScore;
        this.gameOver = gameOver;
    }

    /** 返回位置 (COL, ROW) 上当前的方块，其中 0 <= ROW < size()，0 <= COL < size()。
     *  如果该位置为空，则返回 null。
     *  此方法仅用于测试，未来可能会被弃用。
     */
    public Tile tile(int col, int row)
    {
        return board.tile(col, row);
    }

    /** 返回棋盘一边的格子数量（即棋盘大小）。
     *  此方法仅用于测试，未来可能会被弃用。
     */
    public int size() {
        return board.size();
    }

    /** 如果游戏结束（无合法操作，或棋盘上出现值为 2048 的方块），则返回 true。 */
    public boolean gameOver()
    {
        checkGameOver();
        if (gameOver)
        {
            maxScore = Math.max(score, maxScore);
        }
        return gameOver;
    }

    /** 返回当前得分。 */
    public int score() {
        return score;
    }

    /** 返回历史最高得分（在游戏结束时更新）。 */
    public int maxScore() {
        return maxScore;
    }

    /** 清空棋盘并重置得分为 0。 */
    public void clear() {
        score = 0;
        gameOver = false;
        board.clear();
        setChanged();
    }

    /** 将 TILE 添加到棋盘上。
     *  要求该位置当前没有其他方块。
     */
    public void addTile(Tile tile)
    {
        board.addTile(tile);
        checkGameOver();
        setChanged();
    }

    /** 将棋盘向 SIDE 方向倾斜（滑动）。
     *  如果棋盘状态发生变化，则返回 true。
     *
     *  合并规则如下：
     *  1. 如果在滑动方向上有两个相邻方块的值相同，则它们会合并为一个值为原值两倍的新方块，
     *     并将新值加到 score 中。
     *  2. 一次滑动中，一个由合并产生的方块不能再参与另一次合并。
     *     也就是说，每个方块在一次滑动中最多只参与一次合并（也可能不参与）。
     *  3. 如果在滑动方向上有三个连续相同值的方块，则**最靠前的两个**会合并，
     *     第三个（靠后的）不会参与此次合并。
     */
    public boolean tilt(Side side)
    {
        boolean changed;
        changed = false;

        board.setViewingPerspective(side);
        for (int i = 0; i < board.size(); i++)
        {
            if (processCol(i) == true)
            {
                changed = true;
            }
        }

        // 如果棋盘上的任何内容发生变化，我们必须将 changed 局部变量设置为 true
        // 通知 GUI 有内容需要绘制
        checkGameOver();
        if (changed)
        {
            setChanged();
        }
        board.setViewingPerspective(Side.NORTH);
        return changed;
    }

    /** 处理一列的移动和合并逻辑 */
    private boolean processCol(int col)
    {
        int boardsize = board.size();
        boolean changed = false;
        boolean[] merged= new boolean[4];
        for (int i = 0; i < boardsize; i++)
        {
            Tile t1 = board.tile(col, boardsize - i - 1);
            // 从上往下遍历到tile后,向上遍历到board边界或tile
            if (t1 != null)
            {
                if (boardsize - i >= boardsize) { continue; }
                for (int j = boardsize - i; j < boardsize; j++) /** j边界似乎有问题 */
                {
                    Tile t2 = board.tile(col, j);
                    //
                    if (j == 3 && t2 == null)
                    {
                        board.move(col, j, t1);
                        changed = true;
                        break;
                    }
                    else if (t2 != null)
                    {
                        // t2没有merge过,t1.value() == t2.value()
                        if (merged[j] == false && t1.value() == t2.value())
                        {
                            board.move(col, j, t1);
                            merged[j] = true;
                            score += t2.value() * 2;
                            changed = true;
                            break;
                        }
                        else
                        {
                            board.move(col, j -1, t1);
                            changed = true;
                            break;
                        }
                    }
                }
            }
        }
        return changed;
    }

    /** 检查游戏是否结束，并相应地设置 gameOver 变量。 */
    private void checkGameOver()
    {
        gameOver = checkGameOver(board);
    }

    /** 判断游戏是否结束。 */
    private static boolean checkGameOver(Board b)
    {
        return maxTileExists(b) || !atLeastOneMoveExists(b);
    }

    /** 如果棋盘上至少有一个空格（即值为 null），则返回 true。
     *  空格在棋盘中以 null 表示。
     */
    public static boolean emptySpaceExists(Board b)
    {
        int size = b.size();
        for (int i = 0; i < size; i++)
        {
            for (int j = 0; j < size; j++)
            {
                if (b.tile(i, j) == null)
                {
                    return  true;
                }
            }
        }
        return false;
    }

    /**
     * 如果棋盘上任意一个方块的值等于最大有效值（MAX_PIECE），则返回 true。
     * 注意：给定一个 Tile 对象 t，可通过 t.value() 获取其数值。
     * @helper : ai.qwen 3 pro
     */
    public static boolean maxTileExists(Board b)
    {
        int size = b.size();
        for (int i = 0; i < size; i++)
        {
            for (int j = 0; j < size; j++)
            {
                Tile t = b.tile(i, j);
                if (t != null && t.value() == MAX_PIECE)
                {
                    return  true;
                }
            }
        }
        return false;
    }

    /**
     * 如果棋盘上存在至少一个合法操作，则返回 true。
     * 合法操作有两种情况：
     * 1. 棋盘上至少有一个空格。
     * 2. 存在两个相邻（上下左右）且值相同的方块。
     */
    public static boolean atLeastOneMoveExists(Board b)
    {
        for (int i = 0; i < b.size(); i++)
        {
            for (int j = 0; j < b.size(); j++)
            {
                Tile t = b.tile(i, j);
                if (t == null)
                {
                    return true;
                }

                // east
                if (i + 1 < b.size())
                {
                    Tile n = b.tile(i + 1, j);
                    if (n != null && n.value() == t.value())
                    {
                        return true;
                    }
                }
                // weast
                if (i - 1 >= 0)
                {
                    Tile s = b.tile(i - 1, j);
                    if (s != null && s.value() == t.value())
                    {
                        return true;
                    }
                }
                // north
                if (j + 1 < b.size())
                {
                    Tile e = b.tile(i, j + 1);
                    if (e != null && e.value() == t.value())
                    {
                        return true;
                    }
                }
                // south
                if (j - 1 >= 0)
                {
                    Tile w = b.tile(i, j - 1);
                    if (w != null && w.value() == t.value())
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    /** 将模型以字符串形式返回，用于调试。 */
    public String toString() {
        Formatter out = new Formatter();
        out.format("%n[%n");
        for (int row = size() - 1; row >= 0; row -= 1)
        {
            for (int col = 0; col < size(); col += 1)
            {
                if (tile(col, row) == null)
                {
                    out.format("|    ");
                }
                else
                {
                    out.format("|%4d", tile(col, row).value());
                }
            }
            out.format("|%n");
        }
        String over = gameOver() ? "已结束" : "未结束";
        out.format("] %d (最高: %d) (游戏 %s) %n", score(), maxScore(), over);
        return out.toString();
    }

    @Override
    /** 判断两个模型是否相等。 */
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        } else if (getClass() != o.getClass()) {
            return false;
        } else {
            return toString().equals(o.toString());
        }
    }

    @Override
    /** 返回该模型字符串表示的哈希码。 */
    public int hashCode() {
        return toString().hashCode();
    }
}