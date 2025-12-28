public class Main
{
    public static void main(String[] args)
    {
        // test
        System.out.println("=== Testing AList ===");
        List61B<String> al = new AList<>();
        al.addlast("I");
        al.addlast("am");
        al.addlast("you");
        al.addlast("Dad");
        System.out.println("AList contents: ");
        for (int i = 0; i < al.size(); i++)
        {
            System.out.println("  [" + i + "] = " + al.get(i));
        }
        System.out.println("Longest in AList: " + ListUtils.longest(al));
        System.out.println("Size: " + al.size());

        System.out.println();

        // test dllist
        System.out.println("=== Testing DLList ===");
        List61B<String> dl = new DLList<>();
        dl.addlast("I");
        dl.addlast("am");
        dl.addlast("you");
        dl.addlast("Dad");
        System.out.println("DLList contents: ");
        for (int i = 0; i < dl.size(); i++)
        {
            System.out.println("  [" + i + "] = " + dl.get(i));
        }
        System.out.println("Longest in DLList: " + ListUtils.longest(dl));
        System.out.println("Size: " + dl.size());
    }
}

