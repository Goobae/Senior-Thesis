package goobs.amessage.helpers;

import java.util.Date;

/**
 * Created by midni on 3/22/2016.
 */
public class message {
    public String message;
    public String date;
    public int isUser;

    public message(String message, String date, int isUser) {
        super();
        this.message = message;
        this.date = date;
        this.isUser = isUser;
    }
}
