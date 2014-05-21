package com.cantwellcode.athletejournal;

import java.io.File;
import java.io.IOException;
import java.net.URI;
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

    private int totalSeconds;
    private int totalMiles;
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

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
