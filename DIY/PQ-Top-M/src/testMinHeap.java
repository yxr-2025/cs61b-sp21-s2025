import org.junit.Test;
import static org.junit.Assert.*;

public class testMinHeap {

    // 辅助方法：验证堆序性质
    private boolean isValidMinHeap(MinHeap heap) {
        int[] items = heap.items;
        int size = heap.size();
        for (int i = 1; i <= size; i++) {
            int left = 2 * i;
            int right = 2 * i + 1;
            if (left <= size && items[i] > items[left]) return false;
            if (right <= size && items[i] > items[right]) return false;
        }
        return true;
    }

    @Test
    public void testBasicOperations() {
        MinHeap heap = new MinHeap();
        int[] inputs = {10, 3, 7, 1, 5};
        for (int x : inputs) {
            heap.add(x);
            assertTrue("堆序在插入 " + x + " 后被破坏", isValidMinHeap(heap));
        }

        int[] expected = {1, 3, 5, 7, 10};
        for (int e : expected) {
            assertEquals("removeMin 应返回 " + e, e, heap.removeMin());
            if (!heap.isEmpty()) {
                assertTrue("堆序在删除后被破坏", isValidMinHeap(heap));
            }
        }
        assertTrue("堆应为空", heap.isEmpty());
    }

    @Test
    public void testEdgeCases() {
        MinHeap heap = new MinHeap();
        assertTrue(heap.isEmpty());

        // 空堆操作（假设你的 removeMin 在空时抛异常）
        try {
            heap.removeMin();
            fail("空堆应抛出异常");
        } catch (Exception ignored) {}

        heap.add(42);
        assertEquals(42, heap.getMin());
        assertEquals(42, heap.removeMin());
        assertTrue(heap.isEmpty());

        // 重复值
        heap.add(5);
        heap.add(5);
        heap.add(3);
        assertEquals(3, heap.removeMin());
        assertEquals(5, heap.removeMin());
        assertEquals(5, heap.removeMin());
    }

    @Test
    public void testDynamicResizing() {
        MinHeap heap = new MinHeap(2); // 初始容量=2
        for (int i = 1; i <= 4; i++) {
            heap.add(i);
        }
        assertEquals(4, heap.size());
        // 只要不崩溃，说明扩容基本工作（JUnit 4 不易测内部数组长度）
    }


    @Test
    public void testSwap()
    {
        MinHeap mh = new MinHeap(2);
        mh.items[1] = 1;
        mh.items[2] = 2;
        // mh.swap(1,2);
        System.out.println("1:" + mh.items[1]);
        System.out.println("2:" + mh.items[2]);
    }

    @Test
    public void testadd()
    {
        MinHeap mh = new MinHeap();

        mh.add(5);
        mh.add(5);
        mh.add(3);
        System.out.println(mh.items);
    }
}