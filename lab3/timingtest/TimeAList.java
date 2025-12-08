package timingtest;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by hug.
 */
public class TimeAList {

    private static void printTimingTable(AList<Integer> Ns, AList<Double> times, AList<Integer> opCounts) {
        System.out.printf("%12s %12s %12s %12s\n", "N", "time (s)", "# ops", "microsec/op");
        System.out.printf("------------------------------------------------------------\n");
        for (int i = 0; i < Ns.size(); i += 1) {
            int N = Ns.get(i);
            double time = times.get(i);
            int opCount = opCounts.get(i);
            double timePerOp = time / opCount * 1e6;
            System.out.printf("%12d %12.2f %12d %12.2f\n", N, time, opCount, timePerOp);
        }
    }

    public static void main(String[] args)
    {
        AList<Integer> Ns = new AList<>();
        Ns.addLast(1000);
        Ns.addLast(2000);
        Ns.addLast(4000);
        Ns.addLast(8000);
        Ns.addLast(16000);
        Ns.addLast(32000);
        Ns.addLast(64000);
        Ns.addLast(128000);
        Ns.addLast(256000);
        Ns.addLast(512000);
        Ns.addLast(1024000);
        timeAListConstruction(Ns);
    }

    public static void timeAListConstruction(AList<Integer> Ns)
    {
        AList<Double> times = new AList<>();

        // 遍历times和Ns链表
        for (int i = 0; i < Ns.size(); i++)
        {
            AList<Integer> al = new AList<>();
            Stopwatch sw = new Stopwatch();
            int size_n = Ns.get(i);

            for (int j = 0; j < size_n; j++)
            {
                al.addLast(j);
            }
            // 把时间存储在times链表里
            times.addLast(sw.elapsedTime());
        }


        printTimingTable(Ns, times, Ns);
    }
}
