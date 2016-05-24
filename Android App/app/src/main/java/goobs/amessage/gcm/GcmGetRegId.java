package goobs.amessage.gcm;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.json.JSONObject;

import java.io.IOException;

import goobs.amessage.activities.MainActivity;
import goobs.amessage.json.JsonGCMIDSender;

/**
 * Created by midni on 4/28/2016.
 */
public class GcmGetRegId extends AsyncTask<String, Void, String> {

    GoogleCloudMessaging gcm;
    Context context;
    String regid;
    String userID;
    String SENDER_ID = "518113903216";

    public GcmGetRegId(Context passed){
        context =passed;
    }

    //gcm - will update databse with sender id when called


    public static final String PROPERTY_REG_ID = "registration_id";

    private String getRegistrationId(Context context) {

        final SharedPreferences prefs = getGcmPreferences(context);

        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.d("Registration not found.", "");
            return "";
        }

        return registrationId;
    }

    protected void onPreExecute() {

    }
    /**
     * Registers the application with GCM servers asynchronously.
     * <p>
     * Stores the registration ID and the app versionCode in the application's
     * shared preferences.
     */
            @Override
            protected String doInBackground(String... params) {
                String msg = "";
                userID = params[0];
                gcm = GoogleCloudMessaging.getInstance(context);
                regid = getRegistrationId(context);

                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    regid = gcm.register(SENDER_ID);
                    msg = "Device registered, registration ID=" + regid;

                    // You should send the registration ID to your server over HTTP, so it
                    // can use GCM/HTTP or CCS to send messages to your app.
                    sendRegistrationIdToBackend(context);

                    // For this demo: we don't need to send it because the device will send
                    // upstream messages to a server that echo back the message using the
                    // 'from' address in the message.

                    // Persist the regID - no need to register again.
                    storeRegistrationId(context, regid);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                //Log.d("message", msg);
            }






    private SharedPreferences getGcmPreferences(Context context) {
        // This sample app persists the registration ID in shared preferences, but
        // how you store the regID in your app is up to you.
        return context.getSharedPreferences("goobs.amessage",
                Context.MODE_PRIVATE);
    }

    private void sendRegistrationIdToBackend(Context context) {
        JsonGCMIDSender gcmidSender = new JsonGCMIDSender();
        gcmidSender.execute(regid, userID);
    }

    private void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getGcmPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.apply();
    }
}
