package goobs.amessage.json;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
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

import goobs.amessage.activities.MainActivity;
import goobs.amessage.gcm.GcmGetRegId;

/**
 * Created by midni on 5/5/2016.
 */
public class JsonLogin extends AsyncTask<String, Void, String> {
    String serverURL;
    String json;
    String userid;
    JSONObject jsonObject;
    Activity act;
    Context context;

    public JsonLogin(Activity passed){
        act = passed;
        context=passed.getApplicationContext();
    }

    @Override
    protected void onPreExecute() {
        serverURL = "http://75.86.150.212:8080/thesis/AMessage/database/log_in_user.php";
        jsonObject = new JSONObject();
    }

    @Override
    protected String doInBackground(String... params) {
        try {

            jsonObject.put("email", params[0]);
            jsonObject.put("password", params[1]);

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
            response = jsonObject.getString("userid");


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
            Toast.makeText(context,"Failed to log you in..", Toast.LENGTH_SHORT).show();
        } else {
            SharedPreferences prefs = context.getSharedPreferences("goobs.amessage", Context.MODE_PRIVATE);
            prefs.edit().putInt("amessageuserID", Integer.parseInt(result)).apply();
            Log.d("LOGGED IN UER", result);
            prefs.edit().putBoolean("amessageLoggedIn", true).apply();
            GcmGetRegId gcmGetRegId = new GcmGetRegId(context);
            gcmGetRegId.execute(result);

            Toast.makeText(context, "Please wait. Logging In...", Toast.LENGTH_SHORT).show();

            Intent main = new Intent(context, MainActivity.class);
            act.finish();
            act.startActivity(main);
        }
    }
}
