public class MergeAndSearch
{


    public void merge_sort(int[] n)
    {
        if(n == null || n.length == 1)   return;
        mergeSortHelper(n, 0, n.length - 1);
    }

    private void mergeSortHelper(int[] n, int left, int right)
    {
        if (left >= right)  return;

        int mid = (left + right) / 2;

        mergeSortHelper(n, left, mid);
        mergeSortHelper(n, mid + 1, right);
        merge(n, left, mid, right);
    }

    private void merge(int[] n, int left, int mid, int right)
    {
        int[] temp = new int[right -left + 1];

        int i = left;
        // 左半边数组的指针
        int j = mid + 1;
        // 右半边数组的指针
        int k = 0;
        // 临时数组的下标

        while (i <= mid && j <= right)
        {
            if (n[i] < n[j])
            {
                temp[k] = n[i];
                i += 1;
            }
            else
            {
                temp[k] = n[j];
                j += 1;
            }
            k += 1;
        }

        // 单侧处理完另一侧直接全放过去
        while (i <= mid)
        {
            temp[k] = n[i];
            k += 1;
            i += 1;
        }
        while (j <= right)
        {
            temp[k] = n[j];
            k += 1;
            j += 1;
        }

        System.arraycopy(temp, 0, n, left, right - left + 1);
    }

    public int Binary_Search(int[] n, int target)
    {
        return Binary_Search_Helper(n, target, 0, n.length - 1);
    }

    // 二分搜索(递归）
    private int Binary_Search_Helper(int[] n, int target, int left, int right)
    {
        if (left > right)          return -1;
        int mid = left + (right - left) / 2;
        if (n[mid] == target)      return mid;
        else if (n[mid] < target)  return Binary_Search_Helper(n, target, mid + 1, right);
        else                       return Binary_Search_Helper(n, target, left, mid - 1);
    }
}
