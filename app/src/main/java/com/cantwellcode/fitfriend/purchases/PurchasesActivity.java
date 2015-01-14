package com.cantwellcode.fitfriend.purchases;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.cantwellcode.fitfriend.R;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRole;
import com.parse.ParseUser;

public class PurchasesActivity extends Activity {

    // In App Billing
    IabHelper mHelper;

    // Does the user have the premium upgrade?
    boolean mIsAthlete = false;

    private String mAthletePrice;
    static final String SKU_ATHLETE = "athlete";

    private Button mUpgradeAthlete;

    private ParseUser user;

    static final String TAG = "Fit-Friend";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchases);

        user = ParseUser.getCurrentUser();

        mUpgradeAthlete = (Button) findViewById(R.id.upgradeAthlete);

        String a = "MIIBIjANBgkqhki";
        String b = "G9w0BAQEFAAOCAQ8AMIIBCgKCAQEAh+Z+TCnf";
        String c = "OZXzuQs1HmJpgzGlgzltHCP4RHPfpx";
        String d = "LBG4urknaog76upqstmW8bMfJ9qxV8bRpT9iNiDTZBmD2PclOuZX";
        String e = "AHTtihgdGnWVxBfuWM6VKWkYKtpFF+UasSyJmhkXFKMy9P2tre9Cj2m5";
        String f = "2uu8hT37BYGoX5C0kMECewKBbFgGH1MUjPIN1gaBS4oKLx/bSCT20677AF0Ko0PkHon1HVsiQ";
        String g = "TcgXa3eu7YhXJnTx9R5NjbTggyJcA/u1a8hnhAUvIOZSwiyIwJMszROlUS2lTc26cgmko/ID";
        String h = "oPZ2ZiWIoEjKQmXswsC7h6f7rJctASzjcYxa9RwRhLZt8sDBzQQIDAQAB";

        String base64EncodedPublicKey = a + b + c + d + e + f + g + h;

        mHelper = new IabHelper(this, base64EncodedPublicKey);

        // enable debug logging (for a production application, you should set this to false).
//        mHelper.enableDebugLogging(true);

        // Start setup. This is asynchronous and the specified listener
        // will be called once setup completes.
        Log.d(TAG, "Starting setup.");
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                Log.d(TAG, "Setup finished.");

                if (!result.isSuccess()) {
                    // Oh noes, there was a problem.
                    complain("Problem setting up in-app billing: " + result);
                    return;
                }

                // Have we been disposed of in the meantime? If so, quit.
                if (mHelper == null) return;

                // IAB is fully set up. Now, let's get an inventory of stuff we own.
                Log.d(TAG, "Setup successful. Querying inventory.");
                mHelper.queryInventoryAsync(mGotInventoryListener);
            }
        });

        mUpgradeAthlete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHelper.launchPurchaseFlow(PurchasesActivity.this, SKU_ATHLETE, 10001,
                        mPurchaseFinishedListener, user.getObjectId());
            }
        });
    }

    // Listener that's called when we finish querying the items and subscriptions we own
    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
            Log.d(TAG, "Query inventory finished.");

            // Have we been disposed of in the meantime? If so, quit.
            if (mHelper == null) return;

            // Is it a failure?
            if (result.isFailure()) {
                complain("Failed to query inventory: " + result);
                return;
            }

            Log.d(TAG, "Query inventory was successful.");

            /*
             * Check for items we own. Notice that for each purchase, we check
             * the developer payload to see if it's correct! See
             * verifyDeveloperPayload().
             */

            // Do we have the athlete upgrade?
            Purchase premiumPurchase = inventory.getPurchase(SKU_ATHLETE);
            mIsAthlete = (premiumPurchase != null && verifyDeveloperPayload(premiumPurchase));
            Log.d(TAG, "User is " + (mIsAthlete ? "PREMIUM" : "NOT PREMIUM"));

            updateUi();
            Log.d(TAG, "Initial inventory query finished; enabling main UI.");
        }
    };

    /** Verifies the developer payload of a purchase. */
    boolean verifyDeveloperPayload(Purchase p) {
        String payload = p.getDeveloperPayload();

        if (payload == user.getObjectId()) {
            return true;
        } else {
            return false;
        }

        /*
         * TODO: verify that the developer payload of the purchase is correct. It will be
         * the same one that you sent when initiating the purchase.
         *
         * WARNING: Locally generating a random string when starting a purchase and
         * verifying it here might seem like a good approach, but this will fail in the
         * case where the user purchases an item on one device and then uses your app on
         * a different device, because on the other device you will not have access to the
         * random string you originally generated.
         *
         * So a good developer payload has these characteristics:
         *
         * 1. If two different users purchase an item, the payload is different between them,
         *    so that one user's purchase can't be replayed to another user.
         *
         * 2. The payload must be such that you can verify it even when the app wasn't the
         *    one who initiated the purchase flow (so that items purchased by the user on
         *    one device work on other devices owned by the user).
         *
         * Using your own server to store and verify developer payloads across app
         * installations is recommended.
         */
    }

    // Callback for when a purchase is finished
    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            Log.d(TAG, "Purchase finished: " + result + ", purchase: " + purchase);

            // if we were disposed of in the meantime, quit.
            if (mHelper == null) return;

            if (result.isFailure()) {
                complain("Error purchasing: " + result);
                return;
            }
            if (!verifyDeveloperPayload(purchase)) {
                complain("Error purchasing. Authenticity verification failed.");
                return;
            }

            Log.d(TAG, "Purchase successful.");

            if (purchase.getSku().equals(SKU_ATHLETE)) {
                // bought the premium upgrade!
                Log.d(TAG, "Purchase is premium upgrade. Congratulating user.");
                alert("Thank you for upgrading to premium!");
                mIsAthlete = true;
                user.put("athlete", true);
                user.saveEventually();
                updateUi();
            }
        }
    };

    // updates UI to reflect model
    public void updateUi() {

        // "Upgrade" button is only usable if the user is not premium
        if (mIsAthlete) {
            mUpgradeAthlete.setText("Purchases 'Athlete' Upgrade");
            mUpgradeAthlete.setClickable(false);
        }
    }

    void complain(String message) {
        Log.e(TAG, "**** TrivialDrive Error: " + message);
        alert("Error: " + message);
    }

    void alert(String message) {
        AlertDialog.Builder bld = new AlertDialog.Builder(this);
        bld.setMessage(message);
        bld.setNeutralButton("OK", null);
        Log.d(TAG, "Showing alert dialog: " + message);
        bld.create().show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_purchases, menu);
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

    // We're being destroyed. It's important to dispose of the helper here!
    @Override
    public void onDestroy() {
        super.onDestroy();

        // very important:
        Log.d(TAG, "Destroying helper.");
        if (mHelper != null) {
            mHelper.dispose();
            mHelper = null;
        }
    }
}
