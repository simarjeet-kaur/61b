package gitlet;

import java.io.Serializable;
import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Commit implements Serializable {

    public gitlet.Utils Utils;
    private String _message;
    private LocalTime _time;
    private Date _date;
    private ArrayList<byte[]> _blobs;
    private boolean _isFirst;
    private String SHA_id;
    private ArrayList<String> _parents;

    Commit(String message, LocalTime time, Date date, ArrayList<byte[]> blobs, ArrayList<String> parents, boolean isFirst) {

        //blob is the name of the file and the readContents of that file
        //sha-id of this is how you save the blob

        //hash map file name to its contents in commit
        //staging area hash map - file name to the SHA-id

        //extracting what's given from the commit
        _message = message;
        _time = time;
        _date = date;
        _isFirst = isFirst;
        _parents = parents; //(should be some sort of sha-id of the commit before it)
        //SHA-id list of blobs or something, them serialized - need them to be serialized into
        // byte arrays so you can use them in the init
        _blobs = blobs;
        //remember to serialize the blob before you put it in to the commits
        //Each commit is identified by its SHA-1 id, which must include the file (blob)
            // references of its files, parent reference, log message, and commit time.
        List<Object> _shaList = new ArrayList<Object>(_blobs);
        _shaList.addAll(_parents);
        _shaList.add(_message);
        _shaList.add(_time.toString());
        //making the SHA-id
        SHA_id = gitlet.Utils.sha1(_shaList);

        //hash map of the file name to the sha-id of the contents of the file
            //need this because when you save the blobs, you name them by the sha-id
    }

    /**Returns the message of the commit.*/
    String returnMessage() {
        return _message;
    }

    /**Returns the local time of this commit as a string.*/
    String returnTime() {
        return _time.toString();
    }

    /**Returns the date of this commit as a string.*/
    String returnDate() {
        return _date.toString();
    }

    /**Returns all of the blobs this commit takes care of.*/
    ArrayList<byte[]> returnBlobs() {
        return _blobs;
    }

    /**Returns whether or not this is the first commit as a boolean.*/
    boolean returnIsFirst() {
        return _isFirst;
    }

    /**Returns the SHA-id of the commit.*/
    String returnSHA_id() {
        return SHA_id;
    }

    /**Returns SHA-id of the parent(s) commit(s).*/
    ArrayList<String> returnParents() {
        return _parents;
    }



}
