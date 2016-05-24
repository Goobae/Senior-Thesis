package goobs.amessage.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import goobs.amessage.R;
import goobs.amessage.json.JsonLogin;
import goobs.amessage.json.JsonSignUp;
import goobs.amessage.json.JsonUsernameCheckerLogin;

public class Login extends AppCompatActivity {
    EditText username;
    EditText password;
    EditText email;
    EditText phone;
    EditText username1;
    TextView signedUp;
    Button login;

    boolean isSignedUp = false;
    boolean isSecondClick=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = this.getSharedPreferences("goobs.amessage", Context.MODE_PRIVATE);
        boolean isLoggedIn = prefs.getBoolean("amessageLoggedIn", false);


        if (!isLoggedIn) {

            setContentView(R.layout.activity_login);


            //set data
            username = (EditText) findViewById(R.id.login_name_edittext);
            password = (EditText) findViewById(R.id.login_password_edittext);
            username1 =(EditText) findViewById(R.id.login_username_edittext);
            email = (EditText) findViewById(R.id.login_email_edittext);
            phone = (EditText) findViewById(R.id.login_number_edittext);
            login = (Button) findViewById(R.id.login_login_button);
            signedUp = (TextView) findViewById(R.id.login_signedUp_textview);


            signedUp.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    if(!isSecondClick) {
                        username.setVisibility(View.INVISIBLE);
                        username1.setVisibility(View.INVISIBLE);
                        phone.setVisibility(View.INVISIBLE);
                    } else{
                        username.setVisibility(View.VISIBLE);
                        username1.setVisibility(View.VISIBLE);
                        phone.setVisibility(View.VISIBLE);
                    }
                    isSecondClick=!isSecondClick;
                    isSignedUp = !isSignedUp;
                }
            });

            username1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(!hasFocus){
                        JsonUsernameCheckerLogin jsonUsernameCheckerLogin = new JsonUsernameCheckerLogin(Login.this);
                        jsonUsernameCheckerLogin.execute(username1.getText().toString());
                    }
                }
            });

            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        sendInfo();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (TimeoutException e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            Intent main = new Intent(this, MainActivity.class);
            this.finish();
            startActivity(main);
        }
    }

    private void sendInfo() throws ExecutionException, InterruptedException, TimeoutException {

        if (isSignedUp) {
            if (!password.getText().toString().equals("") && !email.getText().toString().equals("")) {
                String pass = password.getText().toString();
                String emai = email.getText().toString();

                JsonLogin jsonLogin = new JsonLogin(this);
                jsonLogin.execute(emai, pass);
            }
        } else {
            if (!username.getText().toString().equals("") && !password.getText().toString().equals("") && !email.getText().toString().equals("") && !phone.getText().toString().equals("") && !username1.getText().toString().equals("")) {
                String name = username.getText().toString();
                String pass = password.getText().toString();
                String emai = email.getText().toString();
                String phon = phone.getText().toString();
                String un = username1.getText().toString();

                SharedPreferences prefs = this.getSharedPreferences("goobs.amessage", Context.MODE_PRIVATE);
                prefs.edit().putString("amessageusername", name).apply();

                //send info to server to put into db and get the user id
                JsonSignUp jsonSignUp = new JsonSignUp(this);
                jsonSignUp.execute(name, pass, emai, phon, un);
            }
        }
    }
}
