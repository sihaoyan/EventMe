package edu.usc.eventme;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class EventMeUserUnit {
    private User testUser;
    private EventList testList;
    private Event event0;
    private Event event1;

    @Before
    public void setup() {
        testList = new EventList();
        event0 = new Event("0", "title0", "category0", "2022-12-10", "2022-12-01", "13:00", "12:00",
                0, "description0", "cost0", true, "sponsor0", "url0", "loc0");
        event1 = new Event("1", "title1", "category1", "2022-12-27", "2022-12-20", "18:00", "17:00",
                1, "description1", "cost1", true, "sponsor1", "url1", "loc1");
        testUser = new User("test", "test@usc.edu", "09/05/2000", "test");
        testUser.getUserEventList().addEvent(event0);
        testUser.getUserEventList().addEvent(event1);
        testList.addEvent(event0);
        testList.addEvent(event1);
    }

    @Test
    //mainly check with event0
    public void testCheckConflict_conflict() {
        //only overlaps end date with same time range
        Event testEvent2 = new Event("2", "title2", "category2", "2022-12-15", "2022-12-10", "13:00", "12:00",
                2, "description2", "cost2", true, "sponsor2", "url2", "loc2");
        assertEquals("check conflict with event only overlaps end date with same time range", true, testUser.checkConflict(testEvent2));

        //only overlaps start date with same time range
        Event testEvent3 = new Event("3", "title3", "category3", "2022-12-02", "2022-11-10", "13:00", "12:00",
                3, "description3", "cost3", true, "sponsor3", "url3", "loc3");
        assertEquals("check conflict with event only overlaps start date with same time range", true, testUser.checkConflict(testEvent3));

        //covers date range with same time range
        Event testEvent4 = new Event("4", "title4", "category4", "2022-12-11", "2022-11-13", "13:00", "12:00",
                4, "description4", "cost4", true, "sponsor4", "url4", "loc4");
        assertEquals("check conflict with event covers date range with same time range", true, testUser.checkConflict(testEvent4));

        //inside the date range and inside the time range
        Event testEvent5 = new Event("5", "title5", "category5", "2022-12-09", "2022-12-05", "12:40", "12:20",
                5, "description5", "cost5", true, "sponsor5", "url5", "loc5");
        assertEquals("check conflict with event inside the date range and inside the time range", true, testUser.checkConflict(testEvent5));

        //inside the date range and time overlaps start time
        Event testEvent6 = new Event("6", "title6", "category6", "2022-12-09", "2022-12-05", "12:40", "09:20",
                6, "description6", "cost6", true, "sponsor6", "url6", "loc6");
        assertEquals("check conflict with event inside the date range and time overlaps start time", true, testUser.checkConflict(testEvent6));

        //inside the date range and time overlaps end time
        Event testEvent7 = new Event("7", "title7", "category7", "2022-12-09", "2022-12-05", "13:40", "12:20",
                7, "description7", "cost7", true, "sponsor7", "url7", "loc7");
        assertEquals("check conflict with event inside the date range and time overlaps end time", true, testUser.checkConflict(testEvent7));

        //inside the date range and covers the time range
        Event testEvent8 = new Event("8", "title8", "category8", "2022-12-09", "2022-12-05", "13:40", "08:20",
                8, "description8", "cost8", true, "sponsor8", "url8", "loc8");
        assertEquals("check conflict with event inside the date range and covers the time range", true, testUser.checkConflict(testEvent8));



    }

    @Test
    //mainly check with event1
    public void testCheckConflict_no_conflict() {
        //the same event
        assertEquals("check conflict with the same event", false, testUser.checkConflict(event0));

        //event after the end date with same time range
        Event testEvent2 = new Event("2", "title2", "category2", "2023-01-01", "2022-12-28", "18:00", "17:00",
                2, "description2", "cost2", true, "sponsor2", "url2", "loc2");
        assertEquals("check conflict with event after the end date with same time range", false, testUser.checkConflict(testEvent2));

        //event before the start date with same time range
        Event testEvent3 = new Event("3", "title3", "category3", "2022-12-16", "2022-12-15", "18:00", "17:00",
                3, "description3", "cost3", true, "sponsor3", "url3", "loc3");
        assertEquals("check conflict with event before the start date with same time range", false, testUser.checkConflict(testEvent3));

        //event in the same date range but time before
        Event testEvent4 = new Event("4", "title4", "category4", "2022-12-27", "2022-12-20", "12:00", "13:00",
                4, "description4", "cost4", true, "sponsor4", "url4", "loc4");
        assertEquals("check conflict with event event in the same date range but time before", false, testUser.checkConflict(testEvent4));

        //event in the same date range but time after
        Event testEvent5 = new Event("5", "title5", "category5", "2022-12-27", "2022-12-20", "19:00", "18:30",
                5, "description5", "cost5", true, "sponsor5", "url5", "loc5");
        assertEquals("check conflict with event event in the same date range but time after", false, testUser.checkConflict(testEvent5));
    }

    @Test
    public void testEventExist_exist() {
        //event exist
        assertEquals("user have registered event 0", true, testUser.eventExist("0"));

    }

    @Test
    public void testEventExist_no_exist() {
        //event not exist
        assertEquals("user have registered event 3", false, testUser.eventExist("3"));

    }

    @Test
    public void testRegisterEvent() {
        //event is registered
        Event testEvent = new Event("2", "title2", "category2", "2022-12-27", "2022-12-20", "18:00", "17:00",
                2, "description2", "cost2", true, "sponsor2", "url2", "loc2");
        assertEquals("user have registered the event successfully", true, testUser.registerEvent(testEvent));
        assertEquals("new event exists in the user event list", true, testUser.eventExist("2"));
    }

    @Test
    public void testRegisterEvent_sameEvent() {
        assertEquals("user have registered the same event should return false", false, testUser.registerEvent(event0));
    }

    @Test
    public void testRemoveEvent() {
        assertEquals("user have registered the event successfully", true, testUser.removeEvent(event0));
        assertEquals("user have registered the event successfully", false, testUser.eventExist("0"));
    }

    @Test
    public void testRemoveEvent_not_exist() {
        //remove an event that did not exist
        Event testEvent = new Event("2", "title2", "category2", "2022-12-27", "2022-12-20", "18:00", "17:00",
                2, "description2", "cost2", true, "sponsor2", "url2", "loc2");
        assertEquals("user have registered the event successfully", false, testUser.removeEvent(testEvent));

    }
}
