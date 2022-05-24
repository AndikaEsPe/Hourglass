package model;

import exceptions.TimerException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

// inspired from TellerApp; https://github.students.cs.ubc.ca/CPSC210/TellerApp.git and
// IntegerSetLecLab; https://github.students.cs.ubc.ca/CPSC210/IntegerSetLecLab.git
// test suite for Activities
class ActivitiesTest {
    private static final int NUMITEMS = 5000;
    private Activity activity;
    private Activities activities;

    @BeforeEach
    public void setup() {
        activity = new Activity("Study computer science!");
        activities = new Activities("Dika's activities");
    }

    @Test
    public void testActivityConstructor() {
        assertEquals("Study computer science!", activity.getName());
        assertTrue(activity.getId() > 0);
        assertEquals("NOT_YET_STARTED", activity.getStatus().name());
        assertEquals(0, activities.getActivitiesRecord().size());
    }

    @Test
    public void testNullStartTime() {
        try {
            activity.getStartTime();
            fail("Exception is expected");
        } catch (TimerException e) {
            // do nothing
        }
    }

    @Test
    public void testNullStopTime() {
        try {
            activity.start();
            activity.getEndTime();
            fail("Exception is expected");
        } catch (TimerException e) {
            // do nothing
        }
    }

    @Test
    public void testOngoing() {
        activity.start();
        activity.stop();
        activity.setOngoing();
        assertEquals("ONGOING", activity.getStatus().name());
        try {
            assertFalse(activity.isOvertime());
        } catch (TimerException e) {
            fail("Unexpected TimerException");
        }
    }

    @Test
    public void testFinished() {
        activity.start();
        activity.stop();
        activity.setFinished();
        assertEquals("FINISHED", activity.getStatus().name());
        try {
            assertFalse(activity.isOvertime());
        } catch (TimerException e) {
            fail("Unexpected TimerException");
        }
    }

    @Test
    public void testOvertime() throws InterruptedException {
        activity.setDeadline(0);
        activity.start();
        TimeUnit.SECONDS.sleep(1);
        activity.stop();
        activity.setOvertime();
        assertEquals("OVERTIME", activity.getStatus().name());
        try {
            assertTrue(activity.isOvertime());
        } catch (TimerException e) {
            fail("Unexpected TimerException");
        }
    }

    @Test
    public void testStart() {
        activity.start();
        LocalTime time = LocalTime.now();

        try {
            LocalTime startTime = activity.getStartTime();
            assertTrue(Duration.between(startTime, time).getSeconds() < 0.0000001);
        } catch (TimerException e) {
            fail("Unexpected exception");
        }

    }

    @Test
    public void testEnd() throws InterruptedException {
        activity.start();
        TimeUnit.SECONDS.sleep(1);
        activity.stop();

        try {
            LocalTime startTime = activity.getStartTime();
            LocalTime stopTime = activity.getEndTime();
            assertTrue(Duration.between(startTime, stopTime).getSeconds() < 1.0000001);
        } catch (TimerException e) {
            fail("Unexpected exception");
        }

    }

    @Test
    public void testIsOvertime() throws InterruptedException {
        activity.setDeadline(0);
        activity.start();
        TimeUnit.SECONDS.sleep(1);
        activity.stop();

        try {
            assertTrue(activity.isOvertime());
        } catch (TimerException e) {
            fail("Unexpected TimerException");
        }
    }

    @Test
    public void testIsNotOvertime() {
        activity.setDeadline(100);
        activity.start();
        activity.stop();

        try {
            assertFalse(activity.isOvertime());
        } catch (TimerException e) {
            fail("Unexpected TimerException");
        }
    }

    @Test
    public void testActivitiesConstructor() {
        assertEquals(0, activities.size());
    }

    @Test
    public void testSize() {
        for (int i = 0; i < NUMITEMS; i++) {
            activities.addActivity(activity);
        }

        assertEquals(NUMITEMS, activities.size());
    }

    @Test
    public void testDeadline() {
        activity.setDeadline(10);
        assertEquals(10, activity.getDeadline());
    }

    @Test
    public void testContainsOne() {
        assertFalse(activities.contains(activity.getId()));
        activities.addActivity(activity);
        assertTrue(activities.contains(activity.getId()));
    }

    @Test
    public void testContainsLots() {
        ArrayList<Integer> idList = new ArrayList<>();
        for (int i = 0; i < NUMITEMS; i++) {
            Activity a = new Activity(String.valueOf(i));
            activities.addActivity(a);
            idList.add(a.getId());
        }

        for (int i = 0; i < NUMITEMS; i++) {
            assertTrue(activities.contains(idList.get(i)));
        }
    }

    @Test
    public void testAddOneActivity() {
        activities.addActivity(activity);

        assertTrue(activities.contains(activity.getId()));
        assertEquals(1, activities.size());
    }

    @Test
    public void testAddLotsActivity() {
        ArrayList<Integer> idList = new ArrayList<>();
        for (int i = 0; i < NUMITEMS; i++) {
            Activity a = new Activity(String.valueOf(i));
            activities.addActivity(a);
            idList.add(a.getId());
        }
        assertTrue(activities.contains(idList.get(0)));
        assertTrue(activities.contains(idList.get(NUMITEMS / 2)));
        assertTrue(activities.contains(idList.get(NUMITEMS - 1)));
        assertEquals(NUMITEMS, activities.size());
    }

    @Test
    public void testRemove() {
        activities.addActivity(activity);
        activities.removeActivity(activity.getId());
        assertFalse(activities.contains(activity.getId()));
        assertEquals(0, activities.size());
    }

    @Test
    public void testRemoveLots() {
        ArrayList<Integer> idList = new ArrayList<>();
        for (int i = 0; i < NUMITEMS; i++) {
            Activity a = new Activity(String.valueOf(i));
            activities.addActivity(a);
            idList.add(a.getId());
        }
        for (int i = 0; i < NUMITEMS; i++) {
            activities.removeActivity(idList.get(i));
        }

        assertFalse(activities.contains(idList.get(0)));
        assertFalse(activities.contains(idList.get(NUMITEMS / 2)));
        assertFalse(activities.contains(idList.get(NUMITEMS - 1)));
        assertEquals(0, activities.size());
    }

    @Test
    public void testGetActivities() {
        activities.addActivity(activity);
        assertEquals(1, activities.getActivities().size());
    }
}