package gitlet;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

/** Driver class for Gitlet, the tiny stupid version-control system.
 *  @author
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND> .... */
    public static void main(String... args) {

        /**List of viable commands.*/
        String[] arrayOfCommands = {"init", "add", "commit", "rm", "log", "global-log", "find", "status", "checkout",
                "branch", "rm-branch", "reset", "merge", "add-remote", "rm-remote", "push", "fetch", "pull"};

        /**Gitlet file path to the repo.*/
        File gitletFile = new File(".gitlet/gitletRepo/repo");

        /**Checking if the args is input correctly and calling these arguments on _repo.*/
         try {
            if (args.length == 0) {
                throw new GitletException("Please enter a command.");
            } else {
                if (Arrays.asList(arrayOfCommands).contains(args[0])) {
                    if (args[0].equals("init")) {
                        if (repoExists()) {
                            throw new GitletException("A Gitlet version-control system already exists in the current directory.");
                        } else {
                            Repo _repo = new Repo();
                            _repo.init();
                            //serialize the repo to get it later into gitletRepo
                            //File gitletFile = new File(".gitlet/gitletRepo");
                            //Utils.writeObject(Utils.join(gitletFile, "repo"), _repo);
                            Utils.writeObject(gitletFile, _repo);
                        }
                    } else if (repoExists()) {
                        //de-serialize the repo
                        //File gitletFile = new File(".gitlet/gitletRepo");
                        Repo _repo = Utils.readObject(gitletFile, Repo.class);
                        //get the rest of the args
                        String [] rest_of_args;
                       // System.out.print(args);
                        if (args.length > 1) {
                            rest_of_args = Arrays.copyOfRange(args, 1, args.length);
                        } else {
                            rest_of_args = new String[0];
                        }
                        //call the command
                        callingCommand(args[0], rest_of_args, _repo);
                        //re-serialize the repo
                       // Utils.writeObject(Utils.join(gitletFile, "repo"), _repo);
                        Utils.writeObject(gitletFile, _repo);
                    }
                } else {
                    throw new GitletException("No command with that name exists.");
                }
            }
        } catch (GitletException e) {
             throw new GitletException();
            //System.exit(0); //FIXME - is this right?
        }
    }

        private static void callingCommand(String command, String[] arguments, Repo repo) {
            switch (command) {
                case "add":
                    if (arguments.length == 1) {
                        repo.add(arguments[0]);
                    }
                    break;
                case "commit":
                    if (arguments.length == 1) {
                        repo.commit(arguments[0]);
                    }
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
                    if (arguments.length == 3 || arguments.length == 1) {
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
                    if (arguments.length == 1) {
                        repo.merge(arguments[0]);
                    }
                    break;
            }
        }
    /**Checking if the repo has already been initialized.*/
    private static boolean repoExists() {
        File checking = new File(".gitlet");
        return checking.isDirectory();
    }

    /**Repo is _repo.  Used to represent the whole .gitlet repo.*/
    //Repo _repo = null;

}


//look at the args main takes in and use this to determine which call to make
//methods can go here or somewhere else\\t

//staging area, commit, repo

//staging area  - where you put stuff before you commit
//you can't commit something you haven't added
//how do you get what you put in your init as instance variables

//branch is a pointer to a commit - a single branch only refers to a single commit
//(it's a chain)

//use hash codes to check if they've changed or not

//commit
//should have certain things
//commit should have
//time, date, message, SHA-id, blobs, parent commit - using SHA-ID of the parent
//get the name of it , sha-id of it, to compare - use this to get checkout

//init makes an initial commit

//not a detached head as long as its at the front of the branch

//serialization
//turns an object into a file
//deserialization: turning the file back into the object
//needed because you keep running gitlet every time - the information needs to last
//serialize in files so when you run some command, you can look around to see if you
//have the right files

//make a commit object
//it'd contain location, time, message, blob (could be multiple adds)
//every commit parent - this would be saved as a sha-id - so when you deserialize it
//doesn't affect

//head is the reference to most current commit

//every time you add
//you should make a blob - assuming it's valid

//what does a blob contain
//need an object so you can serialize it
//file name
//file contents
