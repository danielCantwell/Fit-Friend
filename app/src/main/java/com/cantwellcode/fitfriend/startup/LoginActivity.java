package com.cantwellcode.fitfriend.startup;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cantwellcode.fitfriend.R;
import com.parse.FunctionCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.HashMap;

public class LoginActivity extends Activity {

    private EditText usernameField;
    private EditText passwordField;

    private Button loginButton;
    private Button signupButton;

    private ProgressDialog progressDialog;

    private String anonId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameField = (EditText) findViewById(R.id.username);
        passwordField = (EditText) findViewById(R.id.password);

        loginButton = (Button) findViewById(R.id.login);
        signupButton = (Button) findViewById(R.id.signup);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLoginClick();
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSignUpClick();
            }
        });

        anonId = ParseUser.getCurrentUser().getObjectId();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getActionBar().setDisplayHomeAsUpEnabled(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void onLoginClick() {
        String username = usernameField.getText().toString().trim();
        String password = passwordField.getText().toString();

        if (username.length() == 0) {
            showToast(R.string.error_blank_username);
        } else if (password.length() == 0) {
            showToast(R.string.error_blank_password);
        } else {
            progressDialog = ProgressDialog.show(this, null, "Logging in as " + username, true, false);

            ParseUser.logInInBackground(username, password, new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {

                    if (user != null) {
                        progressDialog.dismiss();
                        // Associate the device with a user
                        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
                        installation.put("user", user);
                        installation.saveInBackground();
                        loginSuccess();
                    } else {
                        progressDialog.dismiss();
                        if (e != null) {
                            if (e.getCode() == ParseException.OBJECT_NOT_FOUND) {
                                showToast(R.string.error_login_fail_invalid_credentials);
                                passwordField.selectAll();
                                passwordField.requestFocus();
                            } else {
                                showToast(R.string.error_login_fail_unknown);
                            }
                        }
                    }
                }
            });
        }
    }

    private void onSignUpClick() {
        String username = usernameField.getText().toString().trim();
        String password = passwordField.getText().toString();

        if (username.length() == 0) {
            showToast(R.string.error_blank_username);
        } else if (password.length() == 0) {
            showToast(R.string.error_blank_password);
        } else {

            Bundle args = new Bundle();
            args.putString("email", username);
            args.putString("password", password);

            Intent i = new Intent(this, SignUpActivity.class);
            i.putExtra("args", args);

            startActivity(i);
        }
    }

    private void loginSuccess() {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("anonId", anonId);
        ParseCloud.callFunctionInBackground("deleteAnonUser", params);

        // Save the current Installation to Parse. (Used for push notifications)
        // Associate the device with a user
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        installation.put("user", ParseUser.getCurrentUser());
        installation.saveInBackground();

        Toast.makeText(this, "Successfully Logged In", Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
    }

    private void showToast(int resource) {
        String msg = getResources().getString(resource);
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
