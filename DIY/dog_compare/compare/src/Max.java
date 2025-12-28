public class Max
{
    public static <T extends Comparable<T>> T max(T[] items)
    {
        T maxitem = items[0];
        for (T item : items)
        {
            if (item.compareTo(maxitem) > 0)
            {
                maxitem = item;
            }
        }
        return maxitem;
    }
}
