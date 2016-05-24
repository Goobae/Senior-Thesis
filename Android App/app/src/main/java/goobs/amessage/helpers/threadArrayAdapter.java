package goobs.amessage.helpers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
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
public class threadArrayAdapter extends ArrayAdapter<thread> {
    private TextView threadText;
    private TextView threadName;
    private List<thread> threadList = new ArrayList<thread>();
    private LinearLayout layout;

    public threadArrayAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);}

    public void add(thread object) {
        threadList.add(object);
        super.add(object);
    }
    public int getCount()
    {
        return this.threadList.size();
    }

    public thread getItem(int index){

        return this.threadList.get(index);
    }

    public View getView(int position,View ConvertView, ViewGroup parent){

        View v = ConvertView;
        if(v==null){

            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v =inflater.inflate(R.layout.thread, parent,false);

        }

        layout = (LinearLayout)v.findViewById(R.id.threadLinearLayout);
        thread messageobj = getItem(position);
        threadText =(TextView)v.findViewById(R.id.threadMessage);
        threadText.setText(messageobj.message);
        threadName = (TextView)v.findViewById(R.id.threadParticipants);
        threadName.setText(messageobj.participant);

        if(messageobj.isNotified){
            layout.setBackgroundColor(1);
            Log.d("HERE", "here");
        }

        layout.setGravity(Gravity.LEFT);
        return v;
    }

    public Bitmap decodeToBitmap(byte[] decodedByte) {
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }
}
