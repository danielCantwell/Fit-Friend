package com.cantwellcode.athletejournal;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by Daniel on 5/20/2014.
 */
public class GPX extends File {

    private NodeList nodeList;
    private List<Item> items;

    private float totalSeconds;
    private float totalMiles;
    private float maxSpeed;
    private int avgHR;
    private int maxHR;
    private int avgCadence;
    private int maxCadence;
    private int elevationGain;

    public GPX(File dir, String name) {
        super(dir, name);
        setup();
    }

    public GPX(String path) {
        super(path);
        setup();
    }

    public GPX(String dirPath, String name) {
        super(dirPath, name);
        setup();
    }

    public GPX(URI uri) {
        super(uri);
        setup();
    }

    private void setup() {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(this);
            doc.getDocumentElement().normalize();

            /* create node list of each trackpoint in the gpx file */
            nodeList = doc.getElementsByTagName("trkpt");
            items = new ArrayList<Item>();

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                Item item = new Item();

                if (node.getNodeType() == Node.ELEMENT_NODE) {

                    Element element = (Element) node;
                    item.lat = element.getAttribute("lat");
                    item.lon = element.getAttribute("lon");
                    item.ele = element.getElementsByTagName("ele").item(0).getTextContent();
                    item.time = element.getElementsByTagName("time").item(0).getTextContent();
                }

                items.add(item);
            }

//            processItems();

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    private void processItems() {
//        totalMiles = findTotalMiles();
//    }
//
//    private float findTotalMiles() {
//
//    }

    private double distance(double lat1, double lon1, double lat2, double lon2, char unit) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        if (unit == 'K') { // kilometers, else leave as miles
            dist = dist * 1.609344;
        }
        return (dist);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::  This function converts decimal degrees to radians             :*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::  This function converts radians to decimal degrees             :*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    private class Item {
        String lat;
        String lon;
        String time;
        String ele;
//        String hr;
    }
}
