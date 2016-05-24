package goobs.amessage.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import goobs.amessage.R;
import goobs.amessage.helpers.message;
import goobs.amessage.helpers.messageArrayAdapter;
import goobs.amessage.json.JsonGetMessages;
import goobs.amessage.json.JsonMessageSender;

public class Thread extends AppCompatActivity {
    ListView listView;
    EditText messageEditText;
    Button send;
    messageArrayAdapter messageArrayAdapter;


    int thread;
    int user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = this.getSharedPreferences("goobs.amessage", Context.MODE_PRIVATE);
        user = prefs.getInt("amessageuserID",0);

        Log.d("THREAD UER", Integer.toString(user));

        thread =  getIntent().getIntExtra("threadid", 0);
        String name = getIntent().getStringExtra("name");
        boolean isSend = getIntent().getBooleanExtra("isSend", false);
        String message = getIntent().getStringExtra("message");

        setContentView(R.layout.activity_thread);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if(name ==null){
            name = "aMessage";
        }
        toolbar.setTitle(name);
        setSupportActionBar(toolbar);

        listView = (ListView)findViewById(R.id.thread_threads_listview);
        messageArrayAdapter = new messageArrayAdapter(this, R.layout.message);
        messageEditText = (EditText)findViewById(R.id.thread_message_editText);
        send = (Button)findViewById(R.id.thread_send);

        JsonGetMessages getMessages = new JsonGetMessages(this, listView, messageArrayAdapter);
        getMessages.execute(thread);


        if(isSend){
            sendChatMessage(message);
            DateFormat df = new SimpleDateFormat("MM/dd HH:mm");
            Date d = new Date();
            goobs.amessage.helpers.message temp = new message(message, d.toString(), 1);
            messageEditText.setText("");

            messageArrayAdapter.add(temp);

        }

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateFormat df = new SimpleDateFormat("MM/dd HH:mm");
                Date d = new Date();
                goobs.amessage.helpers.message temp = new message(messageEditText.getText().toString(), d.toString(), 1);
                sendChatMessage(messageEditText.getText().toString());
                messageEditText.setText("");

                messageArrayAdapter.add(temp);
            }
        });


    }


    @Override
    public void onBackPressed() {
        finish();
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void sendChatMessage(String message){
        JsonMessageSender messageSender = new JsonMessageSender();
        messageSender.execute( Integer.toString(user), message, Integer.toString(thread));
    }

}
