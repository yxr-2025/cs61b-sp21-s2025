package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.TreeMap;
import java.util.TreeSet;

import static gitlet.Repository.GITLET_DIR;
import static gitlet.Utils.join;

public class StagingArea implements Serializable
{
    /**
     *  added 和 removed 集合。
     *   对外api：包含 add(fileName, sha1)、remove(fileName)、clear() 等方法。
     *   对内操作：同样，它可以有自己的 save() 和 load() 方法，负责把自己序列化到 .gitlet/index 文件中。
     *
     * 每次执行 add 或 rm 命令时，就把这个对象反序列化读取到内存，
     * 更新哈希表里的键值对，然后再序列化覆盖写回到 .gitlet/index 文件里。
     * 清空暂存区，无非就是把这两个哈希表 .clear() 一下再存回去
     */


    /** .gitlet 子文件 ，index负责存储 暂存区状态*/
    public static final File INDEX_FILE = join(GITLET_DIR, "index");

    // （暂存增加/修改的文件）
    private TreeMap<String, String> added;

    // （暂存准备删除的文件）
    private TreeSet removed;

    public StagingArea()
    {
        this.added = new TreeMap<>();
        this.removed = new TreeSet<>();
    }

    // 去重过的文件
    public void stage(String fileName, String sha1)
    {
        this.added.put(fileName, sha1);
    }

    // stage暂存
    public void unstage(String fileName)
    {
        this.added.remove(fileName);
    }

    public void stageForRemoval(String fileName)
    {
        this.removed.add(fileName);
    }

    public void unstageForRemoval(String fileName)
    {
        this.removed.remove(fileName);
    }

    public boolean isSameStaged(String name, String hash)
    {
        String stagedHash = this.added.get(name);
        return hash.equals(stagedHash);
    }

    public boolean isInStaged(String fileName)
    {
        return this.added.containsKey(fileName);
    }

    public boolean isInUnstaged(String fileName)
    {
        return this.removed.contains(fileName);
    }

    public TreeMap getAddedFiles()  {return this.added;}

    public TreeSet getRemovedFiles()    {return this.removed;}

    public void clear()
    {
        this.added.clear();
        this.removed.clear();
    }

    public void save()
    {
        Utils.writeObject(INDEX_FILE, this);
    }

    public static StagingArea load()
    {
        if(INDEX_FILE.exists())
        {
            return Utils.readObject(INDEX_FILE, StagingArea.class);
        }
        else
        {
            return new StagingArea();
        }
    }

    public String getHash(String fileName)
    {
        return this.added.get(fileName);
    }
}
