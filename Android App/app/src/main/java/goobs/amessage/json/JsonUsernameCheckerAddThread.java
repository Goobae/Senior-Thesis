package goobs.amessage.json;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

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

/**
 * Created by midni on 5/7/2016.
 */
public class JsonUsernameCheckerAddThread extends AsyncTask<String, Void, ArrayList<String>> {
    String serverURL;
    String json;
    String userid;
    JSONObject jsonObject;
    Activity act;
    Context context;
    ArrayList<String> returned;

    public JsonUsernameCheckerAddThread(Activity passed){
        act = passed;
        context=passed.getApplicationContext();
    }

    @Override
    protected void onPreExecute() {
        serverURL = "http://75.86.150.212:8080/thesis/AMessage/database/check_user_add_thread.php";
        jsonObject = new JSONObject();
        returned = new ArrayList<String>();
    }

    @Override
    protected ArrayList<String> doInBackground(String... params) {
        try {

            jsonObject.put("username", params[0]);


            json = jsonObject.toString();

            Log.d("input", json);

            //Setup Connection and create an output stream writer
            URL url = new URL(serverURL);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();

            //send the sender
            outputStream.write(json.getBytes("UTF-8"));



            //Get the response
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            StringBuilder sb = new StringBuilder();
            String line = null;
            String response = "";

            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line + "\n");
            }
            json = sb.toString();


            JSONObject jsonObject = new JSONObject(json);
            returned.add(jsonObject.getString("username"));
            returned.add(jsonObject.getString("userid"));



            //close things up
            outputStream.close();
            inputStream.close();
            httpURLConnection.disconnect();



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
    protected void onPostExecute(ArrayList<String> result) {

        if(result.get(0).equals("false")){
            Toast.makeText(context, "No user named that..", Toast.LENGTH_SHORT).show();
            act.findViewById(R.id.add_thread_send).setVisibility(View.GONE);

        } else {
            act.findViewById(R.id.add_thread_send).setVisibility(View.VISIBLE);

        }
    }
}