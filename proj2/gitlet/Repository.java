package gitlet;

import java.io.File;
import java.util.*;

import static gitlet.Utils.*;

/** 代表一个 Gitlet 版本库（仓库）。
 *
 *  （管理 .gitlet 目录结构、持久化对象、协调命令执行等）
 *  隐藏文件系统细节，提供简洁 API
 *
 *  @author Yxr_
 */
public class Repository {


    /** 当前工作目录（即用户执行命令时所在的目录）。*/
    public static final File CWD = new File(System.getProperty("user.dir"));

    /** .gitlet 目录（即 Gitlet 仓库的元数据目录，类似 Git 的 .git 目录）。*/
    public static final File GITLET_DIR = join(CWD, ".gitlet");

    /** .gitlet 子目录 ，objects负责存储commits, blob对象。*/
    public static final File OBJECTS_DIR = join(GITLET_DIR, "objects");

    /** .objects 子目录 ，commits负责存储commits对象*/
    public static final File COMMITS_DIR = join(OBJECTS_DIR, "commits");

    /** .objects 子目录 ，blobs负责存储blob对象。*/
    public static final File BLOB_DIR = join(OBJECTS_DIR, "blobs");

    /** .gitlet 子目录 ，refs负责存储heads文件夹*/
    public static final File REFS_DIR = join(GITLET_DIR, "refs");

    /** .refs 子目录 ，heads负责存储branch*/
    public static final File HEADS_DIR = join(REFS_DIR, "heads");

    /** .gitlet 子文件  HEAD负责存储当前分支名*/
    public static final File HEAD_FILE = join(GITLET_DIR, "HEAD");

    public static final File REMOTE_DIR = join(GITLET_DIR, "remote");

    public static final File REMOTENAME_DIR = join(REFS_DIR, "remote");

    public static void init() {
        /**
         * 初始化文件夹
         * initial commit
         * initial master
         * HEAD
         * index
         */
        DIR_CREATER();

        String newCommitId = initCommit();

        branchDoubleHelper("master", newCommitId);

        Utils.writeContents(HEAD_FILE, "master");
        //updateHead(String branchName)

        new StagingArea();
    }

    // 业务逻辑 1创建新分支
    private static void branchDoubleHelper(String branchName, String newCommitId) {
        File branchFile = Utils.join(HEADS_DIR, branchName);
        if(branchFile.exists())
        {
            System.out.println("A branch with that name already exists.");
            System.exit(0);
        }

        // 写进HEADS_DIR里
        writeBranch(branchName, newCommitId);
    }

    // 暴露给用户的
    public static void branchHelper(String branchName) {
        String currentHash = getCommitHashBybranch(getCurrentBranchName());
        branchDoubleHelper(branchName, currentHash);
    }

    // 业务逻辑 2 更新branch文件的hashID
    private static void updateCurrentBranch(String newHashID) {
        String currentBranch = getCurrentBranchName();
        writeBranch(currentBranch, newHashID);
    }

    // 底层实现逻辑：writeBranch（只负责无脑更新）
    // 调用者负责检查
    private static void writeBranch(String branchName, String latestHasID) {
        File branchFile = Utils.join(HEADS_DIR, branchName);
        Utils.writeContents(branchFile, latestHasID);
    }

    private static String initCommit() {
        Commit initCommit = new Commit();
         return initCommit.saveCommit();
    }

    //保存blob,底层实现逻辑：（只负责无脑写入）
    private static void saveBlob(String fileHasID, byte[] fileBytes) {
        File bolbFile = Utils.join(BLOB_DIR, fileHasID);
        if (!bolbFile.exists())
        {
            Utils.writeContents(bolbFile, fileBytes);
        }
    }

    private static byte[] loadBlob(String fileHash) {
        File blobFile = Utils.join(BLOB_DIR, fileHash);

        // 如果文件不存在，说明你的哈希索引出错了
        if (!blobFile.exists()) {
            throw new GitletException("Blob content missing for hash: " + fileHash);
        }

        return Utils.readContents(blobFile);
    }

    // 通过HEAD读取当前分支名
    /** 读取 HEAD 文件，获取当前分支名 (例如 "master") */
    public static String getCurrentBranchName() {
        if (!(HEAD_FILE.exists())) {
            System.out.println("HEAD_FILE don`t exist");
            System.exit(0);
        }
        return Utils.readContentsAsString(HEAD_FILE);
    }

    // 通过branch分支名（String）读取currentCommitHash
    public static String getCommitHashBybranch(String branchName) {
        File branchfile = Utils.join(HEADS_DIR, branchName);
        if (!branchfile.exists())   return null;
        return Utils.readContentsAsString(branchfile);
    }

    public static Commit searchParent(Commit lastCommit) {
        String p1 = lastCommit.getP1();
        return Commit.getCommitByHash(p1, COMMITS_DIR);
    }


    public static void addHelper(String fileName) {
        StagingArea s = StagingArea.load();
        addDoubleHelper(fileName, s);
        s.save();
    }

    private static void addDoubleHelper(String fileName, StagingArea s) {
        File file = Utils.join(CWD, fileName);

        if (!file.exists()){
            System.out.println("File does not exist.");
            return;
        }

        byte[] fileBytes = Utils.readContents(file);

        String fileHash = Utils.sha1(fileBytes);

        Commit c = Commit.getHeadCommit();

        // 情况 1: 和 HEAD 一样（去重/撤销暂存）
        if(c.isSameAsCurrentCommit(fileName, fileHash)) {
            s.unstage(fileName);
            s.unstageForRemoval(fileName);
            s.save();
            return;
        }

        // 情况 2: 和暂存区一样（避免重复操作）
        if (s.isSameStaged(fileName, fileHash))    {return;}

        saveBlob(fileHash, fileBytes);

        s.stage(fileName, fileHash);
    }

    public static void commitHelper(String message) {
        if (message == null || message.isEmpty()) {
            System.out.println("Please enter a commit message.");
            return; // 别忘了 return，否则程序会继续往下跑！
        }

        StagingArea s = StagingArea.load();
        if (s.getAddedFiles().isEmpty() && s.getRemovedFiles().isEmpty()) {
            System.out.println("No changes added to the commit.");
            return;
        }

        String p1 = getCommitHashBybranch(getCurrentBranchName());
        String p2 = null;
        Commit newCommit = new Commit(message, p1, p2, s);
        s.clear();
        String commitID = newCommit.getSAH1();
        updateCurrentBranch(commitID);
        // 生成newcommit id
        newCommit.saveCommit();
        s.clear();
        s.save();
    }

    public static void logHelper() {
        /**
         * ===
         * commit a0da1ea5a15ab613bf9961fd86f010cf74c7ee48
         * Date: Thu Nov 9 20:00:05 2017 -0800
         * A commit message.
         *
         * ===
         * commit 3e8bf1d794ca2e9ef8a4007275acf3751c7170ff
         * Merge: 4975af1 2c1ead1
         * Date: Sat Nov 11 12:30:00 2017 -0800
         * Merged development into master.
         *
         */
        // 初始化
        Commit headCommit = Commit.getHeadCommit();

        String currentCommithash = getCommitHashBybranch(getCurrentBranchName());

        // 递归调用
        logDoubleHelper(currentCommithash, headCommit);
    }

    private static void logDoubleHelper(String commitHash, Commit currentCommit) {
        String p1 = currentCommit.getP1();
        String p2 = currentCommit.getP2();

        writeCommitData(currentCommit, commitHash);

        // 业务逻辑：是否到顶了   （对父commit的判断 -> 信息在当前commit的p里）
        if((p1 == null) && (p2 == null))    // 到顶了
        {
            return;
        }

        // 还在继续
        logDoubleHelper(p1, searchParent(currentCommit));
    }

    private static void writeCommitData(Commit c, String commitHash) {
        System.out.println("===");
        System.out.printf("commit %s%n", commitHash);
        if (c.isMerge())    {System.out.printf("Merge: %.7s %.7s%n", c.getP1(), c.getP2());}
        System.out.printf("Date: %s%n", c.getDate());
        System.out.printf("%s%n", c.getMessage());
        System.out.println();
    }

    /** 供 Main 调用的命令行入口 */
    public static void rmCommand(String fileName) {
        StagingArea s = StagingArea.load();
        rmHelper(fileName, s);
        s.save();
    }

    private static void rmHelper(String fileName, StagingArea s) {
        Commit headCommit = Commit.getHeadCommit();

        // 未被追踪
        if (!s.isInStaged(fileName) && !headCommit.getSnapshot().containsKey(fileName)) {
            System.out.println("No reason to remove the file.");
            return;
        }

        // 情况 A： 如果文件在暂存区，直接取消暂存（unstage）
        s.unstage(fileName);

        // 情况 B： 如果文件在当前 Commit 里，标记为“待删除”（removedFiles），并从工作目录物理删除
        // if fileName in headCommit -> s.stageForRemoval(fileName)
        if (headCommit.getSnapshot().containsKey(fileName))
        {
            s.stageForRemoval(fileName);
            File delectFile = Utils.join(CWD, fileName);
            Utils.restrictedDelete(delectFile);
        }
    }

    public static void globalLogHelper() {
        // 扫描把文件变为可遍历对象
        List<String> commitHashes = Utils.plainFilenamesIn(COMMITS_DIR);

        // 遍历输出
        for (String hash : commitHashes) {
            // 反序列化为commit后传给 writeCommitData(Commit c, String commitHash)
            Commit c = Commit.getCommitByHash(hash, COMMITS_DIR);
            writeCommitData(c, hash);
        }
    }

    public static void findHelper(String message) {
        /**
         * 打印所有包含该提交消息的提交的 ID，每行一个 ID。
         * 如果有多个这样的提交，它会把 id 打印出来，分开行。
         * 提交消息是单操作数;要表示多词消息，操作数在引号中表示，就像下面的 commit 命令一样
         */
        boolean isFinded = false;

        // 扫描把文件变为可遍历对象
        List<String> commitHashes = Utils.plainFilenamesIn(COMMITS_DIR);

        // 遍历输出
        for (String hash : commitHashes) {
            // 反序列化为commit后传给 writeCommitData(Commit c, String commitHash)
            Commit c = Commit.getCommitByHash(hash, COMMITS_DIR);

            String commitMessage = c.getMessage();
            if (commitMessage.equals(message)) {
                System.out.println(hash);
                isFinded = true;
            }
        }

        if (!isFinded){
            System.out.println("Found no commit with that message.");
        }
    }

    public static void statusHelper() {
        printBranches();
        printStagedFiles();
        printRemovedFiles();
        printAffectedFiles();
        printUntrackedFiles();
    }

    private static void printBranches() {
        String currentBranchName = getCurrentBranchName();
        List<String> branches = Utils.plainFilenamesIn(HEADS_DIR);

        // 打印当前分支名
        System.out.println("=== Branches ===");
        System.out.printf("*%s%n", currentBranchName);

        // 排序
        Collections.sort(branches);

        // 按字典序打印剩下分支名
        for (String branchName : branches) {
            if (!branchName.equals(currentBranchName)) {
                System.out.printf("%s%n", branchName);
            }
        }
        System.out.println();
    }

    private static void printStagedFiles() {
        StagingArea s = StagingArea.load();
        TreeMap<String, String> StagedFiles = s.getAddedFiles();

        System.out.println("=== Staged Files ===");
        for (String StagedFile : StagedFiles.keySet()) {
            System.out.printf("%s%n", StagedFile);
        }
        System.out.println();
    }

    private static void printRemovedFiles() {
        StagingArea s = StagingArea.load();
        TreeSet<String> RemovedFiles = s.getRemovedFiles();

        System.out.println("=== Removed Files ===");
        for (String RemovedFile : RemovedFiles) {
            System.out.printf("%s%n", RemovedFile);
        }
        System.out.println();
    }

    private static void printAffectedFiles() {
        Commit c = Commit.getHeadCommit();
        StagingArea s = StagingArea.load();

        Set<String> allExpectedFiles = new TreeSet<>();
        allExpectedFiles.addAll(c.getSnapshot().keySet());
        allExpectedFiles.addAll(s.getAddedFiles().keySet());

        // 存贮被改的代码
        Set<String> affectedFiles = new TreeSet<>();

        for (String file : allExpectedFiles) {
            // 获取当前应该比对的“标准哈希”
            String savedHash = s.isInStaged(file)
                             ? s.getHash(file)
                             : c.getSnapshot().get(file);

            File f = Utils.join(CWD, file);

            if (!f.exists()) {
                // 场景：记录里有，但硬盘上没了。且你还没运行过 `rm`
                if (!s.isInUnstaged(file)) {
                    // 排除掉已经显式运行过 rm 命令的文件
                    affectedFiles.add(file);
                }
            } else {
                // 场景：硬盘上有，但内容和记录的不一样
                byte[] fileByte = Utils.readContents(f);
                String workFileHash = Utils.sha1(fileByte);
                if (!workFileHash.equals(savedHash)) {
                    affectedFiles.add(file);
                }
            }

        }
        printAffectedFilesHelper(affectedFiles);
    }

    private static void printAffectedFilesHelper(Set<String> affectedFiles) {
        System.out.println("=== Modifications Not Staged For Commit ===");

        for (String file : affectedFiles) {
            File f = Utils.join(CWD, file);

            if(!f.exists()) {
                System.out.printf("%s (deleted)%n", file);
            } else {
                System.out.printf("%s (modified)%n", file);
            }
        }
        System.out.println();
    }

    private static Set<String> getUntrackedFiles()
    {
        StagingArea s = StagingArea.load();
        Commit c = Commit.getHeadCommit();
        Set<String> untrackerFile = new TreeSet<>();

        // 获取工作区所有的file名
        List<String> cwdFiles = Utils.plainFilenamesIn(CWD);

        // 排序
        Collections.sort(cwdFiles);

        for (String fileName : cwdFiles) {
            if ((!c.getSnapshot().containsKey(fileName)) && (!s.isInStaged(fileName)) && (!s.isInUnstaged(fileName))) {
                untrackerFile.add(fileName);
            }
        }
        return untrackerFile;
    }

    private static void printUntrackedFiles() {
        System.out.println("=== Untracked Files ===");

        Set<String> untrackFile = getUntrackedFiles();

        for (String fileName : untrackFile) {
            System.out.printf("%s%n", fileName);
        }
        System.out.println();
    }

    public static void checkoutHelper(String[] args) {
        if (args.length == 3 && args[1].equals("--")) {
            // 第一种情况：checkout -- [file]
            String commitHash = getCommitHashBybranch(getCurrentBranchName());
            fetchFileFromCommit(commitHash, args[2]);
        } else if (args.length == 4 && args[2].equals("--") ) {
            // 第二种情况：checkout [id] -- [file]
            fetchFileFromCommit(args[1], args[3]);
        } else if (args.length == 2) {
            // 第三种情况：checkout [branch]
            checkoutBranch(args[1]);
        } else {
            System.out.println("Incorrect operands.");
        }
    }

    private static void fetchFileFromCommit(String commitId, String fileName) {
        Commit c = Commit.getCommitByHash(commitId, COMMITS_DIR);

        // 第一优先级：能否找到commit
        if (c == null) {
            System.out.println("No commit with that id exists.");
            System.exit(0);
        }

        // 第二优先级：能否找到文件
        if (!c.getSnapshot().containsKey(fileName)) {
            System.out.println("File does not exist in that commit.");
            System.exit(0);
        }

        String blobHashId = c.getSnapshot().get(fileName);
        OverwriteCWDByFilenameAndHash(fileName, blobHashId);

    }

    private static void checkoutBranch(String branchName) {
        StagingArea s = StagingArea.load();

        // 0. 各种预检 (Pre-checks)
        Commit targetCommit = validateBranchAndConflicts(branchName);

        // 1. 清理不再需要的文件
        clearObsoleteFiles(targetCommit);

        // 2. 覆盖/恢复目标文件
        restoreFilesFromCommit(targetCommit);

        // 3. 状态更新
        finalizeCheckout(branchName, s);
    }

    private static Commit validateBranchAndConflicts(String branchName) {
        File branchFile =  Utils.join(HEADS_DIR, branchName);

        if (!branchFile.exists()) {
            branchFile = Utils.join(REMOTENAME_DIR, branchName);
        }

        if (!branchFile.exists()) {
            System.out.println("No such branch exists.");
            System.exit(0);
        }
        if (getCurrentBranchName().equals(branchName)) {
            System.out.println("No need to checkout the current branch.");
            System.exit(0);
        }

        String targetCommitHash = Utils.readContentsAsString(branchFile);

        Commit targetCommit = Commit.getCommitByHash(targetCommitHash, COMMITS_DIR);

        if (isCheckUntrackedConflict(targetCommit)) {
            System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
            System.exit(0);
        }

        return targetCommit;
    }

    private static void clearObsoleteFiles(Commit targetCommit) {
        List<String> cwdFiles = Utils.plainFilenamesIn(CWD);

        // delect: 清理CWD中不在targetCommit里的文件
        for (String fileName : cwdFiles) {
            File f = Utils.join(CWD, fileName);

            if (!targetCommit.getSnapshot().containsKey(fileName)) {
                Utils.restrictedDelete(f);
            }
        }
    }

    private static void restoreFilesFromCommit(Commit targetCommit) {
        // Overwrite: 遍历targetCommit快照， 覆盖写入CWD
        for (Map.Entry<String, String> entry : targetCommit.getSnapshot().entrySet()) {
            String fileName = entry.getKey();
            String hashId = entry.getValue();
            OverwriteCWDByFilenameAndHash(fileName, hashId);
        }
    }

    private static void finalizeCheckout(String branchName, StagingArea s) {
        // 更新暂存区
        s.clear();
        s.save();

        // 更新HEAD指针
        Utils.writeContents(HEAD_FILE, branchName);
    }

    private static void OverwriteCWDByFilenameAndHash(String fileName, String hashId) {
        byte[] commitedFile = loadBlob(hashId);
        writeToCWD(fileName, commitedFile);
    }

    /** * 真正的复用核心：把任何字节/字符串写到 CWD 的某个文件
     */
    private static void writeToCWD(String fileName, byte[] content) {
        File f = Utils.join(CWD, fileName);
        Utils.writeContents(f, content);
    }

    private static boolean isCheckUntrackedConflict(Commit targetCommit) {
        // 文件在当前 Commit 中是被跟踪的，那么它在 reset 或 checkout 时被修改或删除是安全的
        Set<String> untrackedFiles = getUntrackedFiles();

        for (String fileName : untrackedFiles) {
            if (targetCommit.getSnapshot().containsKey(fileName)){
                return true;
            }
        }
        return false;
    }

    public static void rmBranchHelper(String branchName) {
        // 检查合法性
        File branchFile =  Utils.join(HEADS_DIR, branchName);

        if (!branchFile.exists()) {
            System.out.println("A branch with that name does not exist.");
            System.exit(0);
        }

        if (getCurrentBranchName().equals(branchName)) {
            System.out.println("Cannot remove the current branch.");
            System.exit(0);
        }

        // 物理删除
        branchFile.delete();
    }

    public static void resetHelper(String commitHash) {
        StagingArea s = StagingArea.load();

        Commit targetCommit = Commit.getCommitByHash(commitHash, COMMITS_DIR);

        // 0. 预处理
        validateCommitAndConflicts(targetCommit);

        // 1. 清理不再需要的文件
        clearObsoleteFiles(targetCommit);

        // 2. 覆盖/恢复目标文件
        restoreFilesFromCommit(targetCommit);

        // 3. 状态更新
        finalizeReset(commitHash, s);
    }

    private static void validateCommitAndConflicts(Commit targetCommit) {
        if (targetCommit == null) {
            System.out.println("No commit with that id exists.");
            System.exit(0);
        }
        // 文件在当前 Commit 中是被跟踪的，那么它在 reset 或 checkout 时被修改或删除是安全的
        // 有未追踪文件在targetcommit
        if (isCheckUntrackedConflict(targetCommit)) {
            System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
            System.exit(0);
        }
    }

    private static void finalizeReset(String commitHash,StagingArea s) {
        // 修改当前分支文件（如 refs/heads/master）里的哈希码
        String CurrentBranchName = getCurrentBranchName();
        File f = Utils.join(HEADS_DIR, CurrentBranchName);
        Utils.writeContents(f, commitHash);

        // 更新暂存区
        s.clear();
        s.save();
    }

    // 暴露给用户的
    public static void mergeHelper(String branchName){
        mergeDoubleHelper(branchName, HEADS_DIR);
    }

    private static void mergeDoubleHelper(String branchName, File headDir) {
        StagingArea s = StagingArea.load();
        String headCommitHash = getCommitHashBybranch(getCurrentBranchName());

        File f = Utils.join(headDir, branchName);

        boolean hasConflit = false;

        Commit headCommit = Commit.getHeadCommit();

        Commit targetCommit = mergePreChecks(s, branchName, headDir);

        String targetCommitHash = Utils.readContentsAsString(f);

        Commit splitCommit = searchSplit(headCommitHash, targetCommitHash, branchName);

        if (threeWayMerge(splitCommit, headCommit, targetCommit, s)) hasConflit = true;

        String currentBranchName = getCurrentBranchName();

        String message = "Merged " + branchName + " into " +currentBranchName + ".";

        Commit newCommit = new Commit(message, headCommitHash, targetCommitHash, s);
        String commitID = newCommit.saveCommit();
        updateCurrentBranch(commitID);

        s.clear();
        s.save();

        if (hasConflit) System.out.println("Encountered a merge conflict.");
    }

    private static Commit mergePreChecks(StagingArea s, String branchName, File headDir) {
        File branchFile = Utils.join(headDir, branchName);

        if (!s.getRemovedFiles().isEmpty() || !s.getAddedFiles().isEmpty()) {
            System.out.println("You have uncommitted changes.");
            System.exit(0);
        }

        if(!branchFile.exists()) {
            System.out.println("A branch with that name does not exist.");
            System.exit(0);
        }

        if (getCurrentBranchName().equals(branchName)) {
            System.out.println("Cannot merge a branch with itself.");
            System.exit(0);
        }

        String targetCommitHash = Utils.readContentsAsString(branchFile);

        Commit targetCommit = Commit.getCommitByHash(targetCommitHash, COMMITS_DIR);

        if (isCheckUntrackedConflict(targetCommit)) {
            System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
            System.exit(0);
        }

        return targetCommit;
    }

    // 先染色，后捕捉
    private static Commit searchSplit(String headCommitHash, String targetCommitHash, String branchName) {
        // 第一步：标记head分支的所有祖先
        Set<String> ancestors = getAncestors(headCommitHash, COMMITS_DIR);

        // 分裂点 == 目标分支头
        if (ancestors.contains(targetCommitHash)) {
            System.out.println("Given branch is an ancestor of the current branch.");
            System.exit(0);
        }

        Queue<String> queue = new LinkedList<>();
        queue.add(targetCommitHash);

        while (!queue.isEmpty()) {
            String currentHash = queue.poll();

            for (String p : Commit.getParents(currentHash, COMMITS_DIR)) {
                if (ancestors.contains(p)) {
                    // Fast-forward
                    if (p.equals(headCommitHash)) {
                        System.out.println("Current branch fast-forwarded.");
                        checkoutBranch(branchName);
                        System.exit(0);
                    }
                    return Commit.getCommitByHash(p, COMMITS_DIR);
                }
            }
        }
        return null;
    }

    private static boolean threeWayMerge(Commit s, Commit h, Commit t, StagingArea sa) {
        Set<String> allFile = new HashSet<>();
        allFile.addAll(s.getSnapshot().keySet());
        allFile.addAll(t.getSnapshot().keySet());
        allFile.addAll(h.getSnapshot().keySet());

        boolean isconflit = false;

        for (String fileName : allFile) {
            String spiltFileId = s.getSnapshot().get(fileName);
            String targetFileId = t.getSnapshot().get(fileName);
            String headFileId = h.getSnapshot().get(fileName);

            boolean changedInT = !Objects.equals(targetFileId, spiltFileId);
            boolean changedInH = !Objects.equals(headFileId, spiltFileId);

            // 都动了
            if (changedInT && changedInH) {
                // 不一样 - 冲突
                if (!Objects.equals(headFileId, targetFileId)) {
                    isconflit = mergeByConflict(targetFileId, headFileId, sa, fileName);
                }

                // 如果动的一样 - 跳过
            }
            // 听 Given 的：意味着你要把 Given 分支的意志强加给当前的工作区，所以必须处理文件 IO。
            else if (changedInT && !changedInH) {
                overwriteCWDandStage(fileName, targetFileId, sa);
            }
            // 听 head 的：意味着你认可了当前工作区的状态，所以只需保持静默。
        }

        return isconflit;
    }

    // 改写CWD + 暂存
    private static void overwriteCWDandStage(String fileName, String targetFileId, StagingArea s) {

        if (targetFileId == null) {
            rmHelper(fileName, s);
            s.unstage(fileName);
        }
        else {
            OverwriteCWDByFilenameAndHash(fileName, targetFileId);
            s.stage(fileName, targetFileId);
        }
    }

    // 读取在blob的f1，f2 合在一起，写进CWD，暂存
    private static boolean mergeByConflict(String targetFileId, String headFileId, StagingArea s, String fileName){
        String headFile = "";
        if (headFileId != null) {
            File hFile = Utils.join(BLOB_DIR, headFileId);
            headFile = Utils.readContentsAsString(hFile);
        }

        String targetFile = "";
        if (targetFileId != null) {
            File tFile = Utils.join(BLOB_DIR, targetFileId);
            targetFile = Utils.readContentsAsString(tFile);
        }

        // <<<<<<< HEAD
        // [Current 内容]
        // =======
        // [Given 内容]
        // >>>>>>>
        StringBuilder sb = new StringBuilder("<<<<<<< HEAD\n");
        sb.append(headFile)
                .append("=======\n")
                .append(targetFile)
                .append(">>>>>>>\n");
        String newFile = sb.toString();

        File f = Utils.join(CWD, fileName);
        Utils.writeContents(f, newFile);
        addDoubleHelper(fileName, s);

        return true;
    }

    // java gitlet.Main add-remote [remote name] [name of remote directory]/.gitlet
    public static void addRemoteHelper(String remoteName, String remoteDirectory){
        File remoteFile = Utils.join(REMOTE_DIR, remoteName);

        if (remoteFile.exists()){
            System.out.println("A remote with that name already exists.");
            return;
        }

        // 建立一个 remoteName 到 remoteDirectory 的映射
        Utils.writeContents(remoteFile, remoteDirectory);
    }

    public static void rmRemoteHelper(String remoteName){
        File remoteFile = Utils.join(REMOTE_DIR, remoteName);

        if (!remoteFile.exists()){
            System.out.println("A remote with that name does not exist.");
            return;
        }

        Utils.restrictedDelete(remoteFile);
    }

    // 获取本地存储得远程仓库url
    private static File readDirFromRemoteName(String remoteName){
        File remoteNameFile = Utils.join(REMOTE_DIR, remoteName);

        if (!remoteNameFile.exists()){
            System.out.println("Remote directory not found.");
            System.exit(0);
        }

        // 读出String:  name of remote directory/.gitlet
        String DirName = Utils.readContentsAsString(remoteNameFile);

        return new  File(DirName);
    }

    // java gitlet.Main fetch [remote name] [remote branch name]
    public static void fetchHelper(String remoteName, String remoteBranchName){

        //找到远程目录的 .gitlet。如果不存在，打印 Remote directory not found.
        File remoteDir = readDirFromRemoteName(remoteName);

        File remoteBranchFile = preFetch(remoteDir, remoteBranchName);

        String remoteCommitHash = Utils.readContentsAsString(remoteBranchFile);

        // File srcGit, File destGit, String targetCommitHash
        syncObject(remoteDir, GITLET_DIR, remoteCommitHash);

        updateRemoteBranchName(remoteName, remoteBranchName, remoteCommitHash);
    }

    private static File preFetch(File remoteDir, String remoteBranchName){
        if (!remoteDir.exists()){
            System.out.println("Remote directory not found.");
            System.exit(0);
        }

        //读取远程的 refs/heads/[remote branch name] 获取其 branchCommit 提交的 ID。
        File remoteBranchFile = Utils.join(remoteDir, "refs", "heads", remoteBranchName);

        if (!remoteBranchFile.exists()){
            System.out.println("That remote does not have that branch.");
            System.exit(0);
        }

        return remoteBranchFile;
    }

    private static void syncObject(File srcGit, File destGit, String targetCommitHash){
        //递归获取：从该提交开始，遍历其所有祖先提交。如果本地没有这个提交对象，就把它从远程的 objects 文件夹拷贝到本地。
        File srcCommitDir = Utils.join(srcGit, "objects", "commits");

        Set<String> ancestors = getAncestors(targetCommitHash, srcCommitDir);

        for (String commitHash : ancestors) {

            copyObject(srcGit, destGit, commitHash, "commits");

            //同步 Blob：对于每个新拷贝的提交，确保其 snapshot 里的所有 Blob 也被拷贝到本地。
            Commit targetCommit = Commit.getCommitByHash(commitHash, srcCommitDir);
            Map<String, String> targetCommitSnapshot = targetCommit.getSnapshot();

            for (String remoteblobId : targetCommitSnapshot.values()){
                copyObject(srcGit, destGit, remoteblobId, "blobs");
            }
        }
    }

    private static void updateRemoteBranchName(String remoteName,
                                               String remoteBranchName,
                                               String remoteCommitHash){
        //更新本地引用：在本地创建/更新 refs/remotes/[remote name]/[remote branch name]
        File remoteNameFile = Utils.join(REMOTENAME_DIR, remoteName);
        if (!remoteNameFile.exists())
        {
            remoteNameFile.mkdirs();
        }
        File remoteBranchFile = Utils.join(remoteNameFile, remoteCommitHash);
        Utils.writeContents(remoteBranchFile, remoteCommitHash);
    }

    /** 将一个对象从源 Gitlet 目录拷贝到目标 Gitlet 目录
     *  自带去重
     */
    private static void copyObject(File srcGit, File destGit, String objId, String DirName){
        File srcObject = Utils.join(srcGit, "objects", DirName, objId);
        File desDir = Utils.join(destGit, "objects", DirName);

        if (!desDir.exists()){
            desDir.mkdirs();
        }

        File desObject = Utils.join(desDir, objId);

        if (!desObject.exists()) {
            byte[] srcBytes = Utils.readContents(srcObject);
            Utils.writeContents(desObject, srcBytes);
        }
    }

    // 通用的逻辑来获取“从某个 Commit 开始的所有祖先节点列表”：
    private static Set<String> getAncestors(String CommitHash, File commitFile){
        // 第一步：标记head分支的所有祖先
        Set<String> ancestors = new HashSet<>();
        Queue<String> queue = new LinkedList<>();

        queue.add(CommitHash);
        ancestors.add(CommitHash);

        // 下一步没到头
        while (!queue.isEmpty()) {
            String currentHash = queue.poll();

            for (String p : Commit.getParents(currentHash, commitFile)) {
                if (!ancestors.contains(p)) {
                    ancestors.add(p);
                    queue.add(p);
                }
            }
        }
        return ancestors;
    }

    // java gitlet.Main push [remote name] [remote branch name]
    public static void pushHelper(String remoteName, String remoteBranchName){
        // 如果远程 .gitlet 目录不存在，打印 Remote directory not found.
        File remoteDir = readDirFromRemoteName(remoteName);

        // 如果远程不存在remoteBranch，则创建
        String remoteHeadID = null;

        File remoteBranchFile = Utils.join(remoteDir, "refs", "heads", remoteBranchName);

        if (remoteBranchFile.exists()) {
            remoteHeadID = Utils.readContentsAsString(remoteBranchFile);
        }

        String localHeadHash = getCommitHashBybranch(getCurrentBranchName());

        Set<String> ancestors = getAncestors(localHeadHash, COMMITS_DIR);

        if (!(remoteHeadID == null) && !ancestors.contains(remoteHeadID)){
            System.out.println("Please pull down remote changes before pushing.");
            return;
        }

        // 搬运  // File srcGit, File destGit, String targetCommitHash
        syncObject(GITLET_DIR, remoteDir, localHeadHash);

        // 更新或创建分支 不管有没有 refs/heads/branchName
        if (!remoteBranchFile.getParentFile().exists()) {
            remoteBranchFile.getParentFile().mkdirs();
        }
        Utils.writeContents(remoteBranchFile, localHeadHash);
    }

    // java gitlet.Main pull [remote name] [remote branch name]
    public static void pullHelper(String remoteName, String remoteBranchName){
        fetchHelper(remoteName, remoteBranchName);

        File remoteDir = readDirFromRemoteName(remoteName);

        File remoteHeadsDir = Utils.join(remoteDir, "remoteName", "remoteBranchName");

        mergeDoubleHelper(remoteBranchName, remoteHeadsDir);
    }

    private static void DIR_CREATER() {
        if(GITLET_DIR.exists()) {
            System.out.println("A Gitlet version-control system already exists in the current directory.");
            System.exit(0);
        }
        GITLET_DIR.mkdir();

        if (!(OBJECTS_DIR.exists())){
            OBJECTS_DIR.mkdirs();
        }
        if (!(REFS_DIR.exists())){
            REFS_DIR.mkdirs();
        }
        if (!(HEADS_DIR.exists())){
            HEADS_DIR.mkdirs();
        }
        if (!(COMMITS_DIR.exists())){
            COMMITS_DIR.mkdirs();
        }
        if (!(BLOB_DIR.exists())){
            BLOB_DIR.mkdirs();
        }
        if (!(REMOTE_DIR.exists())){
            REMOTE_DIR.mkdirs();
        }

    }
}
