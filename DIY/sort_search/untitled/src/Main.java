void main() {

    MergeAndSearch mas = new MergeAndSearch();
    int[] arr = {9, 5, 3, 1, 4, 2, 8, 7, 10, 6};

    mas.merge_sort(arr);
    for (int i = 0; i < arr.length; i++)    System.out.println(arr[i]);
    for (int i = 0; i < arr.length; i++)    System.out.print(arr[i]);


}