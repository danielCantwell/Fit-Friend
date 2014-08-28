//package com.cantwellcode.fitfriend.app.startup;
//
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.app.Dialog;
//import android.app.ProgressDialog;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v4.app.DialogFragment;
//import android.support.v4.app.FragmentActivity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import com.cantwellcode.fitfriend.app.R;
//import com.parse.GetCallback;
//import com.parse.LogInCallback;
//import com.parse.ParseException;
//import com.parse.ParseObject;
//import com.parse.ParseQuery;
//import com.parse.ParseUser;
//import com.parse.SignUpCallback;
//
///**
// * Created by Daniel on 5/3/2014.
// */
//public class LogInActivity extends FragmentActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.log_in_activity);
//
//        // Sign Up button click handler
//        ((Button) findViewById(R.id.signUp)).setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                DialogFragment signUpDialog = new SignUpDialog(LogInActivity.this);
//                signUpDialog.show(getSupportFragmentManager(), "SignUpDialog");
//            }
//        });
//
//        // Log In button click handler
//        ((Button) findViewById(R.id.logIn)).setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                DialogFragment logInDialog = new LogInDialog(LogInActivity.this);
//                logInDialog.show(getSupportFragmentManager(), "LogInDialog");
//            }
//        });
//
//        // Anonymous Log In button click handler
//        ((Button) findViewById(R.id.anonLogIn)).setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                // Log In Anonymously
//                // Start Main Activity
//            }
//        });
//    }
//
//    private class SignUpDialog extends DialogFragment {
//
//        EditText usernameView;
//        EditText passwordView;
//        EditText passwordAgainView;
//
//        Activity mActivity;
//
//        public SignUpDialog(Activity activity) {
//            mActivity = activity;
//        }
//
//        @Override
//        public Dialog onCreateDialog(Bundle savedInstanceState) {
//            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//            // Get the layout inflater
//            LayoutInflater inflater = getActivity().getLayoutInflater();
//
//            View view = inflater.inflate(R.layout.sign_up_dialog, null);
//
//            // Set up the signup form.
//            usernameView = (EditText) view.findViewById(R.id.username);
//            passwordView = (EditText) view.findViewById(R.id.password);
//            passwordAgainView = (EditText) view.findViewById(R.id.passwordConfirm);
//
//            builder.setTitle("Sign Up");
//
//            // Inflate and set the layout for the dialog
//            // Pass null as the parent view because its going in the dialog layout
//            builder.setView(view)
//                    // Add action buttons
//                    .setPositiveButton("Sign Up", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int id) {
//                                    // Validate the sign up data
//                                    boolean validationError = false;
//                                    StringBuilder validationErrorMessage =
//                                            new StringBuilder(getResources().getString(R.string.error_intro));
//                                    if (isEmpty(usernameView)) {
//                                        validationError = true;
//                                        validationErrorMessage.append(getResources().getString(R.string.error_blank_username));
//                                    }
//                                    if (isEmpty(passwordView)) {
//                                        if (validationError) {
//                                            validationErrorMessage.append(getResources().getString(R.string.error_join));
//                                        }
//                                        validationError = true;
//                                        validationErrorMessage.append(getResources().getString(R.string.error_blank_password));
//                                    }
//                                    if (!isMatching(passwordView, passwordAgainView)) {
//                                        if (validationError) {
//                                            validationErrorMessage.append(getResources().getString(R.string.error_join));
//                                        }
//                                        validationError = true;
//                                        validationErrorMessage.append(getResources().getString(
//                                                R.string.error_mismatched_passwords));
//                                    }
//                                    validationErrorMessage.append(getResources().getString(R.string.error_end));
//
//                                    // If there is a validation error, display the error
//                                    if (validationError) {
//                                        Toast.makeText(LogInActivity.this, validationErrorMessage.toString(), Toast.LENGTH_LONG)
//                                                .show();
//                                        return;
//                                    }
//
//                                    // Set up a progress dialog
//                                    final ProgressDialog dlg = new ProgressDialog(LogInActivity.this);
//                                    dlg.setTitle("Please wait.");
//                                    dlg.setMessage("Signing up.  Please wait.");
//                                    dlg.show();
//
//                                    // Set up a new Parse user
//                                    final ParseUser user = new ParseUser();
//                                    user.setUsername(usernameView.getText().toString());
//                                    user.setPassword(passwordView.getText().toString());
//                                    // Call the Parse signup method
//                                    user.signUpInBackground(new SignUpCallback() {
//
//                                        @Override
//                                        public void done(ParseException e) {
//                                            dlg.dismiss();
//                                            if (e != null) {
//                                                // Show the error message
//                                                Toast.makeText(LogInActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
//                                            } else {
//                                                // Add CANTWELL CODE as a friend
//                                                ParseQuery<ParseUser> query = ParseUser.getQuery();
//                                                query.whereEqualTo("username", "CantwellCode");
//                                                query.setLimit(1);
//                                                query.getFirstInBackground(new GetCallback<ParseUser>() {
//                                                    @Override
//                                                    public void done(ParseUser parseUser, ParseException e) {
//                                                        ParseObject friend = new ParseObject("Friend");
//                                                        friend.put("confirmed", true);
//                                                        friend.put("from", user);
//                                                        friend.put("to", parseUser);
//                                                        friend.saveInBackground();
//                                                    }
//                                                });
//                                                // Start an intent for the dispatch activity
//                                                Intent intent = new Intent(LogInActivity.this, DispatchActivity.class);
//                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                                mActivity.startActivity(intent);
//                                                mActivity.finish();
//                                            }
//                                        }
//                                    });
//                                }
//                            }
//                    )
//                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//                                    SignUpDialog.this.getDialog().cancel();
//                                }
//                            }
//                    );
//            return builder.create();
//        }
//    }
//
//    private class LogInDialog extends DialogFragment {
//
//        EditText usernameView;
//        EditText passwordView;
//
//        Activity mActivity;
//
//        public LogInDialog(Activity activity) {
//            mActivity = activity;
//        }
//
//        @Override
//        public Dialog onCreateDialog(Bundle savedInstanceState) {
//            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//            // Get the layout inflater
//            LayoutInflater inflater = getActivity().getLayoutInflater();
//
//            View view = inflater.inflate(R.layout.log_in_dialog, null);
//
//            // Set up the login form.
//            usernameView = (EditText) view.findViewById(R.id.username);
//            passwordView = (EditText) view.findViewById(R.id.password);
//
//            builder.setTitle("Log In");
//
//            // Inflate and set the layout for the dialog
//            // Pass null as the parent view because its going in the dialog layout
//            builder.setView(view)
//                    // Add action buttons
//                    .setPositiveButton("Log In", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int id) {
//                            // Validate the log in data
//                            boolean validationError = false;
//                            StringBuilder validationErrorMessage =
//                                    new StringBuilder(getResources().getString(R.string.error_intro));
//                            if (isEmpty(usernameView)) {
//                                validationError = true;
//                                validationErrorMessage.append(getResources().getString(R.string.error_blank_username));
//                            }
//                            if (isEmpty(passwordView)) {
//                                if (validationError) {
//                                    validationErrorMessage.append(getResources().getString(R.string.error_join));
//                                }
//                                validationError = true;
//                                validationErrorMessage.append(getResources().getString(R.string.error_blank_password));
//                            }
//                            validationErrorMessage.append(getResources().getString(R.string.error_end));
//
//                            // If there is a validation error, display the error
//                            if (validationError) {
//                                Toast.makeText(LogInActivity.this, validationErrorMessage.toString(), Toast.LENGTH_LONG)
//                                        .show();
//                                return;
//                            }
//
//                            // Set up a progress dialog
//                            final ProgressDialog dlg = new ProgressDialog(LogInActivity.this);
//                            dlg.setTitle("Please wait.");
//                            dlg.setMessage("Logging in.  Please wait.");
//                            dlg.show();
//                            // Call the Parse login method
//                            ParseUser.logInInBackground(usernameView.getText().toString(), passwordView.getText()
//                                    .toString(), new LogInCallback() {
//
//                                @Override
//                                public void done(ParseUser user, ParseException e) {
//                                    dlg.dismiss();
//                                    if (e != null) {
//                                        // Show the error message
//                                        Toast.makeText(LogInActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
//                                    } else {
//                                        // Start an intent for the dispatch activity
//                                        Intent intent = new Intent(LogInActivity.this, DispatchActivity.class);
//                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                        mActivity.startActivity(intent);
//                                        mActivity.finish();
//                                    }
//                                }
//                            });
//                        }
//                    })
//                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                            LogInDialog.this.getDialog().cancel();
//                        }
//                    });
//            return builder.create();
//        }
//
//    }
//
//    private boolean isEmpty(EditText etText) {
//        if (etText.getText().toString().trim().length() > 0) {
//            return false;
//        } else {
//            return true;
//        }
//    }
//
//    private boolean isMatching(EditText etText1, EditText etText2) {
//        if (etText1.getText().toString().equals(etText2.getText().toString())) {
//            return true;
//        } else {
//            return false;
//        }
//    }
//
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//    }
//}
