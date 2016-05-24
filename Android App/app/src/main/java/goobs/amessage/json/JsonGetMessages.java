package goobs.amessage.json;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import goobs.amessage.R;

import goobs.amessage.helpers.message;
import goobs.amessage.helpers.messageArrayAdapter;
import goobs.amessage.helpers.thread;
import goobs.amessage.helpers.threadArrayAdapter;

/**
 * Created by midni on 5/5/2016.
 */
public class JsonGetMessages extends AsyncTask<Integer, Void, JSONArray> {


    String serverURL;
    String json;
    JSONObject jsonObject;
    String jsonSendName;

    messageArrayAdapter messageArrayAdapter;
    ListView listView;
    JSONArray returned;
    int user;

    private Context context;
    private View view;

    public JsonGetMessages(Context context, ListView list, messageArrayAdapter mess) {
        this.context = context;
        messageArrayAdapter = mess;
        listView = list;

    }

    @Override
    protected void onPreExecute() {
        serverURL = "http://75.86.150.212:8080/thesis/amessage/database/get_messages.php";
        jsonObject = new JSONObject();

    }

    @Override
    protected JSONArray doInBackground(Integer... params) {
        try {
            user = params[0];

            //Get the messages corresponding from the sender
            jsonObject.put("thread", params[0]);
            jsonSendName = jsonObject.toString();

            //Setup Connection and create an output stream writer
            URL url = new URL(serverURL);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();

            //send the sender
            outputStream.write(jsonSendName.getBytes("UTF-8"));


            //Get the response
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            StringBuilder sb = new StringBuilder();
            String line = null;

            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line + "\n");
            }
            json = sb.toString();


            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("serverResponse");


            inputStream.close();
            httpURLConnection.disconnect();

            returned= jsonArray;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return returned;
    }

    @Override
    public void onPostExecute(JSONArray result) {
        super.onPostExecute(result);

        SharedPreferences prefs = context.getSharedPreferences("goobs.amessage", Context.MODE_PRIVATE);
        int userId = prefs.getInt("amessageuserID",0);

        int whichSide;



        for (int i = 0; i < result.length(); i++) {
            JSONObject oneObj = null;
            try {
                oneObj = result.getJSONObject(i);
                if(oneObj.getInt("userID")== userId)
                    whichSide=1;
                else
                    whichSide=0;
                message temp = new message( oneObj.getString("message"), oneObj.getString("date"), whichSide);
                messageArrayAdapter.add(temp);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        listView.setAdapter(messageArrayAdapter);
    }
}
