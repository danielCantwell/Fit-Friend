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
import com.parse.GetCallback;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUpActivity extends Activity {

    private String email;
    private String password;

    private EditText usernameField;
    private EditText passwordField;
    private EditText confirmPasswordField;
    private EditText nameField;

    private Button signupButton;

    private static final int minPasswordLength = 6;

    private ProgressDialog progressDialog;

    private ParseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        usernameField = (EditText) findViewById(R.id.username);
        passwordField = (EditText) findViewById(R.id.password);
        confirmPasswordField = (EditText) findViewById(R.id.password_confirm);
        nameField = (EditText) findViewById(R.id.name);

        signupButton = (Button) findViewById(R.id.signup);
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSignUpClick();
            }
        });

        Bundle args = getIntent().getBundleExtra("args");
        email = args.getString("email");
        password = args.getString("password");

        usernameField.setText(email);
        passwordField.setText(password);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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

    public void onSignUpClick() {
        String username = usernameField.getText().toString();
        String password = passwordField.getText().toString();
        String passwordAgain = confirmPasswordField.getText().toString();
        String email = username;
        String name = nameField.getText().toString();

        if (username.length() == 0) {
            showToast(R.string.error_blank_username);
        } else if (password.length() == 0) {
            showToast(R.string.error_blank_password);
        } else if (password.length() < minPasswordLength) {
            showToast(R.string.error_invalid_password);
        } else if (passwordAgain.length() == 0) {
            showToast(R.string.error_mismatched_passwords);
            confirmPasswordField.requestFocus();
        } else if (!password.equals(passwordAgain)) {
            showToast(R.string.error_mismatched_passwords);
            confirmPasswordField.selectAll();
            confirmPasswordField.requestFocus();
        } else if (name.length() == 0) {
            showToast(R.string.error_blank_name);
            nameField.requestFocus();
        } else {

            progressDialog = ProgressDialog.show(this, null, "Signing Up " + name, true, false);

            user = ParseUser.getCurrentUser();

            // Set standard fields
            user.setUsername(username);
            user.setPassword(password);
            user.setEmail(email);
            user.put("name", name);

            user.signUpInBackground(new SignUpCallback() {

                @Override
                public void done(ParseException e) {

                    if (e == null) {
                        progressDialog.dismiss();
                        signupSuccess();
                    } else {
                        progressDialog.dismiss();
                        switch (e.getCode()) {
                            case ParseException.INVALID_EMAIL_ADDRESS:
                                showToast(R.string.error_invalid_email);
                                break;
                            case ParseException.USERNAME_TAKEN:
                                showToast(R.string.error_username_taken);
                                break;
                            case ParseException.EMAIL_TAKEN:
                                showToast(R.string.error_username_taken);
                                break;
                            default:
                                showToast(R.string.error_signup_fail_unknown);
                                Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                break;
                        }
                    }
                }
            });
        }
    }

    private void signupSuccess() {
        Toast.makeText(this, "Sign Up Success", Toast.LENGTH_SHORT).show();

        // Add Cantwell Code (Fit-Friend) as a friend
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("email", "danielcantwell@cantwellcode.com");
        query.setLimit(1);
        query.getFirstInBackground(new GetCallback<ParseUser>() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                ParseObject friend = new ParseObject("Friend");
                friend.put("confirmed", true);
                friend.put("from", ParseUser.getCurrentUser());
                friend.put("to", parseUser);
                friend.saveInBackground();
            }
        });

        // Save the current Installation to Parse. (Used for push notifications)
        // Associate the device with a user
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        installation.put("user", ParseUser.getCurrentUser());
        installation.saveInBackground();

        Intent i = new Intent(this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    private void showToast(int resource) {
        String msg = getResources().getString(resource);
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
