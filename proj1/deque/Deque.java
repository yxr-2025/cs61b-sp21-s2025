package deque;

public interface Deque<T>
{
    void addLast(T x);
    // // 可以假设 item 永远不会是 null

    void addFirst(T x);
    // // 可以假设 item 从不为 null

    T removeFirst();
    // 移除并返回双端队列前端的元素。如果不存在这样的元素，返回 null

    T removeLast();
    // 移除并返回双端队列后端的元素。如果不存在这样的元素，返回 null

    T get(int i);
    // 0 是前端元素，1 是下一个元素
    // 如果不存在这样的元素，返回 null

    int size();
    // 返回双端队列中项目的数量，缓存实现

    boolean isEmpty();
    // 如果双端队列为空，则返回 true ，否则返回 false 。

    void printDeque();
    //头到尾打印双端队列中的项目，项目之间用空格分隔。所有项目打印完后，换行

    public boolean equals(Object o);
    // 看文档实现

    //Deque 接口不应实现 Iterable ，
    // 而应仅由两个实现类 LinkedListDeque 和 ArrayDeque 实现

}