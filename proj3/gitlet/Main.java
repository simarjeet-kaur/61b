package gitlet;

import java.io.File;
import java.util.Arrays;

/** Driver class for Gitlet, the tiny stupid version-control system.
 *  @author Simarjeet Kaur (Collaborators: Nirmol Kaur)
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND> .... */
    public static void main(String... args) {

        String[] arrayOfCommands = {"init", "add", "commit", "rm", "log",
            "global-log", "find", "status", "checkout",
            "branch", "rm-branch", "reset", "merge", "add-remote",
            "rm-remote", "push", "fetch", "pull"};

        File gitletFile = new File(".gitlet/gitletRepo");

        if (args.length == 0) {
            System.out.println("Please enter a command.");
        } else {
            String command = args[0];
            String[] restOfArgs = Arrays.copyOfRange(args,
                    1, args.length);
            if (Arrays.asList(arrayOfCommands).contains(command)) {
                if (command.equals("init")) {
                    if (repoExists()) {
                        System.out.println("A Gitlet version-control "
                                + "system already exists in the current"
                                + " directory.");
                    } else {
                        Repo repo = new Repo();
                        repo.init();
                        Utils.writeObject(gitletFile, repo);
                    }
                } else if (repoExists()) {
                    Repo repo = Utils.readObject(gitletFile, Repo.class);
                    callingCommand(command, restOfArgs, repo);
                    Utils.writeObject(gitletFile, repo);
                }
            } else {
                System.out.println("No command with that name exists.");
            }
        }
    }

    /**Calling command calls the command on the repo.
     * @param arguments string array
     * @param command string of the command
     * @param repo the repo you're editing*/
    private static void callingCommand(String command,
                                       String[] arguments, Repo repo) {
        switch (command) {
        case "add":
            addHelper(arguments, repo);
            break;
        case "commit":
            commitHelper(arguments, repo);
            break;
        case "rm":
            if (arguments.length == 1) {
                repo.rm(arguments[0]);
            }
            break;
        case "log":
            if (arguments.length == 0) {
                repo.log();
            }
            break;
        case "global-log":
            if (arguments.length == 0) {
                repo.globalLog();
            }
            break;
        case "find":
            if (arguments.length == 1) {
                repo.find(arguments[0]);
            }
            break;
        case "status":
            if (arguments.length == 0) {
                repo.status();
            }
            break;
        case "checkout":
            if (arguments.length < 4) {
                repo.checkout(arguments);
            }
            break;
        case "branch":
            if (arguments.length == 1) {
                repo.branch(arguments[0]);
            }
            break;
        case "rm-branch":
            if (arguments.length == 1) {
                repo.rmBranch(arguments[0]);
            }
            break;
        case "reset":
            if (arguments.length == 1) {
                repo.reset(arguments[0]);
            }
            break;
        case "merge":
            mergeHelper(arguments, repo);
            break;
        default:
        }
    }

    /**Checking if the repo has already been initialized.
     * @return a boolean*/
    private static boolean repoExists() {
        File checking = new File(".gitlet");
        return checking.isDirectory();
    }

    /**Calls commit on the repo with given arguments.
     * @param arguments string array
     * @param repo Repo*/
    private static void commitHelper(String[] arguments, Repo repo) {
        if (arguments.length == 1) {
            repo.commit(arguments[0]);
        } else if (arguments.length == 0 || arguments[0].equals("\"\"")) {
            System.out.println("Please enter a commit message.");
        }
    }

    /**Calls add on the repo with given arguments.
     * @param arguments string array
     * @param repo Repo*/
    private static void addHelper(String[] arguments, Repo repo) {
        if (arguments.length == 1) {
            repo.add(arguments[0]);
        }
    }

    /**Calls merge on the repo with given arguments.
     * @param arguments string array
     * @param repo Repo*/
    private static void mergeHelper(String[] arguments, Repo repo) {
        if (arguments.length == 1) {
            repo.merge(arguments[0]);
        }
    }

}
