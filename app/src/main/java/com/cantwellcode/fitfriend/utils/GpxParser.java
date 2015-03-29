package com.cantwellcode.fitfriend.utils;

import android.util.Log;
import android.util.Xml;

import com.cantwellcode.fitfriend.exercise.types.Cardio;
import com.google.android.gms.maps.model.LatLng;
import com.parse.ParseException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by danielCantwell on 3/29/15.
 */
public class GpxParser {

    public List getTrackPoints(Cardio c) {
        XmlPullParserFactory pullParserFactory;
        try {
            pullParserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = pullParserFactory.newPullParser();

            InputStream inputStream = new ByteArrayInputStream(c.getGPX().getData());
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(inputStream, null);

            return parseXML(parser);

        } catch (XmlPullParserException e) {

            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private List parseXML(XmlPullParser parser) throws XmlPullParserException, IOException {
        ArrayList<LatLng> points = null;
        int eventType = parser.getEventType();
        LatLng point = null;

        Log.d("GPX", "Parsing XML");

        while (eventType != XmlPullParser.END_DOCUMENT){
            String name = null;
            switch (eventType){
                case XmlPullParser.START_DOCUMENT:
                    points = new ArrayList();
                    break;
                case XmlPullParser.START_TAG:
                    name = parser.getName();
                    if (name.equalsIgnoreCase("trkpt")){
                        point = new LatLng(Double.valueOf(parser.getAttributeValue(null, "lat")), Double.valueOf(parser.getAttributeValue(null, "lon")));
                    }
                    break;
                case XmlPullParser.END_TAG:
                    name = parser.getName();
                    if (name.equalsIgnoreCase("trkpt") && point != null){
                        points.add(point);
                    }
            }
            eventType = parser.next();
        }

        return points;
    }
}
