package goobs.amessage.json;

/**
 * Created by midni on 3/29/2016.
 */
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import goobs.amessage.activities.MainActivity;
import goobs.amessage.gcm.GcmGetRegId;
import goobs.amessage.helpers.message;


/**
 * Created by midni on 3/3/2016.
 */
public class JsonSignUp extends AsyncTask<String, Void, String> {
    String serverURL;
    String json;
    String userid;
    JSONObject jsonObject;
    Context context;
    Activity act;

    public JsonSignUp(Activity passed){
        context=passed.getApplicationContext();
        act = passed;
    }

    @Override
    protected void onPreExecute() {
        serverURL = "http://75.86.150.212:8080/thesis/AMessage/database/add_user.php";
        jsonObject = new JSONObject();
    }

    @Override
    protected String doInBackground(String... params) {
        try {

            jsonObject.put("name", params[0]);
            jsonObject.put("password", params[1]);
            jsonObject.put("email", params[2]);
            jsonObject.put("phone", params[3]);
            jsonObject.put("username", params[4]);


            json = jsonObject.toString();


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



            JSONObject jsonObject =new JSONObject(json);
            response = jsonObject.getString("id");


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
    protected void onPostExecute(String userID) {
        SharedPreferences prefs = context.getSharedPreferences("goobs.amessage",Context.MODE_PRIVATE);
        prefs.edit().putInt("amessageuserID", Integer.parseInt(userID)).apply();
        prefs.edit().putBoolean("amessageLoggedIn", true).apply();
        GcmGetRegId gcmGetRegId = new GcmGetRegId(context);
        gcmGetRegId.execute(userID);

        Toast.makeText(context, "Please wait. Logging In...", Toast.LENGTH_SHORT).show();

        Intent main = new Intent(context, MainActivity.class);
        act.finish();
        act.startActivity(main);
    }
}
