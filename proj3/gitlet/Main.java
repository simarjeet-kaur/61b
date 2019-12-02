package gitlet;

import java.util.ArrayList;
import java.util.Arrays;

/** Driver class for Gitlet, the tiny stupid version-control system.
 *  @author
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND> .... */
    public static void main(String... args) {

       // If a user doesn't input any arguments, print the message Please enter a command. and exit.
       // If a user inputs a command that doesn't exist, print the message No command with that name exists. and exit.
       // If a user inputs a command with the wrong number or format of operands, print the message Incorrect operands. and exit.
       // If a user inputs a command that requires being in an initialized Gitlet working directory (i.e., one containing
        // a .gitlet subdirectory), but is not in such a directory, print the message Not in an initialized Gitlet directory.

        /**Repo is theRepo.  Used to represent the whole .gitlet repo.*/
        Repo theRepo = null;

        /**List of viable commands.*/
        String[] arrayOfCommands = {"init", "add", "commit", "rm", "log", "global-log", "find", "status", "checkout",
            "branch", "rm-branch", "reset", "merge", "add-remote", "rm-remote", "push", "fetch", "pull"};

        /**Checking if the args is input correctly and calling these arguments on theRepo.*/
        try {
            if (args.length == 0) {
                //Utils.message("Please enter a command.");
                throw new GitletException("Please enter a command.");
            } else {
                if (Arrays.stream(arrayOfCommands).anyMatch(args[0]::equals)) { //seeing if there is a valid command put in
                    //if there is, make an array for the operands.  then check for the init first - it needs to exist before you call anything
                    //so here, set something for the init case
                    //if (repoExists()) {
                    //    theRepo = getTheRepo();
                    //}
                    //now call whatever the arg is onto theRepo
                    if (args[0] == "init") {
                        if (repoExists()) {
                            throw new GitletException("A Gitlet version-control system already exists in the current directory.");
                        } else {
                            theRepo = new Repo();
                        }
                    } else if (repoExists()) {
                        switch (args[0]) {
                            case "add":
                                try {
                                    theRepo.add(args[1]);
                                    //FIXME - says to add this assert statement that is always false?
                                } catch (Exception e) {
                                    throw new GitletException("Please enter a file name.");
                                    //FIXME - do i need this? for this or any other command that requires a file name
                                }
                                break;
                            case "commit":
                                try {
                                    theRepo.commit(args[1]);
                                } catch (Exception e) {
                                    throw new GitletException("Please enter a commit message.");
                                }
                                break;
                            case "rm":
                                theRepo.rm(args[1]);
                                break;
                            case "log":
                                theRepo.log();
                                break;
                            case "global-log":
                                theRepo.globalLog();
                                break;
                            case "find":
                                theRepo.find();
                                break;
                            case "status":
                                theRepo.status();
                                break;
                            case "checkout":
                                theRepo.checkout();
                                break;
                            case "branch":
                                theRepo.branch();
                                break;
                            case "rm-branch":
                                theRepo.rmBranch();
                                break;
                            case "reset":
                                theRepo.reset();
                                break;
                            case "merge":
                                theRepo.merge();
                                break;
                        }
                    } else {
                        throw new GitletException("A repo does not exist yet");
                        //FIXME - do we need this?
                    }
                } else {
                    throw new GitletException("No command with that name exists.");
                }
            }
        } catch (Exception e) {
            throw new GitletException("Incorrect argument format.");
            //FIXME - is this also right??
        }
    }
    /**Checking if the repo has already been initialized.*/
    private static boolean repoExists() {
        return true;
    }
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
