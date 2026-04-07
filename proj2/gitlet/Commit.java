package gitlet;
import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

import static gitlet.Repository.COMMITS_DIR;
import static gitlet.Repository.HEADS_DIR;
import static gitlet.Utils.join;

// 待办：在此添加你需要的任何导入语句

/** 表示一个 Gitlet 提交（Commit）对象。
 *  待办：最好在这里补充描述：这个类在更高层次上还承担哪些职责。
 *
 *  Commit 自己就会计算自己的 SHA-1 并把自己的字节流写进 .gitlet/objects/ 文件夹里。
 *  Repository 不需要操心文件是怎么写进去的。
 *
 *  @author Yxr_
 */
public class Commit implements Serializable{


    /** 该提交的消息内容。 */
    private String message;

    /** 第一个父指针， 存的是hash */
    private String p1;

    private String p2;

    private Date timestamp;

    /** 记录“文件名”到“Blob SHA-1”的映射快照。 */
    private TreeMap<String, String> Snapshot;


    public Commit(String message, String p1, String p2, StagingArea s)
    {
        this.message = message;
        this.p1 = p1;
        this.p2 = p2;
        this.timestamp = new Date();
        this.Snapshot = updateMap(s);
    }

    public Commit()
    {
        this.message = "initial commit";
        this.p1 = null;
        this.p2 = null;
        this.timestamp = new Date(0);
        this.Snapshot = new TreeMap<>();
    }

    public void add_file(String fileName, String blogHash)
    {
        Snapshot.put(fileName, blogHash);
    }

    public TreeMap updateMap(StagingArea s)
    {
        // 继承
        TreeMap<String, String> oldSnapshot = getHeadCommit().Snapshot;
        TreeMap<String, String> newSnapshot = new TreeMap<>();

        // 新增
        newSnapshot.putAll(s.getAddedFiles());

        // 删除
        Set<String> removeFile = s.getRemovedFiles();
        for (String fileName : removeFile)
        {
            newSnapshot.remove(fileName);
        }

        s.clear();
        return newSnapshot;
    }

    public String saveCommit()
    {
        // 序列化-生成sah1作为文件名-存入硬盘
        String commitID = getSAH1();
        File commitFile = Utils.join(COMMITS_DIR, commitID);
        Utils.writeObject(commitFile, this);
        return commitID;
    }


    // 只负责生成哈希码
    public String getSAH1()
    {
        byte[] commitBytes = Utils.serialize(this);
        return Utils.sha1(commitBytes);
    }
    // 对外暴露的只负责生成哈希码的方法， 外部只知道一个commit对象

    public static Commit getCommitByHash(String commitHash)
    {
        if (commitHash == null) return null;
        File currentCommitFile = Utils.join(COMMITS_DIR, commitHash);
        if (!currentCommitFile.exists()) return null;
        return Utils.readObject(currentCommitFile, Commit.class);
    }

    /** 组合拳：直接拿到当前 HEAD 指向的 Commit 对象 */
    public static Commit getHeadCommit()
    {
        String BranchName = Repository.getCurrentBranchName();
        String currenrCommitHash = Repository.getCommitHashBybranch(BranchName);
        return getCommitByHash(currenrCommitHash);
    }

    // 意图明确：这个文件和当前 Commit 里的版本一样吗？
    public boolean isSameAsCurrentCommit(String fileName, String currentFileHash)
    {
        String commitFileHash = this.Snapshot.get(fileName);
        return currentFileHash.equals(commitFileHash); // 里面隐藏了去找当前 Commit、读取字典等所有脏活累活
    }

    public String getP1()   {return this.p1;}
    public String getP2()   {return this.p2;}

    public String getDate()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy Z", Locale.US);
        return sdf.format(this.timestamp);
    }

    public String getMessage()
    {
        return this.message;
    }

    public boolean isMerge()
    {
        return this.p2 != null;
    }

    public TreeMap<String, String> getSnapshot()
    {
        return this.Snapshot;
    }

    public static List<String> getParents(String commitHash)
    {
        Commit currentCommit = getCommitByHash(commitHash);
        List<String> parentsCommitHash = new LinkedList<>();
        if (currentCommit.p1 != null)    parentsCommitHash.add(currentCommit.p1);
        if (currentCommit.p2 != null)    parentsCommitHash.add(currentCommit.p2);
        return parentsCommitHash;
    }

}
