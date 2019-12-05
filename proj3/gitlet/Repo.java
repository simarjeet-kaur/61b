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
        Date initialDate = new Date(1969, Calendar.DECEMBER, 31, 16, 0, 0);

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

        File checking = new File("gitlet/" + fileName);
        if (!checking.exists()) {
            throw new GitletException("File does not exist.");
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
                    Utils.writeObject(Utils.join(added, fileName), blob);
                }
            } else {
                _stagingArea.put(fileName, blobID);
                Utils.writeObject(Utils.join(added, fileName), blob);
            }
        }
    }

    void commit(String message) {
        //getting initialization out of the way

        if (_stagingArea.isEmpty()) {
            //if staging area is empty
            throw new GitletException("No changes added to the commit.");
        }

        //get the parent's staging area to combine the two
            //parent will be the old head - which you will update later
            // - it'll be the current branch's head, so use this instead
        File parentFile = new File (".gitlet/commits/" + _branches.get(currentBranch));
        Commit parentCommit = readObject(parentFile, Commit.class);
        HashMap<String, String> parentStagingArea = parentCommit.returnStagingArea();

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

        Commit newCommit = new Commit(message, date, updatedStaging, _head, false);
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
        _stagingArea = new HashMap<String, String>();

    }

    void rm(String fileName) {
        //need to look at the current commit (head) and see if it's keeping track of this
            //file
        File headFile = new File (".gitlet/commits/" + _branches.get(currentBranch));
        Commit headCommit = readObject(headFile, Commit.class);
        HashMap<String, String> headStagingArea = headCommit.returnStagingArea();
        if (!headStagingArea.containsKey(fileName)) {
            throw new GitletException("No reason to remove the file.");
        }
        _stagingArea.remove(fileName);
        _removedFiles.add(fileName);

    }

    void log() {
        File filePath = new File(".gitlet/commits/" + _head);
        Commit initialCommit = readObject(filePath, Commit.class);
        while (!initialCommit.returnIsFirst()) {
            System.out.println("===");
            System.out.println("commit " + initialCommit.returnSHA_id());
            System.out.println("Date: " + initialCommit.returnDate());
            System.out.println(initialCommit.returnMessage());
            File newFilePath = new File(".gitlet/commits/" + initialCommit.returnParent());
            initialCommit = readObject(newFilePath, Commit.class);
        }
        System.out.println("===");
        System.out.println("commit " + initialCommit.returnSHA_id());
        System.out.println("Date: " + initialCommit.returnDate());
        System.out.println(initialCommit.returnMessage());
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

    void find(String commitID) {

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

            String fileName = arguments[1];
            //get the head commit
            File headPath = new File(".gitlet/commits/" + _head);
            Commit headCommit = readObject(headPath, Commit.class);

            //get it's staging area and the blob with this file
            HashMap<String, String> tempSA = headCommit.returnStagingArea();
            String blobID = tempSA.get(fileName);

            //deserialize this blob and update the file
            File blobPath = new File(".gitlet/blobs/" + blobID);
            String blob = readObject(blobPath, String.class);
            File thisFile = new File(fileName);
            Utils.writeContents(thisFile, blob);

        } else if (arguments.length == 3 && arguments[1].equals("--")) {
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
                    Utils.writeContents(thisFile, blob);
                }
            }
        } else if (arguments.length == 1) {
            //FIXME - finish this
            String branchName = arguments[0];
        }
    }

    void branch(String branchName) {
    }

    void rmBranch(String branchName) {
    }

    void reset(String commitID) {
    }

    void merge(String branchName) {
    }
}
