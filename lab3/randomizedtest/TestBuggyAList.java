package randomizedtest;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by hug.
 */
public class TestBuggyAList
{
    @Test
    public void testThreeAddThreeRemove()
    {
        AListNoResizing<Integer> a = new AListNoResizing<>();
        BuggyAList<Integer> b = new BuggyAList<>();

        a.addLast(4);
        a.addLast(5);
        a.addLast(6);

        b.addLast(4);
        b.addLast(5);
        b.addLast(6);
        assertEquals(a.size(), b.size());

        assertEquals(a.removeLast(), b.removeLast());
        assertEquals(a.removeLast(), b.removeLast());
        assertEquals(a.removeLast(), b.removeLast());
    }

    @Test
    public void randomizedTest()
    {
        AListNoResizing<Integer> L = new AListNoResizing<>();
        BuggyAList<Integer> B = new BuggyAList<>();

        int N = 5000;
        for (int i = 0; i < N; i += 1)
        {
            int operationNumber = StdRandom.uniform(0, 4);

            if (operationNumber == 0)
            {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                L.addLast(randVal);
                B.addLast(randVal);
                assertEquals(L.size(), B.size());
            }
            else if (operationNumber == 1)
            {
                // size
                int L_size = L.size();
                int B_size = B.size();
                assertEquals(B_size, L_size);
            }
            if (L.size() <= 0)
            {
                continue;
            }
            if (operationNumber == 2)
            {
                int L_last = L.getLast();
                int B_last = B.getLast();
                assertEquals(L_last, B_last);
            }
            else if (operationNumber == 3)
            {
                int L_remove = L.removeLast();
                int B_remove = B.removeLast();
                assertEquals(L_remove, B_remove);
            }
        }
    }




}
