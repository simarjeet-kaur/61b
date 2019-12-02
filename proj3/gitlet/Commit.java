package gitlet;

import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;

public class Commit {

    String _message;
    LocalTime _time;
    Date _date;
    ArrayList<String> _blobs;
    boolean _isFirst;

    Commit(String message, LocalTime time, Date date, ArrayList<String> blobs, boolean isFirst) {

        //extracting what's given from the commit
        _message = message;
        _time = time;
        _date = date;
        _isFirst = isFirst;
        //SHA-id list of blobs or something, them serialized
        ArrayList<String> _blobs = blobs;

        //new attributes constructed per commit
        String SHA_id = Utils.sha1(_message, _time, _date, _blobs);
    }

    String returnMessage() {
        return _message;
    }

    LocalTime returnTime() {
        return _time;
    }

    Date returnDate() {
        return _date;
    }

    ArrayList<String> returnBlobs() {
        return _blobs;
    }

    boolean returnIsFirst() {
        return _isFirst;
    }



}
