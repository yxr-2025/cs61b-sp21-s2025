package capers;

import java.io.File;
import java.io.Serializable;

import static capers.Dog.DOG_FOLDER;
import static capers.Utils.*;

/** Capers 仓库类
 * @author Yxr_
 * Capers 仓库的结构如下：
 *
 * .capers/ -- 存储所有持久化数据的顶层文件夹
 * - dogs/ -- 存储所有狗狗持久化数据的文件夹
 * - story -- 存储当前故事内容的文本文件
 *
 */
public class CapersRepository {
    /** 当前工作目录 (CWD)。 */
    static final File CWD = new File(System.getProperty("user.dir"));

    /** 主要的元数据文件夹 (.capers)。 */
    static final File CAPERS_FOLDER = Utils.join(CWD, ".capers");
    /**
     * 执行必要的 filesystem (文件系统) 操作以实现持久化。
     * (创建任何必要的文件夹或文件)
     * 记住推荐的结构（你不一定要完全遵守）：
     *
     * .capers/ -- 顶层文件夹
     * - dogs/ -- 存储狗狗数据的文件夹
     * - story -- 存储故事的文件
     */
    public static void setupPersistence() {
        if (!(CAPERS_FOLDER.exists()))  {CAPERS_FOLDER.mkdir();}
        if (!(DOG_FOLDER.exists()))     {DOG_FOLDER.mkdir();}
    }

    /**
     * 将参数中的第一个非命令参数追加到 .capers 目录中名为 `story` 的文件中。
     * @param text 要追加到故事文件中的字符串文本
     */
    public static void writeStory(String text) {
        File STORY_FILE = Utils.join(CAPERS_FOLDER, "story");
        String oldtext = "";
        if (STORY_FILE.exists())
        {
            oldtext = Utils.readContentsAsString(STORY_FILE);
        }
        String newstory = oldtext + text + "\n";
        Utils.writeContents(STORY_FILE, newstory);
        System.out.println(newstory);
    }

    /**
     * 使用前三个非命令参数（名字、品种、年龄）创建并持久化保存一只狗狗。
     * 同时使用 toString() 打印出这只狗狗的信息。
     */
    public static void makeDog(String name, String breed, int age) {
        Dog d = new Dog(name, breed, age);
        d.saveDog();
        System.out.println(d.toString());
    }

    /**
     * 持久化地增加一只狗狗的年龄，并打印出一条庆祝信息。
     * 同时使用 toString() 打印出这只狗狗的信息。
     * 根据参数中的第一个非命令参数（名字）来选择要过生日的狗狗。
     * @param name 要过生日的狗狗的名字字符串。
     */
    public static void celebrateBirthday(String name) {
        Dog d = Dog.fromFile(name);
        d.haveBirthday();
        d.saveDog();
    }
}
