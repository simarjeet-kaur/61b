package gitlet;

import java.io.File;
import java.io.Serializable;
import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import static gitlet.Utils.readContentsAsString;

class Repo implements Serializable {

    Commit firstCommit;
    HashMap<String, String> _stagingAreaAdded;
    HashMap<String, String> _stagingAreaRemoved;
    String _head; //should be a serialized commit
    ArrayList<String> _trackedFiles;
    HashMap<String, String> _branches;
    File gitlet;
    File commits;
    File blobs;
    File staging_area;
    File added;
    File removed;
    File head;
    String firstCommitID;


    Repo() {
        //making the new .gitlet directory
        gitlet = new File(".gitlet");
        gitlet.mkdir();
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
        //new directory for head
        head = new File(".gitlet/head");
        head.mkdir();
        //(put a copy of the commit that is the head in here)

        //tracked filed kept track of by a list of file names
        _trackedFiles = new ArrayList<String>();
        //branches also a list of commits to keep track of the lists of SHA-id's of commits - should
        //be the name of the branch and it's corresponding head commit, so make it a hashmap
        //that make up a branch
        _branches = new HashMap<String, String>();

        //as a backup, instance variables for the staging area and head
        _stagingAreaAdded = new HashMap<String, String>();
        _stagingAreaRemoved = new HashMap<String, String>();
        _head = null;

    }

    void init() {

        //FIXME - make the initial commit
            //this needs to start automatically with an initial commit - use commit class
        LocalTime initialTime = new Time(0, 0, 0).toLocalTime();
        Date initialDate = new Date(1970, Calendar.JANUARY, 1);

        firstCommit = new Commit("initial commit", initialTime, initialDate, new ArrayList<byte[]>(), new ArrayList<String>(), true);

        //FIXME - put the serialized version of this commit in the commit directory, with the name of the file being it's sha-id
        firstCommitID = firstCommit.returnSHA_id();
        Utils.writeObject(Utils.join(commits, firstCommitID), firstCommit);
        //FIXME - make the branches just the master
            //FIXME - is this the right idea for the branches too?
        _branches.put("master", firstCommitID);
        //FIXME - make this commit the head
        // should it be the name? - yes because you can
        // acces it later by looking through all the commits in the commit folder and deserializing from there
        _head = firstCommitID;
    }

    void add(String fileName) {
        //check if the file exists
        File checking = new File(fileName);
        if (!checking.exists()) {
            throw new GitletException("File does not exist.");
        } else {
            //making a blob of this file, where blob is the fileContents
            String blob = Utils.readContentsAsString(checking); //fileContents
            byte[] serializedBlob = Utils.serialize(checking);
            String blobID = Utils.sha1(blob);
                //add serialized blob to the blob folder like it is done for commits
                Utils.writeObject(Utils.join(blobs, blobID), blob);
                //now if you want to grab this, you can call the blobID from the hashmap in the staging area
            //add the blob to the stagingArea hashmap and staging area folder for addition
            _stagingAreaAdded.put(fileName, blobID);
            Utils.writeObject(Utils.join(added, fileName), blob);
        }
    }

    void commit(String arg) {
    }

    void rm(String arg) {
    }

    void log() {

    }

    void globalLog() {
    }

    void find() {
    }

    void status() {
    }

    void checkout() {
    }

    void branch() {
    }

    void rmBranch() {
    }

    void reset() {
    }

    void merge() {
    }
}
