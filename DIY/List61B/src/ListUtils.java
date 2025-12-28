public class ListUtils
{
    /* 输入字符串数组，输出最长的哪个字符串 */
    public static String longest(List61B<String> list)
    {
        int max = 0;
        int list_size = list.size();
        for (int i = 0; i < list_size; i++)
        {
            if (list.get(i).length() > list.get(max).length())
            {
                max = i;
            }
        }
        return list.get(max);
    }
}
