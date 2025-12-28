import java.util.Arrays;

public class DisjointSet
{
    private int[] parent;

    public DisjointSet(int N)
    {
        parent = new int[N];
        Arrays.fill(parent, -1);
    }

    public void connect(int x, int y)
    {
        if (isConnected(x, y))
        {
            System.out.println("x, y are in the same Set");
        }

        int a = find(x);
        int b = find(y);

        if (parent[a] < parent[b])
        {
            parent[a] += parent[b];
            parent[b] = a;
        }
        else
        {
            parent[b] += parent[a];
            parent[a] = b;
        }
    }

    public boolean isConnected(int x, int y)
    {
        return find(x) == find(y);
    }

    private int find(int x)
    {
        // 找到了
        if (parent[x] < 0)  return x;

        // 没找到，继续找顺便压缩
        // 压缩：子 = 父的根
        parent[x] = find(parent[x]);

        return parent[x];
    }
}
