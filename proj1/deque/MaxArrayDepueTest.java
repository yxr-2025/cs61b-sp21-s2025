package deque;

import org.junit.Test;
import java.util.Comparator;
import static org.junit.Assert.*;

public class MaxArrayDepueTest
{

    @Test
    public void nullmax()
    {
        Comparator<Integer> ic = new MaxArrayDeque.IntComparator();
        MaxArrayDeque<Integer> id = new MaxArrayDeque<Integer>(ic);

        Comparator<String> is = new MaxArrayDeque.StringComparator();
        MaxArrayDeque<String> sd = new MaxArrayDeque<String>(is);

        assertEquals(id.max(), null);
        assertEquals(sd.max(), null);
    }

    @Test
    public void intmax()
    {
        Comparator<Integer> ic = new MaxArrayDeque.IntComparator();
        MaxArrayDeque<Integer> id = new MaxArrayDeque<Integer>(ic);

        id.addLast(3);
        id.addLast(4);
        id.addLast(5);
        id.addLast(6);
        id.addLast(7);
        id.addLast(8);
        assertEquals(8, (int)id.max(ic));
    }

    @Test
    public void stringmax()
    {
        Comparator<String> is = new MaxArrayDeque.StringComparator();
        MaxArrayDeque<String> sd = new MaxArrayDeque<String>(is);

        sd.addLast("z");
        sd.addLast("am");
        sd.addLast("a");
        sd.addLast("pig");
        sd.addLast("Ohhh");
        sd.printDeque();
        assertEquals("a", (String) sd.max(is));
    }
}
