import org.junit.Test;

    public static void main(String[] args) {
    System.out.println("开始测试 ArraySet...");

    // 1. 测试静态工厂方法 of 和 toString
    ArraySet<String> s = ArraySet.of("hi", "hello", "world");
    System.out.println("集合内容 (期待 {hi, hello, world}): " + s);

    // 2. 测试 size
    System.out.println("集合大小 (期待 3): " + s.size());

    // 3. 测试 contains
    System.out.println("包含 'hello'? (期待 true): " + s.contain("hello"));
    System.out.println("包含 'java'? (期待 false): " + s.contain("java"));

    // 4. 测试 add 的去重功能
    s.add("hi");
    System.out.println("再次添加 'hi' 后的大小 (期待 3): " + s.size());

    // 5. 测试 Iterable / 增强型 for 循环 (依赖 Iterator)
    System.out.print("遍历集合: ");
    for (String item : s) {
        System.out.print(item + " ");
    }
    System.out.println();

    // 6. 测试 equals
    ArraySet<String> s2 = ArraySet.of("hi", "hello", "world");
    ArraySet<String> s3 = ArraySet.of("hello", "hi", "world"); // 顺序不同但内容相同
    ArraySet<String> s4 = ArraySet.of("hi", "java");

    System.out.println("s 等于 s2? (期待 true): " + s.equals(s2));
    System.out.println("s 等于 s3? (期待 true): " + s.equals(s3));
    System.out.println("s 等于 s4? (期待 false): " + s.equals(s4));

    // 7. 测试异常处理 (NoSuchElementException)
    System.out.println("测试迭代器越界异常...");
    java.util.Iterator<String> it = s.iterator();
    while (it.hasNext()) {
        it.next();
    }
    try {
        it.next(); // 此时应该抛出异常
    } catch (java.util.NoSuchElementException e) {
        System.out.println("成功捕获 NoSuchElementException!");
    }

    // 8. 测试空集合
    ArraySet<Integer> emptySet = new ArraySet<>();
    System.out.println("空集合打印 (期待 {}): " + emptySet);

    System.out.println("\n所有测试完成！");
}


