package edu.usc.eventme;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class EventUnitTest {
    @Test
    public void testUserChange() {
        int numU = 20;
        Event e = new Event("1","foodmarket","outdoor","2022-10-22","2022-10-22","22:00","20:00", numU, "fun event","$$",true, "USC","none","usc village",34.0265583,-118.28547);
        e.addUser();
        assertEquals(numU+1, e.getNumUser());
        e.lessUser();
        assertEquals(numU,e.getNumUser());
    }

    @Test
    public void sortByCost_diff() {
        Event e = new Event("1","foodmarket","outdoor","2022-10-22","2022-10-22","22:00","20:00", 10, "fun event","$$",true, "USC","none","usc village",34.0265583,-118.28547);
        Event e2 = new Event("2","foodmarket","outdoor","2022-10-22","2022-10-22","22:00","20:00", 10, "fun event","$$$",true, "USC","none","usc village",34.0265583,-118.28547);
        Event e3 = new Event("3","foodmarket","outdoor","2022-10-22","2022-10-22","22:00","20:00", 10, "fun event","$",true, "USC","none","usc village",34.0265583,-118.28547);
        EventList el = new EventList();
        el.getEventList().add(e);
        el.getEventList().add(e2);
        el.getEventList().add(e3);
        el.sort("cost");
        assertEquals(e3.getId(),el.getEventList().get(0).getId());
        assertEquals(e.getId(),el.getEventList().get(1).getId());
        assertEquals(e2.getId(),el.getEventList().get(2).getId());
    }

    @Test
    public void sortByCost_same() {
        Event e = new Event("1","foodmarket","outdoor","2022-10-22","2022-10-22","22:00","20:00", 10, "fun event","$$",true, "USC","none","usc village",34.0265583,-118.28547);
        Event e2 = new Event("2","foodmarket","outdoor","2022-10-22","2022-10-22","22:00","20:00", 10, "fun event","$$",true, "USC","none","usc village",34.0265583,-118.28547);
        Event e3 = new Event("3","foodmarket","outdoor","2022-10-22","2022-10-22","22:00","20:00", 10, "fun event","$$",true, "USC","none","usc village",34.0265583,-118.28547);
        EventList el = new EventList();
        el.getEventList().add(e);
        el.getEventList().add(e2);
        el.getEventList().add(e3);
        el.sort("cost");
        assertEquals(e.getId(),el.getEventList().get(0).getId());
        assertEquals(e2.getId(),el.getEventList().get(1).getId());
        assertEquals(e3.getId(),el.getEventList().get(2).getId());
    }

    @Test
    public void sortByDate_diff() {
        Event e = new Event("1","foodmarket","outdoor","2022-10-30","2022-10-26","22:00","20:00", 10, "fun event","$$",true, "USC","none","usc village",34.0265583,-118.28547);
        Event e2 = new Event("2","foodmarket","outdoor","2022-11-30","2022-10-23","22:00","20:00", 10, "fun event","$$",true, "USC","none","usc village",34.0265583,-118.28547);
        Event e3 = new Event("3","foodmarket","outdoor","2022-10-30","2022-10-22","22:00","20:00", 10, "fun event","$$",true, "USC","none","usc village",34.0265583,-118.28547);
        Event e4 = new Event("4","foodmarket","outdoor","2022-10-30","2022-10-22","22:00","12:00", 10, "fun event","$$",true, "USC","none","usc village",34.0265583,-118.28547);
        EventList el = new EventList();
        el.getEventList().add(e);
        el.getEventList().add(e2);
        el.getEventList().add(e3);
        el.getEventList().add(e4);
        el.sort("date");
        assertEquals(e3.getId(),el.getEventList().get(0).getId());
        assertEquals(e4.getId(),el.getEventList().get(1).getId());
        assertEquals(e2.getId(),el.getEventList().get(2).getId());
        assertEquals(e.getId(),el.getEventList().get(3).getId());
    }

    @Test
    public void sortByDate_sameStart() {
        Event e = new Event("1","foodmarket","outdoor","2022-10-22","2022-10-22","22:00","20:00", 10, "fun event","$$",true, "USC","none","usc village",34.0265583,-118.28547);
        Event e2 = new Event("2","foodmarket","outdoor","2022-10-29","2022-10-22","22:00","20:00", 10, "fun event","$$",true, "USC","none","usc village",34.0265583,-118.28547);
        Event e3 = new Event("3","foodmarket","outdoor","2022-10-02","2022-10-22","22:00","20:00", 10, "fun event","$$",true, "USC","none","usc village",34.0265583,-118.28547);
        Event e4 = new Event("4","foodmarket","outdoor","2022-10-29","2022-10-22","22:00","12:00", 10, "fun event","$$",true, "USC","none","usc village",34.0265583,-118.28547);
        EventList el = new EventList();
        el.getEventList().add(e);
        el.getEventList().add(e2);
        el.getEventList().add(e3);
        el.getEventList().add(e4);
        el.sort("date");
        assertEquals(e.getId(),el.getEventList().get(0).getId());
        assertEquals(e2.getId(),el.getEventList().get(1).getId());
        assertEquals(e3.getId(),el.getEventList().get(2).getId());
        assertEquals(e4.getId(),el.getEventList().get(3).getId());
    }
    @Test
    public void sortByDis_diff() {
        Event e = new Event("1","foodmarket","outdoor","2022-10-22","2022-10-22","22:00","20:00", 10, "fun event","$$",true, "USC","none","usc village",34.0099327,-118.49653420000001);
        Event e2 = new Event("2","foodmarket","outdoor","2022-10-22","2022-10-22","22:00","20:00", 10, "fun event","$$",true, "USC","none","usc village",34.0265583,-118.28547);
        Event e3 = new Event("3","foodmarket","outdoor","2022-10-22","2022-10-22","22:00","20:00", 10, "fun event","$$",true, "USC","none","usc village",34.0265583,-118.9);
        EventList el = new EventList();
        el.getEventList().add(e);
        el.getEventList().add(e2);
        el.getEventList().add(e3);
        el.sort("distance");
        assertEquals(e2.getId(),el.getEventList().get(0).getId());
        assertEquals(e.getId(),el.getEventList().get(1).getId());
        assertEquals(e3.getId(),el.getEventList().get(2).getId());
    }

    @Test
    public void sortByDis_same() {
        Event e = new Event("1","foodmarket","outdoor","2022-10-22","2022-10-22","22:00","20:00", 10, "fun event","$$",true, "USC","none","usc village",34.0265583,-118.28547);
        Event e2 = new Event("2","foodmarket","outdoor","2022-10-22","2022-10-22","22:00","19:00", 10, "fun event","$$",true, "USC","none","usc village",34.0265583,-118.28547);
        Event e3 = new Event("3","foodmarket","outdoor","2022-10-23","2022-10-22","22:00","20:00", 10, "fun event","$$",true, "USC","none","usc village",34.0265583,-118.28547);
        EventList el = new EventList();
        el.getEventList().add(e);
        el.getEventList().add(e2);
        el.getEventList().add(e3);
        el.sort("distance");
        assertEquals(e.getId(),el.getEventList().get(0).getId());
        assertEquals(e2.getId(),el.getEventList().get(1).getId());
        assertEquals(e3.getId(),el.getEventList().get(2).getId());
    }

    @Test
    public void sortByAlph_diff(){
        Event e = new Event("1","fruit melon market ","outdoor","2022-10-22","2022-10-22","22:00","20:00", 10, "fun event","$$",true, "USC","none","usc village",34.0265583,-118.28547);
        Event e2 = new Event("2","fruit bear market","outdoor","2022-10-22","2022-10-22","22:00","20:00", 10, "fun event","$$",true, "USC","none","usc village",34.0265583,-118.28547);
        Event e3 = new Event("3","fruit apple market","outdoor","2022-10-22","2022-10-22","22:00","20:00", 10, "fun event","$$",true, "USC","none","usc village",34.0265583,-118.28547);
        Event e4 = new Event("4","Art Expo","outdoor","2022-10-22","2022-10-22","22:00","20:00", 10, "fun event","$$",true, "USC","none","usc village",34.0265583,-118.28547);
        Event e5 = new Event("5","zoo festival","outdoor","2022-10-22","2022-10-22","22:00","20:00", 10, "fun event","$$",true, "USC","none","usc village",34.0265583,-118.28547);
        EventList el = new EventList();
        el.getEventList().add(e);
        el.getEventList().add(e2);
        el.getEventList().add(e3);
        el.getEventList().add(e4);
        el.getEventList().add(e5);
        el.sort("alphabet");
        assertEquals(e4.getId(),el.getEventList().get(0).getId());
        assertEquals(e3.getId(),el.getEventList().get(1).getId());
        assertEquals(e2.getId(),el.getEventList().get(2).getId());
        assertEquals(e.getId(),el.getEventList().get(3).getId());
        assertEquals(e5.getId(),el.getEventList().get(4).getId());
    }

}