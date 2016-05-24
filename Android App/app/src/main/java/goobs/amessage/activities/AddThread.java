package goobs.amessage.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import goobs.amessage.R;
import goobs.amessage.json.JsonAddThread;
import goobs.amessage.json.JsonUsernameCheckerAddThread;

public class AddThread extends AppCompatActivity {

    EditText contact;
    EditText message;
    Button send;

    String nameOfThread;
    String recipiantID;
    String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_thread);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("New Message");
        setSupportActionBar(toolbar);

        SharedPreferences prefs = this.getSharedPreferences("goobs.amessage", Context.MODE_PRIVATE);
        userId = Integer.toString(prefs.getInt("amessageuserID", 0));



        contact = (EditText)findViewById(R.id.add_thread_contact);
        message = (EditText)findViewById(R.id.add_thread_message_editText);
        send = (Button)findViewById(R.id.add_thread_send);

        contact.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                 @Override
                 public void onFocusChange(View v, boolean hasFocus) {
                     if(!contact.getText().toString().isEmpty() & !hasFocus)
                     {
                         JsonUsernameCheckerAddThread jsonUsernameCheckerAddThread = new JsonUsernameCheckerAddThread(AddThread.this);
                         jsonUsernameCheckerAddThread.execute(contact.getText().toString());
                         try {

                             ArrayList<String> objects;
                             objects= jsonUsernameCheckerAddThread.get();

                             nameOfThread = objects.get(0);
                             recipiantID=objects.get(1);

                         } catch (InterruptedException e) {
                             e.printStackTrace();
                         } catch (ExecutionException e) {
                             e.printStackTrace();
                         }
                     }
                 }
             }

        );

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!contact.getText().toString().isEmpty() && !message.getText().toString().isEmpty()) {

                    JsonAddThread addThread = new JsonAddThread(AddThread.this);
                    addThread.execute(userId,recipiantID, message.getText().toString(),nameOfThread);


                }
            }
        });
    }
}
