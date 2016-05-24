package goobs.amessage.helpers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import goobs.amessage.R;

/**
 * Created by midni on 3/22/2016.
 */
public class messageArrayAdapter extends ArrayAdapter<message> {
    private TextView messageText;
    private TextView messageDate;
    private List<message> messageList = new ArrayList<message>();
    private LinearLayout layout;

    public messageArrayAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);}

    public void add(message object) {
        messageList.add(object);
        super.add(object);
    }

    public int getCount() {
        return this.messageList.size();
    }

    public message getItem(int index) {

        return this.messageList.get(index);
    }

    public View getView(int position, View ConvertView, ViewGroup parent) {

        View v = ConvertView;
        if (v == null) {

            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.message, parent, false);

        }

        layout = (LinearLayout) v.findViewById(R.id.messageLinearLayout);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.FILL_PARENT);
        message messageobj = getItem(position);
        messageText = (TextView) v.findViewById(R.id.message_message);
        messageText.setText(messageobj.message);
        messageDate = (TextView) v.findViewById(R.id.message_date);
        messageDate.setText(messageobj.date);


        if(messageobj.isUser ==0) {
            layout.setGravity(Gravity.LEFT);
            //layout.setBackgroundResource(R.drawable.recieved);
        }
        else
           layout.setGravity(Gravity.RIGHT);
        return v;
    }

    public Bitmap decodeToBitmap(byte[] decodedByte) {
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }

}

