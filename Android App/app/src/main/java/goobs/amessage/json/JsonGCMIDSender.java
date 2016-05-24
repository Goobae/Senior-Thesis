package goobs.amessage.json;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by midni on 4/5/2016.
 */
public class JsonGCMIDSender extends AsyncTask<String, Void, Void> {
    String serverURL;
    String json;
    JSONObject jsonObject;
    @Override
    protected void onPreExecute() {
        serverURL = "http://75.86.150.212:8080/thesis/AMessage/add_gcmid.php";
        jsonObject = new JSONObject();
    }

    @Override
    protected Void doInBackground(String... params) {
        try {

            jsonObject.put("gcmid", params[0]);
            jsonObject.put("user", params[1]);
            json = jsonObject.toString();

            Log.d("THE STRING ", json);

            //Setup Connection and create an output stream writer
            URL url = new URL(serverURL);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();

            //send the sender
            outputStream.write(json.getBytes("UTF-8"));

            //close things up
            outputStream.close();
            InputStreamReader inputStream = new InputStreamReader(httpURLConnection.getInputStream());
            String line, response = "";
            BufferedReader reader = new BufferedReader(inputStream);
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null)
            {
                sb.append(line + "\n");
            }
            // Response from server after login process will be stored in response variable.
            response = sb.toString();
            Log.d("Server response is :", response);
            inputStream.close();
            httpURLConnection.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
