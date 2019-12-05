package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.*;

import static gitlet.Utils.*;

class Repo implements Serializable {

    Commit firstCommit;
    HashMap<String, String> _stagingArea;
    String _head; //should be a serialized commit
    ArrayList<String> _removedFiles;
    HashMap<String, String> _branches;
    File gitlet;
    File gitletRepo;
    File commits;
    File blobs;
    File staging_area;
    File added;
    File removed;
    File head;
    String firstCommitID;
    String currentBranch;



    Repo() {
        //making the new .gitlet directory
        gitlet = new File(".gitlet");
        gitlet.mkdir();
        //gitletRepo = new File(".gitlet/gitletRepo");
        //gitletRepo.mkdir();
        //knowing you want the .gitlet directory to have subdirectories of commits, staging area, and head, make subdirectories for these
        commits = new File(".gitlet/commits");
        commits.mkdir();
        //new directory for blobs to add the serialized version of them
        blobs = new File(".gitlet/blobs");
        blobs.mkdir();
        //new directory for staging area
        staging_area = new File(".gitlet/staging_area");
        staging_area.mkdir();
        //separate directories for the ones to add and remove
        added = new File (".gitlet/staging_area/added");
        added.mkdir();
        removed = new File (".gitlet/staging_area/removed");
        removed.mkdir();
        //new directory for head - keep head as an instance variable
        //head = new File(".gitlet/head");
        //head.mkdir();
        //(put a copy of the commit that is the head in here)

        //tracked filed kept track of by a list of file names
            //_trackedFiles = new ArrayList<String>();

        //INSTANCE VARIABLES
        //branches also a list of commits to keep track of the lists of SHA-id's of commits - should
        //be the name of the branch and it's corresponding head commit, so make it a hashmap
        //that make up a branch
        _branches = new HashMap<String, String>();

        //files that are to be removed, don't include in your next commit, take out of the hashMap
        _removedFiles = new ArrayList<String>();

        //as a backup, instance variables for the staging area and head
        _stagingArea = new HashMap<String, String>();
        _head = null;

    }

    void init() {

        //FIXME - make the initial commit
            //this needs to start automatically with an initial commit - use commit class
        Date initialDate = new Date(69, Calendar.DECEMBER, 31, 16, 0, 0);

        firstCommit = new Commit("initial commit", initialDate, new HashMap<String, String>(),
                "None", true);

        //put the serialized version of this commit in the commit directory, with the name of the
        // file being it's sha-id
        firstCommitID = firstCommit.returnSHA_id();
        Utils.writeObject(Utils.join(commits, firstCommitID), firstCommit);
        //make the branches just the master
        _branches.put("master", firstCommitID);
        // make this commit the head
        // should it be the name? - yes because you can
        // acces it later by looking through all the commits in the commit folder and deserializing from there
        //Utils.writeObject(Utils.join(head, firstCommitID), firstCommit);
            //getting rid of this because now head is just the sha id
        _head = firstCommitID;

        currentBranch = "master";
    }

    void add(String fileName) {
        //check if the file exists
            //what should be here?for file path name

        File checking = new File(fileName);
        if (!checking.exists()) {
            System.out.println("File does not exist.");
            //throw new GitletException("File does not exist.");
        } else {
            //making a blob of this file, where blob is the fileContents

            String blob = Utils.readContentsAsString(checking); //fileContents
            //used to be a string ^^
            byte[] serializedBlob = Utils.serialize(checking);
            String blobID = Utils.sha1((Object) blob);
                //add byte[] blob to the blob folder like it is done for commits
            Utils.writeObject(Utils.join(blobs, blobID), blob);
                //now if you want to grab this, you can call the blobID from the hashmap in the staging area
                    //Staging an already-staged file overwrites the previous entry in the staging area
                    // with the new contents. (automatically done)
            //add the blob to the stagingArea hashmap and staging area folder for addition
            //compare what you just added to the current commit's hashmap
                //do this by deserializing this commit
            File headPath = new File(".gitlet/commits/" + _head);
            Commit currentCommit = readObject(headPath, Commit.class);
            HashMap<String, String> stagingArea = currentCommit.returnStagingArea();
            if (stagingArea.containsKey(fileName)) {
                String currentBlob = stagingArea.get(fileName);
                if (!currentBlob.equals(blobID)) {
                    _stagingArea.put(fileName, blobID);
                   // Utils.writeObject(Utils.join(added, fileName), blob);
                }
            } else {
                _stagingArea.put(fileName, blobID);
               // Utils.writeObject(Utils.join(added, fileName), blob);
            }
        }
    }

    void commit(String message) {
        //getting initialization out of the way

        if (_stagingArea.isEmpty()) {
            //if staging area is empty
            System.out.println("No changes added to the commit.");
            //throw new GitletException("No changes added to the commit.");
        } else {

            //get the parent's staging area to combine the two
            //parent will be the old head - which you will update later
            // - it'll be the current branch's head, so use this instead
            File parentFile = new File(".gitlet/commits/" + _branches.get(currentBranch));
            Commit parentCommit = readObject(parentFile, Commit.class);
            HashMap<String, String> parentStagingArea = parentCommit.returnStagingArea();
            if (!_removedFiles.isEmpty()) {
                for (String removed : _removedFiles) {
                    parentStagingArea.remove(removed);
                }
                _removedFiles.clear();
            }

            //initializing values for the new commit
            Date date = new Date();
            HashMap<String, String> updatedStaging = new HashMap<String, String>();
            updatedStaging.putAll(parentStagingArea);
            updatedStaging.putAll(_stagingArea);
            //removed all the files that are "untracked" i.e. have been removed
            if (!_removedFiles.isEmpty()) {
                for (String file : _removedFiles) {
                    updatedStaging.remove(file);
                }
                //empty it out for later (to not make it redundant)
                _removedFiles = new ArrayList<String>();
            }

            Commit newCommit = new Commit(message, date, updatedStaging, _branches.get(currentBranch), false);
            //files in the staging area are the tracked ones, become untracked later because they aren't
            //in the staging area anymore - it's cleared

            //update the head for the most recent commit of the repo
            _head = newCommit.returnSHA_id();

            //delete the previous head from the head directory - just look at the _head
            //a head directory isnt needed? and instead you look
            // through the commit folder with the shaID and find it that way
            // boolean check = headFile.delete();
            // System.out.println(check);

            //add the commit to the folder of head and commits
            //Utils.writeObject(Utils.join(head, _head), newCommit); - just use the _head instance variable
            Utils.writeObject(Utils.join(commits, _head), newCommit);

            //update the branches hashmap
            _branches.put(currentBranch, _head);

            //now look through the staging area hashmap and update these files - ?? actually dont the point is just to
            //keep a version of the file, you dont need to update or change any files - instead clear it out

            //FIXME - empty out the staging area
            _stagingArea.clear();
        }

    }

    void rm(String fileName) {
        //need to look at the current commit (head) and see if it's keeping track of this
            //file
        File headFile = new File (".gitlet/commits/" + _branches.get(currentBranch));
        Commit headCommit = readObject(headFile, Commit.class);
        HashMap<String, String> headStagingArea = headCommit.returnStagingArea();
        if (!headStagingArea.containsKey(fileName)) {
            System.out.println("No reason to remove the file.");
            //throw new GitletException("No reason to remove the file.");
        } else {
            _stagingArea.remove(fileName);
            _removedFiles.add(fileName);
        }
    }

    void log() {

        //FIXME - make log work for merges

        File filePath = new File(".gitlet/commits/" + _head);
        Commit initialCommit = readObject(filePath, Commit.class);
        while (!initialCommit.returnIsFirst()) {
            if (initialCommit.returnParent().equals(initialCommit.returnSecondParent())) {
                System.out.println("===");
                System.out.println("commit " + initialCommit.returnSHA_id());
                System.out.println("Date: " + initialCommit.returnDate());
                System.out.println(initialCommit.returnMessage());
                System.out.println();
                File newFilePath = new File(".gitlet/commits/" + initialCommit.returnParent());
                initialCommit = readObject(newFilePath, Commit.class);
            } else {
                System.out.println("===");
                System.out.println("commit " + initialCommit.returnSHA_id());
                System.out.println("Merge:" + initialCommit.returnParent().substring(0, 6) +
                        " " + initialCommit.returnSecondParent().substring(0, 6));
                System.out.println("Date: " + initialCommit.returnDate());
                System.out.println("Merged development into master.");
                System.out.println();
                File newFilePath = new File(".gitlet/commits/" + initialCommit.returnParent());
                initialCommit = readObject(newFilePath, Commit.class);
            }
        }
        System.out.println("===");
        System.out.println("commit " + initialCommit.returnSHA_id());
        System.out.println("Date: " + initialCommit.returnDate());
        System.out.println(initialCommit.returnMessage());
        System.out.println();
    }

    void globalLog() {
        List<String> commits = plainFilenamesIn(".gitlet/commits");
        for (String commit : commits) {
            File filePath = new File(".gitlet/commits/" + commit);
            Commit physical_commit = readObject(filePath, Commit.class);
            System.out.println("===");
            System.out.println("commit " + physical_commit.returnSHA_id());
            System.out.println("Date: " + physical_commit.returnDate());
            System.out.println(physical_commit.returnMessage());
        }
    }

    void find(String message) {
        boolean check = false;
        List<String> commits = plainFilenamesIn(".gitlet/commits");
        for (String commit : commits) {
            File filePath = new File(".gitlet/commits/" + commit);
            Commit physical_commit = readObject(filePath, Commit.class);
            if (physical_commit.returnMessage().equals(message)) {
                check = true;
                System.out.println(physical_commit.returnSHA_id());
            }
        }
        if (!check) {
            System.out.println("Found no commit with that message.");
        }
    }

    void status() {
        //Printing out the branches
        System.out.println("=== Branches ===");
        for (String branchName : _branches.keySet()) {
            if (branchName.equals(currentBranch)) {
                System.out.println(branchName + "*");
            } else {
                System.out.println(branchName);
            }
        }

        //printing out the staged files
        System.out.println("=== Staged Files ===");
        for (String stagedFile : _stagingArea.keySet()) {
            System.out.println(stagedFile);
        }

        //printing out the removed files
        System.out.println("=== Removed Files ===");
        for (String removedFile : _removedFiles) {
            System.out.println(removedFile);
        }

    }

    void checkout(String[] arguments) {
        if (arguments.length == 2 && arguments[0].equals("--")) {
                //to test: System.out.println("worked");
            String fileName = arguments[1];
            //get the head commit
            File headPath = new File(".gitlet/commits/" + _head);
            Commit headCommit = readObject(headPath, Commit.class);
                //to test: System.out.println(headCommit.returnMessage());

            //get it's staging area and the blob with this file
            HashMap<String, String> tempSA = headCommit.returnStagingArea();
            String blobID = tempSA.get(fileName);

            //deserialize this blob and update the file
            File blobPath = new File(".gitlet/blobs/" + blobID);
            String blob = readObject(blobPath, String.class);
            File thisFile = new File(fileName);
            Utils.writeContents(thisFile, blob);

        } else if (arguments.length == 3 && arguments[1].equals("--")) {
                //to test: System.out.println("did not work2");
            String commitID = arguments[0];
            String fileName = arguments[2];
            List<String> commits = plainFilenamesIn(".gitlet/commits");
            for (String commit : commits) {
                if (commit.equals(commitID)) {
                    //getting the commit with this ID
                    File filePath = new File(".gitlet/commits/" + commit);
                    Commit thisCommit = readObject(filePath, Commit.class);

                    //grab it's staging area
                    HashMap<String, String> tempSA = thisCommit.returnStagingArea();
                    String blobID = tempSA.get(fileName);

                    //deserialize the blob for this file and update the file according to this
                    File blobPath = new File(".gitlet/blobs/" + blobID);
                    String blob = readObject(blobPath, String.class);
                    File thisFile = new File(fileName);
                        //changed this to /gitlet at the beginning
                    Utils.writeContents(thisFile, blob);
                }
            }
        } else if (arguments.length == 1) {
            //FIXME - finish this
            String branchName = arguments[0];
            if (!_branches.keySet().contains(branchName)) {
                System.out.println("A branch with that name does not exist.");
            } else if (branchName.equals(currentBranch)) {
                System.out.println("Cannot remove the current branch.");
            } else {
                String branchHeadSHA = _branches.get(branchName);

                File commitPath = new File(".gitlet/commits/" + branchHeadSHA);
                Commit thisCommit = readObject(commitPath, Commit.class);

                //getting the staging area for thisCommit
                HashMap<String, String> commitSA = thisCommit.returnStagingArea();

                //updating all the files in this SA
                for (String fileName : commitSA.keySet()) {
                    String blobID = commitSA.get(fileName);
                    //deserialize this blob and update the file
                    File blobPath = new File(".gitlet/blobs/" + blobID);
                    String blob = readObject(blobPath, String.class);
                    File thisFile = new File(fileName);
                    Utils.writeContents(thisFile, blob);
                }

                //Any files that are tracked in the current branch but are not present in the
                // checked-out branch are deleted.

                //get current branch head
                File currentHeadPath = new File(".gitlet/commits/" + _head);
                Commit currentHead = readObject(currentHeadPath, Commit.class);

                for (String fileName : currentHead.returnStagingArea().keySet()) {
                    if (!thisCommit.returnStagingArea().containsKey(fileName)) {
                        //delete the file
                        File deletedFile = new File(fileName);
                        deletedFile.delete();
                        //FIXME - does this remove it from the working directory?
                    }
                }

                //staging area is cleared, unless the checked-out branch is the current branch
                if (!currentBranch.equals(branchName)) {
                    _stagingArea.clear();
                }
                currentBranch = branchName;
                _head = branchHeadSHA;

            }
        }
    }

    void branch(String branchName) {
        if (_branches.keySet().equals(branchName)) {
            System.out.println("A branch with that name already exists.");
        } else {
            _branches.put(branchName, _head);
        }
    }

    void rmBranch(String branchName) {
        if (!_branches.containsKey(branchName)) {
            System.out.println("A branch with that name does not exist.");
        } else if (currentBranch.equals(branchName)) {
            System.out.println("Cannot remove the current branch.");
        } else {
            _branches.remove(branchName);
        }
        //FIXME - is this all? as in you cant access these
        // commits through this branch anymore?
    }

    void reset(String commitID) {
        //current commit things
        File currentCommit = new File(".gitlet/commits/" + _head);
        Commit current = readObject(currentCommit, Commit.class);
        HashMap<String, String> tracked = current.returnStagingArea();

        List<String> commits = plainFilenamesIn(".gitlet/commits");
        if (!commits.contains(commitID)) {
            System.out.print("No commit with that id exists.");
        } else {
            for (String commit : commits) {
                if (commit.equals(commitID)) {
                    File filePath = new File(".gitlet/commits/" + commit);
                    Commit thisCommit = readObject(filePath, Commit.class);
                    for (String fileName : thisCommit.returnStagingArea().keySet()) {
                            String[] args = new String[2];
                            args[0] = commitID;
                            args[1] = fileName;
                            checkout(args);
                        }
                    for (String trackedFile : tracked.keySet()) {
                        if (!thisCommit.returnStagingArea().containsKey(trackedFile)) {
                            File delete = new File(trackedFile);
                            delete.delete();
                        }
                    }
                }
            }
            _branches.put(currentBranch, commitID);
            _stagingArea.clear();
        }
    }

    void merge(String branchName) {

    }
}

//check commits serialzing and deserializing
//look at fixmes