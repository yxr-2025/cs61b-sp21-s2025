package deque;

import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T>
{
    private Comparator<T> mycomparator;

    // 使用给定的 Comparator 创建一个 MaxArrayDeque
    public MaxArrayDeque(Comparator<T> c)
    {
        super();
        mycomparator = c;
    }

    // 返回由之前给定的 Comparator 决定的双端队列中的最大元素。
    // 如果 MaxArrayDeque 为空，则简单返回 null 。
    public T max() {
        if (this.isEmpty())
        {
            return null;
        }
        int maxIndex = 0;
        int size = this.size();
        for (int i = 0; i < size; i++)
        {
            if (mycomparator.compare(this.get(i), this.get(maxIndex)) > 0)
            {
                maxIndex = i;
            }
        }
        return this.get(maxIndex);
    }

    // 返回由参数 Comparator c 决定的双端队列中的最大元素。
    // 如果 MaxArrayDeque 为空，则简单返回 null
    public T max(Comparator<T> c)
    {
        mycomparator = c;
        return max();
    }

    public static class IntComparator implements Comparator<Integer>
    {
        @Override
        public int compare(Integer a, Integer b)
        {
            // 这里也可以转化成int后直接有(> < +)
            return a.compareTo(b);
        }
    }

    public static class StringComparator implements Comparator<String>
    {
        @Override
        public int compare(String a, String b)
        {
            return b.compareToIgnoreCase(a);
        }
    }
}
