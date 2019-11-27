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

        /**Checking to see that the args begins with the right operands.  Sets variable command to the command.*/

        String command;

        if (args.length > 2) {
            if (args[0] != "java" && args[1] != "gitlet.Main") {
                Utils.message("Incorrect operands.");
                throw new GitletException();
            } else {
                command = args[2];
            }
        }

        /**List of viable commands.*/
        String[] arrayOfCommands = {"init", "add", "commit", "rm", "log", "global-log", "find", "status", "checkout",
            "branch", "rm-branch", "reset", "merge", "add-remote", "rm-remote", "push", "fetch", "pull"};

        /**Checking if the args is input correctly.*/
        try {
            if (args.length == 0) {
                Utils.message("Please enter a command.");
                throw new GitletException();
            } else {
                if (Arrays.stream(arrayOfCommands).anyMatch(args[0]::equals)) {

                } else {
                    Utils.message("No command with that name exists.");
                    throw new GitletException();
                }
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

        static void init() { //check this too

            //ex init: check if .gitlet directory exists

            //make a directory called .gitlet
                //make a file about the serialization of the repo

            //usage: java gitlet.Main init

            //should probably make a .gitlet folder and make a new class that does all
            //the other stuff

            //description:
            //automatically starts with one commit - has no files and the commit message
            //is "initial commit" (no punc)
            //single branch: master - points to this initial commit, master is the current branch
            //make instance variable initial commit, current branch, points to master
            //time stamp: 00:00:00 UTC, Thursday, 1 January 1970 in whatever format you want
            //make another instance variable for this
            //since the initial commit in all reps created by gitlet has the exact same content,
            //it will follow that all reps automatically share this commit
            //all have the same UID, all commits in reps will trace back to this
            //these instance variables never change
            //tree data structure? keep adding on to it? make a new class that represents this?

            //runtime: should be constant

            //failure cases: if there is already a gitlet version control system, abort it
                //should not overwrite the existing system with a new one
                //should print error message "A Gitlet version-control system already exists in the current directory."
                    //how to check for this? maybe look at instance variables in the main method?

            //dangerous?: no

            //our line count: about 25

            //make a new gitlet object class and serialize at the end


        }

        void add() {

            //usage: java gitlet.Main add [file name]

            //description: adds a copy of the file as it currently exists to the staging area
            //(see the commit command)
            //the staging area should be somewhere in .gitlet (another file/class to make?)
                //make an instance variable of this class in the init?
            //if the current version is identical to the current commit, do not
            //stage it to be added, remove it from the staging area if it's already there
            //if it's been marked as removed (see gitlet rm) delete that mark

            //runtime: worst case should run in linear time relative to the size of the file
            //being added

            //failure cases: if the file does not exist, print the error message "File does not exist."

            //dangerous?: no

            //our line count: about 20

        }

        void commit() {

            //usage: java gitlet.Main commit [message]

            //saves a snapshot of certain files in the current commit and staging area so they can
            //be restored at a later time, creating a new commit
            //commit is said to be tracking to saved file
            //be default, each commit's snapshot of files will be exactly the same as its parent's commit
            //will only keep the files exactly as they are, not update them
            //a commit only updates files it's tracking that have been staged at the time of commit

            // Each commit is identified by its SHA-1 id, which must
            // include the file (blob) references
            // of its files, parent reference, log message, and commit time. - you can do this by
            // putting it into the sha-id utils function - every time you make a commit, this way
            //if the commit is the exact same, you'll get the same sha-id
            //have a commit class that does all of this and you can grab all of these things from it

            //runtime: should be constant - committing must increase the size of the directory by no
            //more than the number of staged filed to commit
            //don't store redundant copies (maybe don't store the sha-id's that are the same?)
            //save whole copies, don't worry about saving diffs etc

            //failure cases: if no files have been staged, abort "No changes added to the commit."
                //check the staging area file
            //every commit MUST have a non blank message - if it doesn't, print "Please enter a
            //commit message."
            //it is NOT a failure for tracked files to be missing from the working directory
                //ignore everything out of the .gitlet directory
                //what does this mean?

        }

        void rm() {

            //usage: java gitlet.Main rm [file name]

            //unstage the file, if it's currently staged
                //take it out of the staging area
            //if it's trached int he current commit, mark to indicate that it is not to be
                //how do we keep track of this?


        }
    }

}
