package goobs.amessage.json;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

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

import goobs.amessage.activities.*;

/**
 * Created by midni on 5/5/2016.
 */
public class JsonAddThread extends AsyncTask<String, Void, String> {


    String serverURL;
    String json;
    JSONObject jsonObject;
    String jsonSendName;

    String threadID;
    String message;
    String recipID;
    String userID;
    String threadName;

    Context context;
    Activity activity;

    public JsonAddThread(Activity act) {
        this.context = act.getApplicationContext();
        this.activity = act;
    }

    @Override
    protected void onPreExecute() {
        serverURL = "http://75.86.150.212:8080/thesis/amessage/database/add_thread.php";
        jsonObject = new JSONObject();

    }

    @Override
    protected String doInBackground(String... params) {
        try {

            userID = params[0];
            recipID = params[1];
            message = params[2];
            threadName = params[3];

            //Get the messages corresponding from the sender
            jsonObject.put("user1", userID);
            jsonObject.put("user2", recipID);
            jsonObject.put("message", message);
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

            Log.d("JSONADDTHREAD", json);
            JSONObject jsonObject = new JSONObject(json);
            threadID = jsonObject.getString("id");


            inputStream.close();
            httpURLConnection.disconnect();



        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return threadID;
    }

    @Override
    public void onPostExecute(String result) {
        super.onPostExecute(result);

        Intent intent = new Intent(context, goobs.amessage.activities.Thread.class);
        intent.putExtra("message", message);
        intent.putExtra("isSend", true);
        intent.putExtra("name", threadName);
        intent.putExtra("threadid", Integer.parseInt(threadID));
        activity.finish();
        activity.startActivity(intent);

    }
}
