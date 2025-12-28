import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DisjointSetTest
{
    DisjointSet ds = new DisjointSet(10);

    @Test
    public void amptySetTest()
    {
        for(int i = 0, j = i + 1; j < 10; i++, j++)
        {
            assertFalse(ds.isConnected(i,j));
        }
    }

    @Test
    public void ConnectTest()
    {
        ds.connect(2,4);
        ds.connect(2,5);
        ds.connect(1,3);
        ds.connect(2,1);

        ds.connect(6,7);
        ds.connect(6,8);
        ds.connect(9,0);
        ds.connect(6,0);

        assertTrue(ds.isConnected(1,2));
        assertTrue(ds.isConnected(1,3));
        assertTrue(ds.isConnected(1,4));
        assertTrue(ds.isConnected(1,5));
        assertTrue(ds.isConnected(2,2));
        assertTrue(ds.isConnected(2,3));
        assertTrue(ds.isConnected(4,2));
        assertTrue(ds.isConnected(5,2));
        assertTrue(ds.isConnected(3,4));
        assertTrue(ds.isConnected(3,5));
        assertTrue(ds.isConnected(4,5));

        assertFalse(ds.isConnected(1, 6));
        assertFalse(ds.isConnected(1, 7));
        assertFalse(ds.isConnected(1, 8));
        assertFalse(ds.isConnected(1, 9));
        assertFalse(ds.isConnected(1, 0));
        assertFalse(ds.isConnected(2, 6));
        assertFalse(ds.isConnected(2, 7));
        assertFalse(ds.isConnected(2, 8));
        assertFalse(ds.isConnected(2, 9));
        assertFalse(ds.isConnected(2, 0));
    }
}