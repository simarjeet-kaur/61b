package gitlet;

import java.io.File;
import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

class Repo {

    Commit firstCommit;
    HashMap<String, String> _stagingArea;
    String _head; //should be a serialized commit
    ArrayList<String> _trackedFiles;
    ArrayList<String> _branches;
    File gitlet;
    File commits;
    File staging_area;
    File added;
    File removed;
    File head;


    Repo() {
        //making the new .gitlet directory
        gitlet = new File(".gitlet");
        gitlet.mkdir();
        //knowing you want the .gitlet directory to have subdirectories of commits, staging area, and head, make subdirectories for these
        commits = new File(".gitlet/commits")
        commits.mkdir();
        //new directory for staging area
        staging_area = new File(".gitlet/staging_area");
        staging_area.mkdir();
        //seperate directories for the ones to add and remove
        added = new File (".gitlet/staging_area/added");
        added.mkdir();
        removed = new File (".gitlet/staging_area/removed");
        removed.mkdir();
        //new directory for head
        head = new File(".gitlet/head");
        head.mkdir();
        //(put a copy of the commit that is the head in here)

        //as a backup, instance variables for the staging area and head
        _stagingArea = new HashMap<String, String>();
        _head = null;
        //tracked filed kept track of by a list of file names
        _trackedFiles = new ArrayList<String>();
        //branches also a list of commits to keep track of the lists of SHA-id's of commits
        //that make up a branch
        _branches = new ArrayList<String>();
    }

    void init() {

        //FIXME - make the initial commit
            //this needs to start automatically with an initial commit - use commit class
        LocalTime initialTime = new Time(0, 0, 0).toLocalTime();
        Date initialDate = new Date(1970, 1, 1);
        firstCommit = new Commit("initial commit", initialTime, initialDate, null, true);
        //FIXME - make the branches just the master
        _branches.add("master");
        //FIXME - put the serialized version of this commit in the commit directory
        Utils.writeObject(Utils.join(firstCommit);
        //FIXME - make this commit the head



    }

    void add(String arg) {
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
