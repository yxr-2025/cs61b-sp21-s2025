package flik;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class TestFlik
{
    @Test
    public void TestSameNumber()
    {
        int a, b;
        for (int i = 0; i < 10000; i += 1)
        {
            a = i;
            b = i;
            if (!(Flik.isSameNumber(a,b)))
            {
                System.out.println(a);
            }
            assertTrue(Flik.isSameNumber(a,b));
        }
    }
}
