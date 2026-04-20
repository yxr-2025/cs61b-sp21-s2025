package hashmap;

import java.util.Set;
/**
 * 你的 hashmap.MyHashMap 实现类应当实现此接口。为此，
 * 请在 "public class..." 声明的末尾追加 "implements hashmap.Map61B<K, V>"，
 * 不过如果你愿意，也可以使用其他形式类型参数。
 */
public interface Map61B<K, V> extends Iterable<K> {
    /** 从此映射中移除所有映射关系。 */
    void clear();

    /** 如果此映射包含指定键的映射关系，则返回 true。 */
    boolean containsKey(K key);

    /**
     * 返回指定键所映射的值；如果此映射不包含该键的映射关系，则返回 null。
     */
    V get(K key);

    /** 返回此映射中的键-值映射关系数。 */
    int size();

    /**
     * 在此映射中将指定值与指定键关联。
     * 如果映射中以前包含该键的映射关系，则旧值被替换。
     */
    void put(K key, V value);

    /** 返回此映射中包含的键的 Set 视图。 */
    Set<K> keySet();

    /**
     * 如果存在，则从此映射中移除指定键的映射关系。
     * Lab 8 不要求实现此方法。如果你不实现它，请抛出
     * UnsupportedOperationException。
     */
    V remove(K key);

    /**
     * 仅当指定键当前映射到指定值时，才移除此键的条目。
     * Lab 8 不要求实现此方法。如果你不实现它，请抛出
     * UnsupportedOperationException。
     */
    V remove(K key, V value);
}