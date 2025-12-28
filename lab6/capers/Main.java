package capers;


import java.io.File;
import static capers.Utils.*;

/** Canine Capers: Gitlet 的前奏。
 * @author Yxr_
 */
public class Main {
    /**
     * 运行以下三个命令之一：
     * * story [text] -- 将 "text" 外加一个换行符追加到 .capers 目录下的 story 文件中。
     * 此外，打印出当前故事的完整内容。
     *
     * dog [name] [breed] [age] -- 持久化地创建一个具有指定参数的狗狗对象；
     * 还应打印该狗狗的 toString() 内容。假设狗狗名字是唯一的。
     *
     * birthday [name] -- 持久化地增加一只狗狗的年龄，并打印出一条庆祝信息。
     *
     * 所有持久化数据都应存储在当前工作目录 (CWD) 下的 ".capers" 目录中。
     *
     * *你不应该手动创建这些内容，你的程序应当自动创建这些文件夹/文件*
     *
     * .capers/ -- 存储所有持久化数据的顶层文件夹
     * - dogs/ -- 包含所有狗狗持久化数据的文件夹
     * - story -- 包含当前故事内容的文本文件
     *
     * @param args 来自命令行的参数
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            // 使用辅助工具打印错误并终止程序
            Utils.exitWithError("Must have at least one argument");
        }

        // 初始化持久化层：创建必要的文件夹
        CapersRepository.setupPersistence();

        String text;
        switch (args[0])
        {
            case "story":
                /* 这个调用已经为你处理好了。其余部分的逻辑会很相似。 */
                validateNumArgs("story", args, 2);
                text = args[1];
                CapersRepository.writeStory(text);
                break;
            case "dog":
                validateNumArgs("dog", args, 4);
                String name = args[1];
                String breed = args[2];
                int age = Integer.parseInt(args[3]);
                CapersRepository.makeDog(name, breed, age);
                break;
            case "birthday":
                validateNumArgs("birthday", args, 2);
                String name1 = args[1];
                CapersRepository.celebrateBirthday(name1);
                break;
            default:
                // 处理未知命令
                exitWithError(String.format("Unknown command: %s", args[0]));
        }
        return;
    }

    /**
     * 检查参数数量是否符合预期，如果不匹配则抛出运行时异常。
     *
     * @param cmd 正在校验的命令名称
     * @param args 来自命令行的参数数组
     * @param n 预期的参数数量
     */
    public static void validateNumArgs(String cmd, String[] args, int n) {
        if (args.length != n) {
            throw new RuntimeException(
                    String.format("Invalid number of arguments for: %s.", cmd));
        }
    }
}