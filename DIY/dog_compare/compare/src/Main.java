public class Main
{
    public static void main(String[] args)
    {
        Dog[] Dogs = new Dog[]{
                new Dog(5, "yima"),
                new Dog(9, "enlish"),
                new Dog(5, "code")
        };

        Dog.toString(Max.max(Dogs));
    }
}
