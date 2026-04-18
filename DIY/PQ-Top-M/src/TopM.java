import java.util.PriorityQueue;

// 找出N个数中最大的 M 个数（用最小堆维护 M 个最大值）
void main()
{
    /*我有一间体育场，只让m个人进来，每当人数变成m+1时，剔除最小的哪个*/
    int M = 3;
    int[] stream = {10, 5, 20, 1, 2, 3, 9, 8, 7, 16};

    PriorityQueue<Integer> PQ = new PriorityQueue<>();

    for (int x : stream)
    {
        PQ.add(x);
        if (PQ.size() > M)
        {
            PQ.poll();
        }
    }
    System.out.println("TOP M: " + PQ);
}
