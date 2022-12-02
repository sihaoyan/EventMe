package edu.usc.eventme;
import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

@SuppressWarnings("serial")
public class Event implements Serializable {

    private String location;
    private String id;
    private String eventTitle;
    private String category;
    private String endDate;
    private String startDate;
    private String endTime;
    private String startTime;
    private int numUser;
    private String description;
    private String cost;
    private Boolean parking;
    private String sponsoringOrganization;
    private String photoURL;
    private double latitude;
    private double longitude;
    private double est_price;
    private double rating;

    //constructor for serialization
    public Event(){

    }

    public Event(String id,String eventTitle, String category, String endDate, String startDate, String endTime, String startTime, int numUser, String description, String cost, Boolean parking, String sponsoringOrganization, String photoURL, String location, double latitude, double longitude, double est_price, double rating) {
        this.id = id;
        this.eventTitle = eventTitle;
        this.category = category;
        this.endDate = endDate;
        this.startDate = startDate;
        this.endTime = endTime;
        this.startTime = startTime;
        this.numUser = numUser;
        this.description = description;
        this.cost = cost;
        this.parking = parking;
        this.sponsoringOrganization = sponsoringOrganization;
        this.photoURL = photoURL;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
        this.est_price = est_price;
        this.rating = rating;
    }

    //constructor for white box testing
    public Event(String id,String eventTitle, String category, String endDate, String startDate, String endTime, String startTime, int numUser, String description, String cost, Boolean parking, String sponsoringOrganization, String photoURL, String location) {
        this.id = id;
        this.eventTitle = eventTitle;
        this.category = category;
        this.endDate = endDate;
        this.startDate = startDate;
        this.endTime = endTime;
        this.startTime = startTime;
        this.numUser = numUser;
        this.description = description;
        this.cost = cost;
        this.parking = parking;
        this.sponsoringOrganization = sponsoringOrganization;
        this.photoURL = photoURL;
        this.location = location;
    }

    //constructor for white box testing
    public Event(String id,String eventTitle, String category, String endDate, String startDate, String endTime, String startTime, int numUser, String description, String cost, Boolean parking, String sponsoringOrganization, String photoURL, String location, double latitude, double longitude) {
        this.id = id;
        this.eventTitle = eventTitle;
        this.category = category;
        this.endDate = endDate;
        this.startDate = startDate;
        this.endTime = endTime;
        this.startTime = startTime;
        this.numUser = numUser;
        this.description = description;
        this.cost = cost;
        this.parking = parking;
        this.sponsoringOrganization = sponsoringOrganization;
        this.photoURL = photoURL;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public Boolean getParking() {
        return parking;
    }

    public int getNumUser() {
        return numUser;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getCost() {
        return cost;
    }

    public String getDescription() {
        return description;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public String getSponsoringOrganization() {
        return sponsoringOrganization;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getLocation(){return location;}

    public void addUser(){
        numUser++;
    }

    public void lessUser(){
        numUser--;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getEst_price() {
        return est_price;
    }

    public double getRating(){
        return rating;
    }

    public double findDis(double lat, double lon){
        double theta = lon - longitude;
        double dist = Math.sin(deg2rad(lat))
                * Math.sin(deg2rad(latitude))
                + Math.cos(deg2rad(lat))
                * Math.cos(deg2rad(latitude))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }
    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }
}
