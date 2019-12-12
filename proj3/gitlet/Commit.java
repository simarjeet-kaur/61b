package gitlet;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**Commit class that represent the history of the
 * edits at that point in the project.
 * @author Simarjeet Kaur*/
public class Commit implements Serializable {

    /**Commit message.*/
    private String _message;
    /**Commit date.*/
    private Date _date;
    /**If the commit is the first or not.*/
    private boolean _isFirst;
    /**Commit SHA-id.*/
    private String shaID;
    /**Commit first parent.*/
    private String _primaryParent;
    /**Commit second parent (merges).*/
    private String _secondaryParent;
    /**Commit staging area.*/
    private HashMap<String, String> _stagingArea;

    /**Commit class.
     * @param message string message
     * @param date date and time of the commit
     * @param isFirst boolean if it is the first commit
     * @param parent parent SHA id of the commit
     * @param stagingArea hash map history of the rest of the project */
    Commit(String message, Date date, HashMap<String, String> stagingArea,
           String parent, boolean isFirst) {
        _message = message;
        _date = date;
        _isFirst = isFirst;
        _primaryParent = parent;
        _secondaryParent = parent;
        _stagingArea = stagingArea;
        List<Object> shaList = new ArrayList<Object>(_stagingArea.values());
        shaList.add(_primaryParent);
        shaList.add(_secondaryParent);
        shaList.add(_message);
        shaList.add(_date.toString());
        shaID = gitlet.Utils.sha1(shaList);
    }

    /**Returns the message of the commit.*/
    String returnMessage() {
        return _message;
    }

    /**Returns the date of this commit as a string.*/
    String returnDate() {
        SimpleDateFormat formatter =
                new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy ZZZZ");
        return formatter.format(_date);
    }

    /**Returns a hashMap of all the blobs and file
     * names this commit takes care of.*/
    HashMap<String, String> returnStagingArea() {
        return _stagingArea;
    }

    /**Returns whether or not this is the first commit as a boolean.*/
    boolean returnIsFirst() {
        return _isFirst;
    }

    /**Returns the SHA-id of the commit.*/
    String returnSHAId() {
        return shaID;
    }

    /**Returns SHA-id of the primary parent commit.*/
    String returnParent() {
        return _primaryParent;
    }

    /**Returns SHA-id of the secondary parent commit.*/
    String returnSecondParent() {
        return _secondaryParent;
    }

    /**Changes the message of the commit.
     * @param s is the new message*/
    public void changeMessage(String s) {
        _message = s;
    }

    /**Changes the second parent of the commit.
     * @param s is the second parent*/
    public void changeSecondParent(String s) {
        _secondaryParent = s;
    }
}
