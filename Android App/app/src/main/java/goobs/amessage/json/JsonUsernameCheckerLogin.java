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

import goobs.amessage.R;

/**
 * Created by midni on 5/7/2016.
 */
public class JsonUsernameCheckerLogin extends AsyncTask<String, Void, String> {
    String serverURL;
    String json;
    String userid;
    JSONObject jsonObject;
    Activity act;
    Context context;

    public JsonUsernameCheckerLogin(Activity passed){
        act = passed;
        context=passed.getApplicationContext();
    }

    @Override
    protected void onPreExecute() {
        serverURL = "http://75.86.150.212:8080/thesis/AMessage/database/check_user_login.php";
        jsonObject = new JSONObject();
    }

    @Override
    protected String doInBackground(String... params) {
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
            response = jsonObject.getString("username");


            //close things up
            outputStream.close();
            inputStream.close();
            httpURLConnection.disconnect();

            userid = response;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return userid;
    }

    @Override
    protected void onPostExecute(String result) {

        if(result.equals("false")){
            Toast.makeText(context, "Already a user named that..", Toast.LENGTH_SHORT).show();
            act.findViewById(R.id.login_login_button).setVisibility(View.GONE);

        } else {
            act.findViewById(R.id.login_login_button).setVisibility(View.VISIBLE);

        }
    }
}