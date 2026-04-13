package gitlet;

/** Gitlet 的驱动类，Gitlet 是 Git 版本控制系统的一个子集。
 *  @author Yxr_
 */

// Main 类应该主要调用 Repository 类中的辅助方法。
// 参考第 6 次实验中的 CapersRepository 和 Main 类，了解我们推荐的结构示例

public class Main {

    /** 用法：java gitlet.Main 参数，其中参数包含
     *  java gitlet.Main init
     *  java gitlet.Main add [file name]
     *  java gitlet.Main commit [message]
     *  java gitlet.Main rm [file name]
     *  java gitlet.Main log
     *  java gitlet.Main global-log
     *  java gitlet.Main find [commit message]
     *  java gitlet.Main status
     *  java gitlet.Main checkout -- [file name]
     *  java gitlet.Main checkout [commit id] -- [file name]
     *  java gitlet.Main checkout [branch name]
     *  java gitlet.Main branch [branch name]
     *  java gitlet.Main rm-branch [branch name]
     *  java gitlet.Main reset [commit id]
     *  java gitlet.Main merge [branch name]
     */
    public static void main(String[] args) {
        // 待办：如果 args 为空数组怎么办？
        if (args.length == 0)
        {
            System.out.println("Please enter a command.");
            return;
        }

        String firstArg = args[0];
        switch(firstArg) {
            case "init":
                Repository.init();
                break;
            case "add":
                Repository.addHelper(args[1]);
                break;
            case "commit":
                Repository.commitHelper(args[1]);
                break;
            case "rm":
                Repository.rmCommand(args[1]);
                break;
            case "log":
                Repository.logHelper();
                break;
            case "global-log":
                Repository.globalLogHelper();
                break;
            case "find":
                Repository.findHelper(args[1]);
                break;
            case "status":
                Repository.statusHelper();
                break;
            case "checkout":
                Repository.checkoutHelper(args);
                break;
            case "branch":
                Repository.branchHelper(args[1]);
                break;
            case "rm-branch":
                Repository.rmBranchHelper(args[1]);
                break;
            case "reset":
                Repository.resetHelper(args[1]);
                break;
            case "merge":
                Repository.mergeHelper(args[1]);
                break;
            default:
                System.out.println("No command with that name exists.");
                break;

        }
    }
}