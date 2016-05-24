package goobs.amessage.helpers;

import java.util.Date;

/**
 * Created by midni on 3/22/2016.
 */
public class thread {
    public String participant;
    public String message;
    public int userID;
    public String date;
    public boolean isNotified;

    public thread(String participant, String message, String date, int userID, boolean isNotified) {
        // TODO Auto-generated constructor stub
        super();
        this.message = message;
        this.participant = participant;
        this.userID = userID;
        this.date =date;
        this.isNotified = isNotified;

    }
}
