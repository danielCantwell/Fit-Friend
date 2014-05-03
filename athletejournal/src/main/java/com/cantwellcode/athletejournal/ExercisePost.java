package com.cantwellcode.athletejournal;

import android.graphics.Bitmap;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel on 4/27/2014.
 */
public class ExercisePost extends Post {

    private String type;
    private Bitmap icon;
    private String intensity;
    private String dateOf;
    private String timeOf;
    private String duration;
    private String location;
    private String distance;

    private List<User> attending;

    /**
     * ExercisePost Constructor without the lists
     *
     * @param author
     * @param group
     * @param dateTime
     * @param content
     * @param options
     * @param type
     * @param intensity
     * @param dateOf
     * @param timeOf
     * @param duration
     * @param location
     * @param distance
     */
    public ExercisePost(User author, String group, String dateTime, String content, Bundle options, String type, String intensity, String dateOf, String timeOf, String duration, String location, String distance) {
        super(author, group, dateTime, content, options);
        this.type = type;
        this.intensity = intensity;
        this.dateOf = dateOf;
        this.timeOf = timeOf;
        this.duration = duration;
        this.location = location;
        this.distance = distance;

        attending = new ArrayList<User>();
        attending.add(author);

        if (type.equals("Swim")) {
            //this.icon = swimIcon;
        }
        else if (type.equals("Bike")) {
            //this.icon = bikeIcon;
        }
        else if (type.equals("Run")) {
            //this.icon = runIcon;
        }
        else if (type.equals("Gym")) {
            //this.icon = gymIcon;
        }
    }

    /**
     * ExercisePost constructor with the lists
     *
     * @param author
     * @param group
     * @param dateTime
     * @param content
     * @param highFives
     * @param discussion
     * @param options
     * @param type
     * @param intensity
     * @param dateOf
     * @param timeOf
     * @param duration
     * @param location
     * @param distance
     * @param attending
     */
    public ExercisePost(User author, String group, String dateTime, String content, List<User> highFives, List<Comment> discussion, Bundle options, String type, String intensity, String dateOf, String timeOf, String duration, String location, String distance, List<User> attending) {
        super(author, group, dateTime, content, highFives, discussion, options);
        this.type = type;
        this.intensity = intensity;
        this.dateOf = dateOf;
        this.timeOf = timeOf;
        this.duration = duration;
        this.location = location;
        this.distance = distance;
        this.attending = attending;

        if (type.equals("Swim")) {
            //this.icon = swimIcon;
        }
        else if (type.equals("Bike")) {
            //this.icon = bikeIcon;
        }
        else if (type.equals("Run")) {
            //this.icon = runIcon;
        }
        else if (type.equals("Gym")) {
            //this.icon = gymIcon;
        }
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Bitmap getIcon() {
        return icon;
    }

    public void setIcon(Bitmap icon) {
        this.icon = icon;
    }

    public String getIntensity() {
        return intensity;
    }

    public void setIntensity(String intensity) {
        this.intensity = intensity;
    }

    public String getDateOf() {
        return dateOf;
    }

    public void setDateOf(String dateOf) {
        this.dateOf = dateOf;
    }

    public String getTimeOf() {
        return timeOf;
    }

    public void setTimeOf(String timeOf) {
        this.timeOf = timeOf;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public List<User> getAttending() {
        return attending;
    }

    public void setAttending(List<User> attending) {
        this.attending = attending;
    }

    public int getAttendingCount() {
        return attending.size();
    }

    public void addAttendee(User user) {
        attending.add(user);
    }
}
