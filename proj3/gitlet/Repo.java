package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Calendar;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

import static gitlet.Utils.*;

/**Repo object that represents the entire working gitlet repo.
 * @author Simarjeet Kaur*/

class Repo implements Serializable {

    /**Hashmap to represent the staging area.*/
    private HashMap<String, String> _stagingArea;
    /**SHA-id of the head.*/
    private String _head;
    /**Removed files array list.*/
    private ArrayList<String> _removedFiles;
    /**Removed files that have been committed.*/
    private ArrayList<String> _committedRemovedFiles;
    /**Hashmap of branches and their corresponding head commit SHA-id.*/
    private HashMap<String, String> _branches;
    /**File of commits.*/
    private File commits;
    /**File of blobs.*/
    private File blobs;
    /**String of the name of the current branch.*/
    private String currentBranch;
    /**Initial year.*/
    private static final int INITIAL_YEAR = 69;
    /**Initial day.*/
    private static final int INITIAL_DAY = 31;

    /**Repo object initialization.*/
    Repo() {
        File gitlet = new File(".gitlet");
        gitlet.mkdir();
        commits = new File(".gitlet/commits");
        commits.mkdir();
        blobs = new File(".gitlet/blobs");
        blobs.mkdir();
        _branches = new HashMap<String, String>();
        _removedFiles = new ArrayList<String>();
        _committedRemovedFiles = new ArrayList<String>();
        _stagingArea = new HashMap<String, String>();
        _head = null;

    }

    /**init method of the repo.*/
    void init() {
        Date initialDate = new Date(INITIAL_YEAR, Calendar.DECEMBER,
                INITIAL_DAY, 16, 0, 0);
        Commit firstCommit = new Commit("initial commit",
                initialDate, new HashMap<String, String>(),
                "None", true);
        String firstCommitID = firstCommit.returnSHAId();
        Utils.writeObject(Utils.join(commits, firstCommitID), firstCommit);
        _branches.put("master", firstCommitID);
        _head = firstCommitID;
        currentBranch = "master";
    }

    /**Add method.
     * @param fileName is the file to add*/
    void add(String fileName) {
        File checking = new File(fileName);
        if (!checking.exists()) {
            System.out.println("File does not exist.");
        } else {
            String blob = Utils.readContentsAsString(checking);
            byte[] serializedBlob = Utils.serialize(checking);
            String blobID = Utils.sha1((Object) blob);
            Utils.writeObject(Utils.join(blobs, blobID), blob);
            File headPath = new File(".gitlet/commits/" + _head);
            Commit currentCommit = readObject(headPath, Commit.class);
            HashMap<String, String> stagingArea =
                    currentCommit.returnStagingArea();
            if (stagingArea.containsKey(fileName)) {
                String currentBlob = stagingArea.get(fileName);
                if (!currentBlob.equals(blobID)) {
                    _stagingArea.put(fileName, blobID);
                }
            } else {
                _stagingArea.put(fileName, blobID);
            }
            _committedRemovedFiles.remove(fileName);
            _removedFiles.remove(fileName);
        }
    }

    /**Commit method.
     * @param message is the commit message.*/
    void commit(String message) {
        if (_stagingArea.isEmpty() && _removedFiles.isEmpty()) {
            System.out.println("No changes added to the commit.");
        } else if (message.equals("") || message.equals("\"\"")) {
            System.out.println("Please enter a commit message.");
        } else {
            File parentFile = new File(".gitlet/commits/"
                    + _branches.get(currentBranch));
            Commit parentCommit = readObject(parentFile, Commit.class);
            HashMap<String, String> parentStagingArea = parentCommit
                    .returnStagingArea();
            if (!_removedFiles.isEmpty()) {
                for (String removed : _removedFiles) {
                    parentStagingArea.remove(removed);
                    _committedRemovedFiles.add(removed);
                }
                _removedFiles.clear();
            }
            Date date = new Date();
            HashMap<String, String> updatedStage =
                    new HashMap<String, String>();
            updatedStage.putAll(parentStagingArea);
            updatedStage.putAll(_stagingArea);
            if (!_removedFiles.isEmpty()) {
                for (String file : _removedFiles) {
                    updatedStage.remove(file);
                }
                _removedFiles.clear();
            }
            Commit newCommit = new Commit(message, date, updatedStage,
                    _branches.get(currentBranch), false);
            _head = newCommit.returnSHAId();
            Utils.writeObject(Utils.join(commits, _head), newCommit);
            _branches.put(currentBranch, _head);
            _stagingArea.clear();
        }
    }

    /**Remove method.
     * @param fileName to be removed*/
    void rm(String fileName) {
        File headFile = new File(".gitlet/commits/"
                + _branches.get(currentBranch));
        Commit headCommit = readObject(headFile, Commit.class);
        HashMap<String, String> headSA = headCommit.returnStagingArea();
        if (!headSA.containsKey(fileName)
                && !_stagingArea.containsKey(fileName)) {
            System.out.println("No reason to remove the file.");
        } else {
            File filePath = new File(fileName);
            if (_stagingArea.containsKey(fileName)) {
                _stagingArea.remove(fileName);
            }
            if (headSA.containsKey(fileName)) {
                _committedRemovedFiles.add(fileName);
                _removedFiles.add(fileName);
            }
            if (filePath.exists() && (headCommit.returnStagingArea()
                    .containsKey(fileName)
                    || _stagingArea.containsKey(fileName))) {
                filePath.delete();
            }
        }
    }

    /**Log method.*/
    void log() {
        File filePath = new File(".gitlet/commits/" + _head);
        Commit initialCommit = readObject(filePath, Commit.class);
        while (!initialCommit.returnIsFirst()) {
            if (initialCommit.returnParent().equals(initialCommit
                    .returnSecondParent())) {
                System.out.println("===");
                System.out.println("commit " + initialCommit.returnSHAId());
                System.out.println("Date: " + initialCommit.returnDate());
                System.out.println(initialCommit.returnMessage());
                System.out.println();
                File newFilePath = new File(".gitlet/commits/"
                        + initialCommit.returnParent());
                initialCommit = readObject(newFilePath, Commit.class);
            } else {
                System.out.println("===");
                System.out.println("commit " + initialCommit.returnSHAId());
                System.out.println("Merge:"
                        + initialCommit.returnParent().substring(0, 7) + " "
                        + initialCommit.returnSecondParent().substring(0, 7));
                System.out.println("Date: " + initialCommit.returnDate());
                System.out.println(initialCommit.returnMessage());
                System.out.println();
                File newFilePath = new File(".gitlet/commits/"
                        + initialCommit.returnParent());
                initialCommit = readObject(newFilePath, Commit.class);
            }
        }
        System.out.println("===");
        System.out.println("commit " + initialCommit.returnSHAId());
        System.out.println("Date: " + initialCommit.returnDate());
        System.out.println(initialCommit.returnMessage());
        System.out.println();
    }

    /**Global log method.*/
    void globalLog() {
        List<String> commits1 = plainFilenamesIn(".gitlet/commits/");
        for (String commit : commits1) {
            File filePath = new File(".gitlet/commits/" + commit);
            Commit physicalCommit = readObject(filePath, Commit.class);
            if (physicalCommit.returnParent().equals(physicalCommit
                    .returnSecondParent())) {
                System.out.println("===");
                System.out.println("commit " + physicalCommit.returnSHAId());
                System.out.println("Date: " + physicalCommit.returnDate());
                System.out.println(physicalCommit.returnMessage());
                System.out.println();
            } else {
                System.out.println("===");
                System.out.println("commit " + physicalCommit.returnSHAId());
                System.out.println("Merge:"
                        + physicalCommit.returnParent().substring(0, 7) + " "
                        + physicalCommit.returnSecondParent().substring(0, 7));
                System.out.println("Date: " + physicalCommit.returnDate());
                System.out.println(physicalCommit.returnMessage());
                System.out.println();
            }
        }
    }

    /**Find method.
     * @param message is the commit's message that you want to find.*/
    void find(String message) {
        boolean check = false;
        List<String> commits2 = plainFilenamesIn(".gitlet/commits");
        for (String commit : commits2) {
            File filePath = new File(".gitlet/commits/" + commit);
            Commit physicalCommit = readObject(filePath, Commit.class);
            if (physicalCommit.returnMessage().equals(message)) {
                check = true;
                System.out.println(physicalCommit.returnSHAId());
            }
        }
        if (!check) {
            System.out.println("Found no commit with that message.");
        }
    }

    /**Status method.*/
    void status() {
        System.out.println("=== Branches ===");
        Object[] branches = _branches.keySet().toArray();
        Arrays.sort(branches);
        for (Object branch : branches) {
            if (branch.toString().equals(currentBranch)) {
                System.out.println("*" + branch);
            } else {
                System.out.println(branch);
            }
        }
        System.out.println();
        System.out.println("=== Staged Files ===");
        Object[] stagedFiles = _stagingArea.keySet().toArray();
        Arrays.sort(stagedFiles);
        for (Object stagedFile : stagedFiles) {
            System.out.println(stagedFile);
        }
        System.out.println();
        System.out.println("=== Removed Files ===");
        ArrayList<String> removedFiles = _removedFiles;
        Collections.sort(removedFiles);
        for (String removedFile : removedFiles) {
            System.out.println(removedFile);
        }
        System.out.println();
        System.out.println("=== Modifications Not Staged For Commit ===");
        System.out.println();
        System.out.println("=== Untracked Files ===");
        System.out.println();
    }

    /**Checkout method.
     * @param arguments is a string array*/
    void checkout(String[] arguments) {
        if (arguments.length == 2) {
            if (!arguments[0].equals("--")) {
                System.out.println("Incorrect operands.");
            } else {
                String fileName = arguments[1];
                File headPath = new File(".gitlet/commits/" + _head);
                Commit headCommit = readObject(headPath, Commit.class);
                HashMap<String, String> headSA = headCommit.returnStagingArea();
                if (!headSA.containsKey(fileName)) {
                    System.out.println("File does not exist in that commit.");
                } else {
                    String blobID = headSA.get(fileName);
                    File blobPath = new File(".gitlet/blobs/" + blobID);
                    String blob = readObject(blobPath, String.class);
                    File thisFile = new File(fileName);
                    Utils.writeContents(thisFile, blob);
                }
            }
        } else if (arguments.length == 3) {
            if (!arguments[1].equals("--")) {
                System.out.println("Incorrect operands.");
            } else {
                String commitID = arguments[0];
                String fileName = arguments[2];
                List<String> commits3 = plainFilenamesIn(".gitlet/commits");
                boolean checkForCommit = false;
                for (String commit : commits3) {
                    if (commit.equals(commitID) || commit.contains(commitID)) {
                        checkForCommit = true;
                        File filePath = new File(".gitlet/commits/" + commit);
                        Commit thisCommit = readObject(filePath, Commit.class);
                        HashMap<String, String> headSA = thisCommit.
                                returnStagingArea();
                        if (!headSA.containsKey(fileName)) {
                            System.out.println("File does not "
                                    + "exist in that commit.");
                        } else {
                            String blobID = headSA.get(fileName);
                            File blobPath = new File(".gitlet/blobs/" + blobID);
                            String blob = readObject(blobPath, String.class);
                            File thisFile = new File(fileName);
                            Utils.writeContents(thisFile, blob);
                        }
                    }
                }
                if (!checkForCommit) {
                    System.out.println("No commit with that id exists.");
                }
            }
        } else if (arguments.length == 1) {
            String branchName = arguments[0];
            checkoutHelper(branchName);
        }
    }

    /**Checkout helper function.
     * @param branchName given*/
    private void checkoutHelper(String branchName) {
        if (!_branches.containsKey(branchName)) {
            System.out.println("No such branch exists.");
        } else if (branchName.equals(currentBranch)) {
            System.out.println("No need to checkout the current branch..");
        } else {
            String branchHeadSHA = _branches.get(branchName);
            File givenCommitPath = new File(".gitlet/commits/"
                    + branchHeadSHA);
            Commit givenComm = readObject(givenCommitPath, Commit.class);
            File currentHeadPath = new File(".gitlet/commits/" + _head);
            Commit currHead = readObject(currentHeadPath, Commit.class);
            HashMap<String, String> commSA = givenComm.returnStagingArea();
            for (String fileName : commSA.keySet()) {
                String blobID = commSA.get(fileName);
                File blobPath = new File(".gitlet/blobs/" + blobID);
                String blob = readObject(blobPath, String.class);
                File thisFile = new File(fileName);
                Utils.writeContents(thisFile, blob);
            }
            for (String fileName : currHead.returnStagingArea().keySet()) {
                if (!givenComm.returnStagingArea().containsKey(fileName)) {
                    File deletedFile = new File(fileName);
                    restrictedDelete(deletedFile);
                }
            }
            if (!currentBranch.equals(branchName)) {
                _stagingArea.clear();
            }
            currentBranch = branchName;
            _head = branchHeadSHA;
        }
    }


    /**Branch method.
     * @param branchName new branch name*/
    void branch(String branchName) {
        if (_branches.containsKey(branchName)) {
            System.out.println("A branch with that name already exists.");
        } else {
            _branches.put(branchName, _head);
        }
    }

    /**Remove branch method.
     * @param branchName branch to be removed*/
    void rmBranch(String branchName) {
        if (!_branches.containsKey(branchName)) {
            System.out.println("A branch with that name does not exist.");
        } else if (currentBranch.equals(branchName)) {
            System.out.println("Cannot remove the current branch.");
        } else {
            _branches.remove(branchName);
        }
    }

    /**Reset method.
     * @param commitID commit to reset to.*/
    void reset(String commitID) {
        String givenCommitName = "none";
        List<String> commits4 = plainFilenamesIn(".gitlet/commits");
        for (String commit : commits4) {
            if (commit.equals(commitID) || commit.startsWith(commitID)) {
                givenCommitName = commit;
            }
        }
        if (givenCommitName.equals("none")) {
            System.out.println("No commit with that id exists.");
        } else {
            File givenCommitPath = new File(".gitlet/commits/"
                    + givenCommitName);
            Commit givenCommit = readObject(givenCommitPath, Commit.class);
            File currentCommit = new File(".gitlet/commits/" + _head);
            Commit current = readObject(currentCommit, Commit.class);
            boolean untrackedCheck = false;
            if (untrackedCheck) {
                System.out.println("There is an untracked file in the way;"
                        + " delete it or add it first.");
            } else {
                for (String file : givenCommit.returnStagingArea().keySet()) {
                    String[] checkoutArgs = new String[3];
                    checkoutArgs[0] = givenCommit.returnSHAId();
                    checkoutArgs[1] = "--";
                    checkoutArgs[2] = file;
                    checkout(checkoutArgs);
                }
                for (String file : current.returnStagingArea().keySet()) {
                    if (!givenCommit.returnStagingArea().containsKey(file)) {
                        rm(file);
                    }
                }
                for (String file : _stagingArea.keySet()) {
                    if (!givenCommit.returnStagingArea().containsKey(file)) {
                        rm(file);
                    }
                }
                _branches.put(currentBranch, givenCommit.returnSHAId());
                _stagingArea.clear();
            }
        }
    }

    /**Merge method.
     * @param givenBranch name*/
    void merge(String givenBranch) {
        if (!_stagingArea.isEmpty() || !_removedFiles.isEmpty()) {
            System.out.println("You have uncommitted changes.");
        } else if (!_branches.containsKey(givenBranch)) {
            System.out.println("A branch with that name doesn't exist.");
        } else if (givenBranch.equals(currentBranch)) {
            System.out.println("Cannot merge a branch with itself.");
        } else {
            String splitPointID = "nothing";
            Commit spc;
            ArrayList<String> givenBranchAncestors = new ArrayList<String>();
            String commitSHAid = _branches.get(givenBranch);
            File commitPath = new File(".gitlet/commits/" + commitSHAid);
            Commit comm = readObject(commitPath, Commit.class);
            givenBranchAncestors.add(comm.returnSHAId());
            while (!comm.returnIsFirst()) {
                String commParSHA = comm.returnParent();
                if (!comm.returnParent().equals(comm.returnSecondParent())) {
                    commParSHA = comm.returnSecondParent();
                }
                givenBranchAncestors.add(commParSHA);
                File nextCommPath = new File(".gitlet/commits/" + commParSHA);
                comm = readObject(nextCommPath, Commit.class);
            }
            ArrayList<String> currentBranchAncestors = new ArrayList<String>();
            String commitSHAid2 = _branches.get(currentBranch);
            File commitPath2 = new File(".gitlet/commits/" + commitSHAid2);
            Commit commit2 = readObject(commitPath2, Commit.class);
            currentBranchAncestors.add(commit2.returnSHAId());
            while (!commit2.returnIsFirst()) {
                String commParSHA2 = comm.returnParent();
                if (!comm.returnParent().equals(comm.returnSecondParent())) {
                    commParSHA2 = comm.returnSecondParent();
                }
                currentBranchAncestors.add(commParSHA2);
                File nextCommPth2 = new File(".gitlet/commits/" + commParSHA2);
                commit2 = readObject(nextCommPth2, Commit.class);
            }
            outer: for (String ancestor : givenBranchAncestors) {
                for (String currAncestor : currentBranchAncestors) {
                    if (ancestor.equals(currAncestor)) {
                        splitPointID = ancestor;
                        break outer;
                    }
                }
            }
            File splitPointPath = new File(".gitlet/commits/"
                    + splitPointID);
            spc = readObject(splitPointPath, Commit.class);
            if (splitPointID.equals(_branches.get(givenBranch))) {
                System.out.println("Given branch is an "
                        + "ancestor of the current branch.");
            } else if (splitPointID.equals(_branches.get(currentBranch))) {
                _branches.put(currentBranch, _branches.get(givenBranch));
                System.out.println("Current branch fast-forwarded.");
            } else {
                mergeHelper2(givenBranch, spc);
            }
        }
    }

    /**Merge helper function.
     * @param gbc commit
     * @param cbc commit
     * @param spc commit
     * @param file string*/
    void mergeHelper(Commit gbc, Commit spc, Commit cbc, String file) {
        String gbBlobSHA = gbc.returnStagingArea().get(file);
        String spBlobSHA = spc.returnStagingArea().get(file);
        if (!gbBlobSHA.equals(spBlobSHA)) {
            File newBlobPath = new File(".gitlet/blobs/"
                    + gbBlobSHA);
            String newBlob = readObject(newBlobPath, String.class);
            File theFile = new File(file);
            writeContents(theFile, newBlob);
            _stagingArea.put(file, gbBlobSHA);
        } else if ((!gbc.returnStagingArea().containsKey(file)
                && !cbc.returnStagingArea().containsKey(file))
                && spc.returnStagingArea().containsKey(file)) {
            File checkedFile = new File(file);
        } else if (!spc.returnStagingArea().containsKey(file)
                && !gbc.returnStagingArea().containsKey(file)
                && cbc.returnStagingArea().containsKey(file)) {
            _stagingArea.put(file, cbc.returnStagingArea().
                    get(file));
        } else if (!spc.returnStagingArea().containsKey(file)
                && gbc.returnStagingArea().containsKey(file)
                && !cbc.returnStagingArea().containsKey(file)) {
            File newBlobPath = new File(".gitlet/blobs/"
                    + gbBlobSHA);
            String newBlob = readObject(newBlobPath, String.class);
            File theFile = new File(file);
            writeContents(theFile, newBlob);
            _stagingArea.put(file, gbBlobSHA);
        } else if (spc.returnStagingArea().containsKey(file)
                && spc.returnStagingArea().get(file).equals(
                        cbc.returnStagingArea().get(file))
                && !gbc.returnStagingArea().containsKey(file)) {
            _stagingArea.remove(file);
            _removedFiles.add(file);
        } else if (spc.returnStagingArea().containsKey(file)
                && spc.returnStagingArea().get(file).equals(
                gbc.returnStagingArea().get(file))
                && !cbc.returnStagingArea().containsKey(file)) {
            _stagingArea.remove(file);
        } else {
            String beginning = "<<<<<<< HEAD";
            String currBlob = cbc.returnStagingArea().get(file);
            String middle = "=======";
            String givenBlob = gbc.returnStagingArea().get(file);
            String end = ">>>>>>>";
            String[] updatedContents =
                    new String[] {beginning, currBlob, middle, givenBlob, end};
            File filePath = new File(file);
            writeContents(filePath, (Object) updatedContents);
            System.out.println("Encountered a merge conflict");
        }
    }

    /**Second merge helper function.
     * @param givenBranch string
     * @param spc Commit*/
    void mergeHelper2(String givenBranch, Commit spc) {
        String gbcID = _branches.get(givenBranch);
        File gbcPath = new File(".gitlet/commits/"
                + gbcID);
        Commit gbc = readObject(gbcPath, Commit.class);
        ArrayList<String> givenBranchFiles =
                new ArrayList<String>(gbc.returnStagingArea().keySet());
        String cbcID = _branches.get(currentBranch);
        File cbcPath = new File(".gitlet/commits/" + cbcID);
        Commit cbc = readObject(cbcPath, Commit.class);
        new ArrayList<String>(cbc.returnStagingArea().keySet());
        new ArrayList<String>(spc.returnStagingArea().keySet());
        for (String file : givenBranchFiles) {
            mergeHelper(gbc, spc, cbc, file);
        }
        gbc.changeMessage("Merged" + givenBranch + "into"
                + currentBranch + ".");
        gbc.changeSecondParent(cbc.returnParent());
    }
}
