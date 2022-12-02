package edu.usc.eventme;

import android.net.Uri;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class User {
    private String userName;
    private String userEmail;
    private String userBirthday;
    private String userPhoto;
    private EventList userEventList;

    public User() {}

    public User(String userName, String userEmail, String userBirthday, String userPhoto) {
        this.userBirthday = userBirthday;
        this.userEmail = userEmail;
        this.userName = userName;
        this.userPhoto = userPhoto;
        userEventList = new EventList();
    }

    public String getUserName() {
        return userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserBirthday() {
        return userBirthday;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public EventList getUserEventList() {
        return userEventList;
    }

    public boolean checkConflict(Event event) {
        ArrayList<Event> currList = userEventList.getEventList();

        String startTime = event.getStartTime();
        String endTime = event.getEndTime();
        String startDate = event.getStartDate();
        String endDate = event.getEndDate();
        String tempStartTime;
        String tempEndTime;
        String tempStartDate;
        String tempEndDate;

        //startDate = LocalDate.parse(event.getStartDate());
        //endDate = LocalDate.parse(event.getEndDate());
        String eventID= event.getId();

        for(int i = 0; i < currList.size(); i++) {
            Event currEvent = currList.get(i);
            if (!eventID.equals(currEvent.getId())) { //events besides the event
                tempStartDate = currEvent.getStartDate();
                tempEndDate = currEvent.getEndDate();
                if (startDate.compareTo(tempEndDate) <= 0) {
                    if (endDate.compareTo(tempStartDate) >= 0) {
                        //fix bug, change event to current event
                        tempStartTime = currEvent.getStartTime();
                        tempEndTime = currEvent.getEndTime();
                        if (startTime.compareTo(tempEndTime) <= 0 && endTime.compareTo(tempStartTime) >= 0) {
                            return true;
                        }
                    }
                }
            }
        }


        return false;
    }

    public boolean eventExist(String eventID) {
        ArrayList<Event> currList = userEventList.getEventList();
        for (int i = 0; i < currList.size(); i++) {
            if (eventID.equals(currList.get(i).getId())) {
                return true;
            }
        }
        return false;
    }

    public boolean registerEvent(Event event) {
        String tempID = event.getId();
        if (eventExist(tempID)) {
            return false;
        }
        userEventList.addEvent(event);
        return true;
    }

    public boolean removeEvent(Event event) {
        String tempID = event.getId();
        if (!eventExist(tempID)) {
            return false;
        }
        userEventList.removeEvent(event);
        return true;
    }
}
