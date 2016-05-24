package goobs.amessage.activities;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;
import java.util.ArrayList;

import goobs.amessage.R;

import goobs.amessage.gcm.GcmBroadcastReceiver;
import goobs.amessage.gcm.GcmGetRegId;
import goobs.amessage.helpers.thread;
import goobs.amessage.helpers.threadArrayAdapter;
import goobs.amessage.json.JsonGCMIDSender;
import goobs.amessage.json.JsonGetThreads;

public class MainActivity extends AppCompatActivity {


    ListView listView;
    int userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), AddThread.class);
                startActivity(intent);
            }
        });



        SharedPreferences prefs = this.getSharedPreferences("goobs.amessage", Context.MODE_PRIVATE);
        userId = prefs.getInt("amessageuserID",0);

        Log.d("user: ", Integer.toString(userId));

        listView = (ListView)findViewById(R.id.mainactivity_threads_listview);

        JsonGetThreads getThreads = new JsonGetThreads(this,listView );
        getThreads.execute(userId);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                thread temp;
                temp = (thread)parent.getAdapter().getItem(position);

                Intent intent = new Intent(getBaseContext(), Thread.class);
                intent.putExtra("name", temp.participant);
                intent.putExtra("threadid", temp.userID);
                startActivity(intent);

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            SharedPreferences prefs = this.getSharedPreferences("goobs.amessage", Context.MODE_PRIVATE);
            prefs.edit().putBoolean("amessageLoggedIn", false).apply();

            Intent i = new Intent(this, Login.class );
            finish();
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

}
