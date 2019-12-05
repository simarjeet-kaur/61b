package gitlet;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Formatter;
//import java.util.text.SimpleDateFormat;

public class Commit implements Serializable {

    public gitlet.Utils Utils;
    private String _message;
    private Date _date;
    //private ArrayList<byte[]> _blobs;
    private boolean _isFirst;
    private String SHA_id;
    private String _primaryParent;
    private String _secondaryParent;
    private HashMap<String, String> _stagingArea;

    Commit(String message, Date date, HashMap<String, String> stagingArea, String parent, boolean isFirst) {

        //blob is the name of the file and the readContents of that file
        //sha-id of this is how you save the blob

        //hash map file name to its contents in commit
        //staging area hash map - file name to the SHA-id

        //extracting what's given from the commit
        _message = message;
        _date = date;
        _isFirst = isFirst;
        _primaryParent = parent; //(should be some sort of sha-id of the commit before it)
        _secondaryParent = parent;
            //secondary parent is the same until you need to change it when you merge
        //SHA-id list of blobs or something, them serialized - need them to be serialized into
        // byte arrays so you can use them in the init
        //_blobs = blobs;

        //files mapped to their blobs
        _stagingArea = stagingArea;
        //remember to serialize the blob before you put it in to the commits
        //Each commit is identified by its SHA-1 id, which must include the file (blob)
            // references of its files, parent reference, log message, and commit time.
        List<Object> _shaList = new ArrayList<Object>(_stagingArea.values());
        _shaList.add(_primaryParent);
        _shaList.add(_secondaryParent);
        _shaList.add(_message);
        _shaList.add(_date.toString());
        //making the SHA-id
        SHA_id = gitlet.Utils.sha1(_shaList);

        //hash map of the file name to the sha-id of the contents of the file
            //need this because when you save the blobs, you name them by the sha-id



    }

    /**Returns the message of the commit.*/
    String returnMessage() {
        return _message;
    }

    /**Returns the date of this commit as a string.*/
    String returnDate() {
//        String _condensedDate = _date.toString().substring(0, 20);
//        String _year = _date.toString().substring(24);
//        int _timeZone = _date.getTimezoneOffset();
        SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy");
        return formatter.format(_date) + " -0800";
        //System.out.println(formatter.format(_date));
        //return _condensedDate + _year + " -0800";
        //+ _timeZone;
    }

    /**Returns all of the blobs this commit takes care of.*/
    HashMap<String, String> returnStagingArea() {
        return _stagingArea;
    }

    /**Returns whether or not this is the first commit as a boolean.*/
    boolean returnIsFirst() {
        return _isFirst;
    }

    /**Returns the SHA-id of the commit.*/
    String returnSHA_id() {
        return SHA_id;
    }

    /**Returns SHA-id of the primary parent commit.*/
    String returnParent() {
        return _primaryParent;
    }

    /**Returns SHA-id of the secondary parent commit.*/
    String returnSecondParent() {
        return _secondaryParent;
    }



}
