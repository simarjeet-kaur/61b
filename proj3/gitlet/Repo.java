package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.*;

import static gitlet.Utils.*;

class Repo implements Serializable {

    Commit firstCommit;
    HashMap<String, String> _stagingArea;
    String _head; //should be a serialized commit SHA id
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

        if (_stagingArea.isEmpty() && _removedFiles.isEmpty()) {
            //FIXME - added the removed files part
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
            if (physical_commit.returnParent().equals(physical_commit.returnSecondParent())) {
                System.out.println("===");
                System.out.println("commit " + physical_commit.returnSHA_id());
                System.out.println("Date: " + physical_commit.returnDate());
                System.out.println(physical_commit.returnMessage());
                System.out.println();
            } else {
                System.out.println("===");
                System.out.println("commit " + physical_commit.returnSHA_id());
                System.out.println("Merge:" + physical_commit.returnParent().substring(0, 6) +
                        " " + physical_commit.returnSecondParent().substring(0, 6));
                System.out.println("Date: " + physical_commit.returnDate());
                System.out.println("Merged development into master.");
                System.out.println();
            }
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
            //finished?
            String branchName = arguments[0];
            if (!_branches.containsKey(branchName)) {
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
        if (_branches.containsKey(branchName)) {
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
        //FIXME - also consider when the commitID is 6 characters long
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
                            checkout(args); //FIXME - don't just call checkout
                        }
                    for (String trackedFile : tracked.keySet()) {
                        if (!thisCommit.returnStagingArea().containsKey(trackedFile)) {
                            File delete = new File(trackedFile);
                            delete.delete(); //FIXME - use restricted delete, check every use of this
                        }
                    }
                }
            }
            _branches.put(currentBranch, commitID);
            _stagingArea.clear();
        }
    }

    void merge(String givenBranch) {
        if (!_stagingArea.isEmpty() || !_removedFiles.isEmpty()) {
            System.out.println("You have uncommitted changes.");
        } else if (!_branches.containsKey(givenBranch)) {
            System.out.println("A branch with that name doesn't exist.");
        } else if (givenBranch.equals(currentBranch)) {
            //if trying to merge a branch with itself, print
            // "Cannot merge a branch with itself"
            //this means the current branch
            System.out.println("Cannot merge a branch with itself.");
            //FIXME - else if that checks for untracked files too
        } else {
            //get a list of the current branch's parents and for loop through this
            //until one matches the parent of the other branch which you are also for looping through
            //check for the split point by looking for the latest common ancestor
            String splitPointID = "nothing";
            Commit splitPointCommit;

            //branchName's ancestors list
            ArrayList<String> givenBranchAncestors = new ArrayList<String>();
            String commitSHAid = _branches.get(givenBranch);
            File commitPath = new File(".gitlet/commits/" + commitSHAid);
            Commit commit = readObject(commitPath, Commit.class);
            givenBranchAncestors.add(commit.returnSHA_id());
            while (!commit.returnIsFirst()) {
                String commitParentSHAid = commit.returnParent();
                givenBranchAncestors.add(commitParentSHAid);
                File nextCommitPath = new File(".gitlet/commits/" + commitParentSHAid);
                commit = readObject(nextCommitPath, Commit.class);
            }

            //currentBranch's ancestors list
            ArrayList<String> currentBranchAncestors = new ArrayList<String>();
            String commitSHAid2 = _branches.get(currentBranch);
            File commitPath2 = new File(".gitlet/commits/" + commitSHAid2);
            Commit commit2 = readObject(commitPath2, Commit.class);
            currentBranchAncestors.add(commit2.returnSHA_id());
            while (!commit2.returnIsFirst()) {
                String commitParentSHAid2 = commit2.returnParent();
                currentBranchAncestors.add(commitParentSHAid2);
                File nextCommitPath2 = new File(".gitlet/commits/" + commitParentSHAid2);
                commit2 = readObject(nextCommitPath2, Commit.class);
            }

            //now loop through the currentBranch and givenBranch ancestors until they match
            outer: for (String ancestor : givenBranchAncestors) {
                for (String currAncestor : currentBranchAncestors) {
                    if (ancestor.equals(currAncestor)) {
                        splitPointID = ancestor;
                        break outer;
                    }
                }
            }

            //FIXME - can make this a helper function later and
            // make it just return the ancestor instead of needing to break out

            //getting the split point

            File splitPointPath = new File(".gitlet/commits/" + splitPointID);
            splitPointCommit = readObject(splitPointPath, Commit.class);

            //failures

            if (splitPointID.equals(_branches.get(givenBranch))) {
                System.out.println("Given branch is an ancestor of the current branch.");
            } else if (splitPointID.equals(_branches.get(currentBranch))) {
                _branches.put(currentBranch, _branches.get(givenBranch));
                System.out.println("Current branch fast-forwarded.");
            } else {


                //the files in the given branch's staging area
                String givenBranchCommitID = _branches.get(givenBranch);
                File givenBranchCommitPath = new File(".gitlet/commits/" + givenBranchCommitID);
                Commit givenBranchCommit = readObject(givenBranchCommitPath, Commit.class);
                ArrayList<String> givenBranchFiles =
                        new ArrayList<String>(givenBranchCommit.returnStagingArea().keySet());

                String currentBranchCommitID = _branches.get(currentBranch);
                File currentBranchCommitPath = new File(".gitlet/commits/" + currentBranchCommitID);
                Commit currentBranchCommit = readObject(currentBranchCommitPath, Commit.class);
                ArrayList<String> currentBranchFiles =
                        new ArrayList<String>(currentBranchCommit.returnStagingArea().keySet());

                ArrayList<String> splitPointFiles =
                        new ArrayList<String>(splitPointCommit.returnStagingArea().keySet());

                //Any files that have been modified in the current branch but not in the
                // given branch since the split point should stay as they are.

                for (String file : givenBranchFiles) {
                    String givenBranchBlobSHA = givenBranchCommit.returnStagingArea().get(file);
                    String splitPointBlobSHA = splitPointCommit.returnStagingArea().get(file);
                    String currentBranchBlobSHA = currentBranchCommit.returnStagingArea().get(file);


                       //Any files that have been modified in the current branch but not in the
                       // given branch since the split point should stay as they are.

                    if (givenBranchBlobSHA.equals(splitPointBlobSHA)
                            && !currentBranchBlobSHA.equals(splitPointBlobSHA)) {

                        //do nothing

                        //Any files that have been modified in the given branch since the split point,
                        // but not modified in the current branch since the split point should be
                        // changed to their versions in the given branch (checked out from the commit
                        // at the front of the given branch). These files should then all be automatically
                        // staged. To clarify, if a file is "modified in the given branch since the split
                        // point" this means the version of the file as it exists in the commit at the
                        // front of the given branch has different content from the version of the file
                        // at the split point.

                    } else if (!givenBranchBlobSHA.equals(splitPointBlobSHA)) {
                        //if this is true, change them to their version in the givenBranch
                        File newBlobPath = new File(".gitlet/blobs/" + givenBranchBlobSHA);
                        String newBlob = readObject(newBlobPath, String.class);
                        File theFile = new File(file);
                        writeContents(theFile, newBlob);
                        _stagingArea.put(file, givenBranchBlobSHA);
                    }

                    //Any files that have been modified in both the current and given branch in the
                    // same way (i.e., both to files with the same content or both removed) are left
                    // unchanged by the merge.

                    else if (givenBranchBlobSHA.equals(currentBranchBlobSHA)) {
                        //FIXME - is this an issue
                    }

                    // If a file is removed in both, but a file of that name
                    // is present in the working directory that file is not removed from the working
                    // directory (but it continues to be absent—not staged—in the merge).

                    else if ((!givenBranchCommit.returnStagingArea().containsKey(file)
                            && !currentBranchCommit.returnStagingArea().containsKey(file))
                            && splitPointCommit.returnStagingArea().containsKey(file)) {
                        //should this be here? _stagingArea.put(file, givenBranchBlobSHA);
                        File checkedFile = new File(file);
                        if ((checkedFile.exists())) {
                            //make it not staged by making sure it's not in the staging area
                            //FIXME - don't do anything?, look above comment
                        }

                    //Any files that were not present at the split point and are present only in the
                    //current branch should remain as they are.

                    } else if (!splitPointCommit.returnStagingArea().containsKey(file)
                            && !givenBranchCommit.returnStagingArea().containsKey(file)
                            && currentBranchCommit.returnStagingArea().containsKey(file)) {
                        //should remain as they are - FIXME - what does this mean?
                        //do nothing? - put it in the staging area as it is currently
                        _stagingArea.put(file, currentBranchCommit.returnStagingArea().get(file));
                    }

                    //Any files that were not present at the split point and are present only
                    // in the given branch should be checked out and staged.

                    else if (!splitPointCommit.returnStagingArea().containsKey(file)
                            && givenBranchCommit.returnStagingArea().containsKey(file)
                            && !currentBranchCommit.returnStagingArea().containsKey(file)) {
                        //checking out
                        File newBlobPath = new File(".gitlet/blobs/" + givenBranchBlobSHA);
                        String newBlob = readObject(newBlobPath, String.class);
                        File theFile = new File(file);
                        writeContents(theFile, newBlob);
                        //staging
                        _stagingArea.put(file, givenBranchBlobSHA);
                    }

                    //Any files present at the split point, unmodified in the current branch,
                    // and absent in the given branch should be removed (and untracked).

                    else if (splitPointCommit.returnStagingArea().containsKey(file)
                            && splitPointCommit.returnStagingArea().get(file).equals(
                            currentBranchCommit.returnStagingArea().get(file))
                            && !givenBranchCommit.returnStagingArea().containsKey(file)) {
                                _stagingArea.remove(file);
                                _removedFiles.add(file);
                     }

                    //Any files present at the split point, unmodified in the given branch,
                    // and absent in the current branch should remain absent.

                    else if (splitPointCommit.returnStagingArea().containsKey(file)
                            && splitPointCommit.returnStagingArea().get(file).equals(
                            givenBranchCommit.returnStagingArea().get(file))
                            && !currentBranchCommit.returnStagingArea().containsKey(file)) {
                        _stagingArea.remove(file);
                    }

                    //Any files modified in different ways in the current and given branches are in
                    // conflict. "Modified in different ways" can mean that the contents of both are
                    // changed and different from other, or the contents of one are changed and the
                    // other file is deleted, or the file was absent at the split point and has different
                    // contents in the given and current branches. In this case, replace the contents of
                    // the conflicted file with

                    //<<<<<<< HEAD
                    //contents of file in current branch
                    //=======
                    // contents of file in given branch
                    // >>>>>>>

                    // might end up with something like this too:

                    //<<<<<<< HEAD
                    //contents of file in current branch=======
                    //contents of file in given branch>>>>>>>

                    else {
                        String beginning = "<<<<<<< HEAD";
                        String currentBlob = currentBranchCommit.returnStagingArea().get(file);
                        String middle = "=======";
                        String givenBlob = givenBranchCommit.returnStagingArea().get(file);
                        String end = ">>>>>>>";

                        String[] updatedContents = new String[5];
                        updatedContents[0] = beginning;
                        updatedContents[1] = currentBlob;
                        updatedContents[2] = middle;
                        updatedContents[3] = givenBlob;
                        updatedContents[4] = end;

                        File filePath = new File(file);
                        writeContents(filePath, (Object) updatedContents);

                        System.out.println("Encountered a merge conflict");

                    }

                    //Once files have been updated according to the above, and the
                    // split point was not the current branch or the given branch, merge
                    // automatically commits with the log message Merged [given branch name]
                    // into [current branch name]. Then, if the merge encountered a conflict,
                    // print the message Encountered a merge conflict. on the terminal (not the log).
                    // Merge commits differ from other commits: they record as parents both the head
                    // of the current branch (called the first parent) and the head of the branch
                    // given on the command line to be merged in.

                    givenBranchCommit.changeMessage("Merged" + givenBranch + "into" + currentBranch + ".");


                }



            }

            }

        }





    }




//look at fixmes