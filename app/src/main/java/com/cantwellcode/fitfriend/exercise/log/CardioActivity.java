package com.cantwellcode.fitfriend.exercise.log;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Environment;
import android.os.SystemClock;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import com.cantwellcode.fitfriend.R;
import com.cantwellcode.fitfriend.exercise.types.Cardio;
import com.cantwellcode.fitfriend.utils.Statics;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class CardioActivity extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    /* GPX Elements */
    private static final String GPX = "gpx";
    private static final String TRACK = "trk";
    private static final String TRACK_SEGMENT = "trkseg";
    private static final String TRACK_POINT = "trkpt";
    private static final String NAME = "name";
    private static final String ELEVATION = "ele";
    private static final String TIME = "time";

    private Element gpxElement;
    private Element trkElement;
    private Element trkSegElement;

    private Chronometer mChrono;
    private TextView mPace;
    private TextView mDistance;

    private Document mDocument;

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    private Location mCurrentLocation;
    private String mLastUpdateTime;

    private Button mButtonRun;

    private boolean bRunning;
    private boolean bLocated;

    private float mMeters;
    private int mSecondsPerMile;
    private long mTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cardio);

        buildGoogleApiClient();

        bRunning = false;
        bLocated = false;
        mLastUpdateTime = "";

        mChrono = (Chronometer) findViewById(R.id.totalTime);
        mButtonRun = (Button) findViewById(R.id.button_run);
        mPace = (TextView) findViewById(R.id.pace);
        mDistance = (TextView) findViewById(R.id.distance);

        mButtonRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleRunButtonClick();
            }
        });

        mChrono.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                mTime = SystemClock.elapsedRealtime() - chronometer.getBase();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.save, menu);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void handleRunButtonClick() {
        if (bLocated) {
            if (!bRunning) {
                bRunning = true;
                mButtonRun.setText("Running");
                setupGPX();
                startChrono();
            } else {
                bRunning = false;
                mButtonRun.setText("Start Running");
                closeGPX();
                stopChrono();
            }
        } else {
            Toast.makeText(this, "Enable GPS", Toast.LENGTH_SHORT).show();
        }
    }

    private void startChrono() {
        mChrono.setBase(SystemClock.elapsedRealtime());
        mChrono.start();
    }

    private void stopChrono() {
        mChrono.stop();
    }

    private void setupGPX() {
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            mDocument = documentBuilder.newDocument();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        gpxElement = mDocument.createElement(GPX);
        gpxElement.setAttribute("creator", "Fit-Friend");
        try {
            gpxElement.setAttribute("version", getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        mDocument.appendChild(gpxElement);

        trkElement = mDocument.createElement(TRACK);
        gpxElement.appendChild(trkElement);

        trkSegElement = mDocument.createElement(TRACK_SEGMENT);
        trkElement.appendChild(trkSegElement);

//        Element nameElement = mDocument.createElement(NAME);
//        mDocument.appendChild(nameElement);
//
//
//
//        nameElement.appendChild(mDocument.createTextNode(""))

    }

    private void addTrackPoint() {
        Log.d("GPX", "Adding Track Point");
        Element trackPoint = mDocument.createElement(TRACK_POINT);
        trackPoint.setAttribute("lat", String.valueOf(mCurrentLocation.getLatitude()));
        trackPoint.setAttribute("lon", String.valueOf(mCurrentLocation.getLongitude()));

        trkSegElement.appendChild(trackPoint);

        Element elevation = mDocument.createElement(ELEVATION);
        trackPoint.appendChild(elevation);
        elevation.appendChild(mDocument.createTextNode(String.valueOf(mCurrentLocation.getAltitude())));

        Element time = mDocument.createElement(TIME);
        trackPoint.appendChild(time);
        time.appendChild(mDocument.createTextNode(mLastUpdateTime));
    }

    private void closeGPX() {
        try {
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer();

            Properties properties = new Properties();
            properties.setProperty(OutputKeys.INDENT, "yes");
            properties.setProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            properties.setProperty(OutputKeys.METHOD, "xml");
            properties.setProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
//            properties.setProperty(OutputKeys.ENCODING, "string");

            transformer.setOutputProperties(properties);

            DOMSource domSource = new DOMSource(mDocument.getDocumentElement());
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            StreamResult result = new StreamResult(output);

            transformer.transform(domSource, result);

            ParseFile gpxFile = new ParseFile("cardio.gpx", output.toByteArray());
            Cardio cardio = new Cardio();
            cardio.setUser(ParseUser.getCurrentUser());
            cardio.setGPX(gpxFile);
            cardio.setDate(Calendar.getInstance().getTime());
            cardio.setPace(mSecondsPerMile);
            cardio.setDistance(mMeters);
            cardio.setTime(mTime);

            // Get the city name
            Geocoder gcd = new Geocoder(this, Locale.getDefault());
            List<Address> addresses;
            try {
                addresses = gcd.getFromLocation(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude(), 1);
                if (!addresses.isEmpty()) {
                    cardio.setName("Cardio in " + addresses.get(0).getLocality());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

//            cardio.pinInBackground(Statics.PIN_WORKOUT_LOG);
            cardio.saveEventually();


        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(3000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        createLocationRequest();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        stopLocationUpdates();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d("Location", "Connected");
        if (mCurrentLocation == null) {
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
            updateUI();
        }

        // If the user presses the start button before GoogleApiClient connects
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("Location", "Changed");
        Location oldLocation = mCurrentLocation;
        mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        if (!bLocated) {
            bLocated = true;
            findViewById(R.id.location_status).setBackgroundResource(R.drawable.ic_action_location_found);
        }
        if (bRunning) {
            addTrackPoint();
            updateValues(oldLocation, mCurrentLocation);
            updateUI();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        findViewById(R.id.location_status).setBackgroundResource(R.drawable.ic_action_location_off);
    }

    /**
     * Requests location updates from the FusedLocationApi.
     */
    protected void startLocationUpdates() {
        // The final argument to {@code requestLocationUpdates()} is a LocationListener
        // (http://developer.android.com/reference/com/google/android/gms/location/LocationListener.html).
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    /**
     * Removes location updates from the FusedLocationApi.
     */
    protected void stopLocationUpdates() {
        // It is a good practice to remove location requests when the activity is in a paused or
        // stopped state. Doing so helps battery performance and is especially
        // recommended in applications that request frequent location updates.

        // The final argument to {@code requestLocationUpdates()} is a LocationListener
        // (http://developer.android.com/reference/com/google/android/gms/location/LocationListener.html).
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    private void updateUI() {
        if (mMeters > 0)
            mDistance.setText(String.format("%.2f", mMeters / 1609.34));
        if (mSecondsPerMile > 0)
            mPace.setText(String.format("%d:%02d", mSecondsPerMile / 60, mSecondsPerMile % 60));
    }

    private void updateValues(Location l1, Location l2) {
        mMeters += l1.distanceTo(l2);

        if (mMeters > 0 && mTime > 0) {
            mSecondsPerMile = (int) (mTime * 1609.34) / (int)(mMeters * 1000);
        } else {
            mSecondsPerMile = 0;
        }
    }
}
