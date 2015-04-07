package com.cantwellcode.fitfriend.exercise.log;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.cantwellcode.fitfriend.R;
import com.cantwellcode.fitfriend.exercise.types.Cardio;
import com.cantwellcode.fitfriend.utils.GpxParser;
import com.cantwellcode.fitfriend.utils.SmallLabelTextView;
import com.cantwellcode.fitfriend.utils.Statics;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class CardioViewActivity extends Activity implements OnMapReadyCallback {

    private MapFragment mMapFragment;
    private Cardio mCardio;
    private List<LatLng> mGeoPoints;
    private PolylineOptions mPolylineOptions;

    private TextView mTime;
    private SmallLabelTextView mPace;
    private SmallLabelTextView mDistance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cardio_view);

        ParseQuery<Cardio> query = Cardio.getQuery();
        query.fromPin(Statics.PIN_WORKOUT_DETAILS);
        try {
            mCardio = query.getFirst();
            mCardio.unpin(Statics.PIN_WORKOUT_DETAILS);
            getActionBar().setTitle(mCardio.getDateString());
            getGeoPoints();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        mMapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mMapFragment.getMapAsync(this);

        mTime = (TextView) findViewById(R.id.time);
        mPace = (SmallLabelTextView) findViewById(R.id.pace);
        mDistance = (SmallLabelTextView) findViewById(R.id.distance);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cardio_view, menu);
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
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(final GoogleMap map) {
        updateUI(map);
    }

    private void getGeoPoints() {
        mGeoPoints = new GpxParser().getTrackPoints(mCardio);
    }

    private void updateUI(GoogleMap map) {

        mTime.setText(String.valueOf(mCardio.getTimeString()));
        mPace.setTextWithLabel(mCardio.getPaceString(), "/mi");
        mDistance.setTextWithLabel(mCardio.getMilesString(), " mi");

        createPolyLine();

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(mGeoPoints.get(0).latitude, mGeoPoints.get(0).longitude), 15));
        map.addPolyline(mPolylineOptions);
        map.addCircle(new CircleOptions()
                .center(mGeoPoints.get(0))
                .radius(20)
                .strokeColor(Color.WHITE)
                .fillColor(Color.GREEN));
        map.addCircle(new CircleOptions()
                .center(mGeoPoints.get(mGeoPoints.size() - 1))
                .radius(20)
                .strokeColor(Color.WHITE)
                .fillColor(Color.RED));
    }

    private void createPolyLine() {
        mPolylineOptions = new PolylineOptions().geodesic(true)
                .addAll(mGeoPoints);
    }
}
