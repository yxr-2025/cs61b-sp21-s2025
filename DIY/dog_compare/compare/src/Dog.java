import java.util.Comparator;

public class Dog implements Comparable<Dog>
{
    private int size;
    private String name;

    public Dog(int s, String n)
    {
        this.name = n;
        this.size = s;
    }

    @Override
    public int compareTo(Dog other)
    {
        return this.size - other.size;
    }

    public static void toString(Dog a)
    {
        System.out.println(a.name);
    }

    public static class Namecompartor implements  Comparator<Dog>
    {
        @Override
        public int compare(Dog a, Dog b)
        {
            return a.name.compareTo(b.name);
        }
    }
}
