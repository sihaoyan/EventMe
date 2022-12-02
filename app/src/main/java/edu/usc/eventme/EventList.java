package edu.usc.eventme;
import java.io.Serializable;
import java.lang.reflect.Array;

import java.util.Comparator;
import java.lang.reflect.Array;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class EventList implements Serializable {
    private ArrayList<Event> eventList;
    private String sorting;
    private String keyWord;

    private double currentlat;
    private double currentlon;
    public EventList() {
        eventList=new ArrayList<Event>();
    };

    public ArrayList<Event> getEventList() {
        return eventList;
    }

    public void sort(String sort){
        switch (sort) {
            case "cost":
                Collections.sort(eventList, new sortByCost());
                break;
            case "distance":
                Collections.sort(eventList, new sortByDistance());
                break;
            case "date":
                Collections.sort(eventList, new sortByDate());
                break;
            case "alphabet":
                Collections.sort(eventList, new sortByAlpha());
                break;
            case "trending":
                Collections.sort(eventList, new sortByUser());
                break;
            case "rating":
                Collections.sort(eventList, new sortByRate());
                break;
            default:
                Collections.sort(eventList, new sortByCost());
                break;
        }
    }

    public void setCurrentlat(double lat){currentlat=lat;}
    public void setCurrentlon(double lon){currentlon=lon;}

    public void sortbydistoloc(double lat, double lon){
        currentlon=lon;
        currentlat=lat;
        Collections.sort(eventList, new sortByDistance());
    }


    public double getCurrentlat() {
        return currentlat;
    }

    public double getCurrentlon() {
        return currentlon;
    }
    public void addEvent(Event e){
        eventList.add(e);
    }

    public void removeEvent(Event e){
        for (int i = 0; i < eventList.size(); i++) {
            if (eventList.get(i).getId().equals(e.getId())) {
                eventList.remove(eventList.get(i));
                return;
            }
        }

    }

    public Event getEvent(String n){
        for(Event e:eventList){
            if(e.getId().equals(n)){
                return e;
            }
        }

        return null;
    }

    public ArrayList<Event> getList(){
        return eventList;
    }


    class sortByCost implements Comparator<Event>{
        public int compare(Event a, Event b){
            if(a.getEst_price()>b.getEst_price()){
                return 1;
            }
            else if(a.getEst_price()<b.getEst_price()){
                return -1;
            }
            else{
                return 0;
            }
        }
    }

    //need to calculate distance first
    class sortByDistance implements Comparator<Event>{
        public int compare(Event a, Event b){
            if(a.findDis(currentlat, currentlon)>b.findDis(currentlat, currentlon)){
                return 1;
            }
        else if(a.findDis(currentlat, currentlon)<b.findDis(currentlat, currentlon)){
                return -1;
            }
            else{
                return 0;
            }
        }
    }

    class sortByDate implements Comparator<Event>{
        public int compare(Event a, Event b){
            if(a.getStartDate().compareTo(b.getStartDate())>0){
                return 1;
            }
            else if(a.getStartDate().compareTo(b.getStartDate())<0){
                return -1;
            }
            else{
                if(a.getStartTime().compareTo(b.getStartTime())>0){
                    return 1;
                }
                else if(a.getStartTime().compareTo(b.getStartTime())<0){
                    return -1;
                }
                else{
                    return 0;
                }
            }
        }
    }

    class sortByAlpha implements Comparator<Event>{
        public int compare(Event a, Event b){
            if(a.getEventTitle().compareTo(b.getEventTitle())>0){
                return 1;
            }
            else if(a.getEventTitle().compareTo(b.getEventTitle())<0){
                return -1;
            }
            else{
                return 0;
            }
        }
    }

    class sortByRate implements Comparator<Event>{
        public int compare(Event a, Event b){
            if(a.getRating() < b.getRating()){
                return 1;
            }
            else if(a.getRating() > b.getRating()){
                return -1;
            }
            else{
                return 0;
            }
        }
    }

    class sortByUser implements Comparator<Event> {
        public int compare(Event a, Event b) {
            if (a.getNumUser() < b.getNumUser()) {
                return 1;
            } else if (a.getNumUser() > b.getNumUser()) {
                return -1;
            } else {
                return 0;
            }
        }
    }



}
