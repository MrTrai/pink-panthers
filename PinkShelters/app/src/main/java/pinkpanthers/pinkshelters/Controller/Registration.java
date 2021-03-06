package pinkpanthers.pinkshelters.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import pinkpanthers.pinkshelters.Model.Account;
import pinkpanthers.pinkshelters.Model.DBI;
import pinkpanthers.pinkshelters.Model.Db;
import pinkpanthers.pinkshelters.Model.UniqueKeyError;
import pinkpanthers.pinkshelters.R;

/**
 * to create an activity that allows users to register their accounts
 */
public class Registration extends AppCompatActivity implements View.OnClickListener {
    private Spinner userTypes;
    private EditText name;
    private EditText email;
    private EditText username;
    private EditText password;
    private DBI db;

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        Toolbar toolbar = findViewById(R.id.titleBar);
        setSupportActionBar(toolbar);

        // set up the spinner (user type)
        List<String> legalUsers = Arrays.asList("", "Homeless", "Shelter Volunteer", "Admin");
        userTypes = findViewById(R.id.user_type_spinner);
        @SuppressWarnings("unchecked") ArrayAdapter<String> adapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item,
                legalUsers);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userTypes.setAdapter(adapter);

        //grabbing user inputs: name, email, username, password
        name = findViewById(R.id.accountUserName);
        email = findViewById(R.id.email);
        username = findViewById(R.id.username);
        password = findViewById(R.id.pw);

        db = new Db("pinkpanther", "PinkPantherReturns!");
    }

    /**
     * Register user with:
     * Name
     * Password
     * Email
     * User Types
     *
     * @param view the current view that the register button is on
     */
    public void registerButton(@SuppressWarnings("unused") View view) {
        Boolean noName;
        Boolean noUsername;
        Boolean noPass;
        Boolean noEmail;
        Boolean noType;
        String empty = "";

        Editable nameText = name.getText();
        String isValidName = nameText.toString();
        TextView missingName = findViewById(R.id.missingName);
        if (isValidName.equals(empty)) { //missing name
            missingName.setVisibility(View.VISIBLE);
            noName = false;
        } else {
            missingName.setVisibility(View.INVISIBLE);
            noName = true;
        }

        Editable emailText = email.getText();
        String isValidEmail = emailText.toString();
        isValidEmail = isValidEmail.toLowerCase();
        TextView missingEmail = findViewById(R.id.missingEmail);
        if (isValidEmail.equals(empty) || !isValidEmail.contains("@")) {
            //missing email or "@" sign
            missingEmail.setVisibility(View.VISIBLE);
            noEmail = false;
        } else {
            missingEmail.setVisibility(View.INVISIBLE);
            noEmail = true;
        }

        Editable usernameText = username.getText();
        String isValidUsername = usernameText.toString();
        isValidUsername = isValidUsername.toLowerCase();
        TextView missingUsername = findViewById(R.id.missingUsername);
        if (isValidUsername.length() < 6) { //username cannot be less than 6 characters
            missingUsername.setVisibility(View.VISIBLE);
            noUsername = false;
        } else {
            missingUsername.setVisibility(View.INVISIBLE);
            noUsername = true;
        }

        Editable passwordText = password.getText();
        String isValidPassword = passwordText.toString();
        TextView missingPassword = findViewById(R.id.missingPassword);
        if (isValidPassword.length() < 6) { //password cannot be less than 6 characters
            missingPassword.setVisibility(View.VISIBLE);
            noPass = false;
        } else {
            missingPassword.setVisibility(View.INVISIBLE);
            noPass = true;
        }

        String isValidType = (String) userTypes.getSelectedItem();
        TextView missingUserType = findViewById(R.id.missingUserType);
        if (isValidType.equals(empty)) { // missing user type
            missingUserType.setVisibility(View.VISIBLE);
            noType = false;
        } else {
            missingUserType.setVisibility(View.INVISIBLE);
            noType = true;
        }

        Boolean missingAnything = noName && noEmail && noPass && noUsername && noType;
        if (missingAnything) {

            try {

                Account newAccount = db.createAccount(isValidType,
                        isValidUsername,
                        isValidPassword,
                        isValidName,
                        isValidEmail);

                sendEmail(isValidEmail);
                db.logAction(newAccount, "account_created");
                Intent loginPageIntent = new Intent(this, LoginActivity.class);
                startActivity(loginPageIntent);
            } catch (UniqueKeyError e) {
                TextView duplicate = findViewById(R.id.duplicate);
                duplicate.setVisibility(View.VISIBLE);
            }
        }
    }

    private static String readStream(InputStream is) {
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            int i = is.read();
            while(i != -1) {
                bo.write(i);
                i = is.read();
            }
            return bo.toString();
        } catch (IOException e) {
            return "";
        }
    }
    private static void sendEmail(String email) {
        Log.i("Send email", "send email right heree");
        try {
            URL url = new URL("http://54.255.176.28:5000/user/"+email);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try {
                Log.d("",
                        "fetching url0");
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                Log.d("",
                        "fetching url1");
                readStream(in);
                Log.d("",
                        "fetching url2");
            } catch (Exception e) {
                Log.d("", e.getMessage()
                );
            }
            finally {
                urlConnection.disconnect();
            }
        } catch (Exception e){

        }
    }


    /**
     * Direct user to Welcome Page
     *
     * @param v the current view that handles this onClick method
     */
    public void onClick(View v) { //cancel button
        Intent welcomeIntent = new Intent(Registration.this,
                WelcomePageActivity.class);
        startActivity(welcomeIntent);
    }
}