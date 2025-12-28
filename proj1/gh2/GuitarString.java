package gh2;

import deque.ArrayDeque;
import deque.Deque;
// TODO: 可能需要更多的导入语句（比如你具体的 ArrayDeque 或 LinkedListDeque）

public class GuitarString
{
    private static final int SR = 44100;      // 采样率 (Sampling Rate)
    private static final double DECAY = .996; // 能量衰减因子 (energy decay factor)

    /* 用于存储声音数据的缓冲区。 */
    private Deque<Double> buffer;

    /* 创建一根给定频率的吉他弦。 */
    // GuitarString 构造函数中将 Deque 缓冲区填充为零
    public GuitarString(double frequency)
    {
        int capacity = (int)Math.round(SR / frequency);
        buffer = new ArrayDeque<>();
        for (int i = 0; i < capacity; i++)
        {
            buffer.addLast(0.0);
        }
    }


    /* 通过将缓冲区替换为白噪声来拨动吉他弦。 */
    public void pluck()
    {
        int capacity = buffer.size();
        for (int i = 0; i < capacity; i++)
        {
            double r = Math.random() - 0.5;
            buffer.removeLast();
            buffer.addFirst(r);
        }
    }

    /* 通过执行一次 Karplus-Strong 算法迭代，使模拟推进一步。 */
    public void tic()
    {
        double newdouble = (buffer.removeFirst() + buffer.get(0)) / 2 * DECAY;
        buffer.addLast(newdouble);
    }

    /* 返回缓冲区前端（front）的 double 值。 */
    public double sample()
    {
        return buffer.get(0);
    }
}