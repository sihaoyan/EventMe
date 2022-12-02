package edu.usc.eventme;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class EventOperationsUnit {
    @Test
    public void test_findDistance() {
        Event testEvent = new Event("1", "title", "category", "2022-12-27", "2022-12-20", "18:00", "17:00",
                2, "description", "cost", true, "sponsor", "url", "loc",1,1);
        assertEquals("correct distance", 97.71, testEvent.findDis(0,0), 1);
        assertEquals("correct distance", 877, testEvent.findDis(10,10), 1);
    }

    @Test
    public void test_addOneEvent(){
        EventList alist= new EventList();
        Event testEvent = new Event("1", "title", "category", "2022-12-27", "2022-12-20", "18:00", "17:00",
                2, "description", "cost", true, "sponsor", "url", "loc",1,1);
        assertEquals("Eventlist is empty before adding the event", 0, alist.getEventList().size());
        alist.addEvent(testEvent);
        assertEquals("Eventlist has one event after adding the event", 1, alist.getEventList().size());
        assertEquals("The event in the list has the correct title", "title", alist.getEventList().get(0).getEventTitle());
    }

    @Test
    public void test_addThreeEvent(){
        EventList alist= new EventList();
        Event e1 = new Event("1", "title", "category", "2022-12-27", "2022-12-20", "18:00", "17:00",
                2, "description", "cost", true, "sponsor", "url", "loc",1,1);
        Event e2 = new Event("2", "title2", "category2", "2022-12-27", "2022-12-20", "18:00", "17:00",
                2, "description2", "cost2", true, "sponsor2", "url2", "loc2",1,1);
        Event e3 = new Event("3", "title3", "category", "2022-12-27", "2022-12-20", "18:00", "17:00",
                2, "description", "cost", true, "sponsor", "url", "loc",1,1);
        assertEquals("Eventlist is empty before adding the event", 0, alist.getEventList().size());
        alist.addEvent(e1);
        assertEquals("Eventlist has one event after adding one event", 1, alist.getEventList().size());
        alist.addEvent(e2);
        assertEquals("Eventlist has two events after adding two events", 2, alist.getEventList().size());
        alist.addEvent(e3);
        assertEquals("Eventlist has three events after adding three events", 3, alist.getEventList().size());

        assertEquals("The first event in the list has the correct title", "title", alist.getEventList().get(0).getEventTitle());
        assertEquals("The second event in the list has the correct title", "title2", alist.getEventList().get(1).getEventTitle());
        assertEquals("The third event in the list has the correct title", "title3", alist.getEventList().get(2).getEventTitle());
    }

    @Test
    public void test_removeOneEvent(){
        EventList alist= new EventList();
        Event e1 = new Event("1", "e", "category", "2022-12-27", "2022-12-20", "18:00", "17:00",
                2, "description", "cost", true, "sponsor", "url", "loc",1,1);
        Event e2 = new Event("2", "e2", "category2", "2022-12-27", "2022-12-20", "18:00", "17:00",
                2, "description2", "cost2", true, "sponsor2", "url2", "loc2",1,1);
        Event e3 = new Event("3", "e3", "category", "2022-12-27", "2022-12-20", "18:00", "17:00",
                2, "description", "cost", true, "sponsor", "url", "loc",1,1);
        assertEquals("Eventlist is empty before adding the event", 0, alist.getEventList().size());
        alist.addEvent(e1);
        alist.addEvent(e2);
        alist.addEvent(e3);
        assertEquals("Eventlist has three events after adding three events", 3, alist.getEventList().size());
        assertEquals("Second event in the list is e2 before removing", "e2", alist.getEventList().get(1).getEventTitle());
        alist.removeEvent(e2);
        assertEquals("Eventlist has two events after adding three events", 2, alist.getEventList().size());
        assertEquals("Second event in the list is e3 after removing e2", "e3", alist.getEventList().get(1).getEventTitle());

    }

    @Test
    public void test_removeAllEvent(){
        EventList alist= new EventList();
        Event e1 = new Event("1", "e", "category", "2022-12-27", "2022-12-20", "18:00", "17:00",
                2, "description", "cost", true, "sponsor", "url", "loc",1,1);
        Event e2 = new Event("2", "e2", "category2", "2022-12-27", "2022-12-20", "18:00", "17:00",
                2, "description2", "cost2", true, "sponsor2", "url2", "loc2",1,1);
        Event e3 = new Event("3", "e3", "category", "2022-12-27", "2022-12-20", "18:00", "17:00",
                2, "description", "cost", true, "sponsor", "url", "loc",1,1);
        assertEquals("Eventlist is empty before adding the event", 0, alist.getEventList().size());
        alist.addEvent(e1);
        alist.addEvent(e2);
        alist.addEvent(e3);
        assertEquals("Eventlist has three events after adding three events", 3, alist.getEventList().size());
        alist.removeEvent(e1);
        alist.removeEvent(e2);
        alist.removeEvent(e3);
        assertEquals("Eventlist is empty after removing three events", 0, alist.getEventList().size());
    }


}
