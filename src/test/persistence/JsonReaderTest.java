package persistence;

import exceptions.TimerException;
import model.Activities;
import model.Activity;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class JsonReaderTest extends JsonTest{

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            Activities activities = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyActivities() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyActivities.json");
        try {
            Activities activities = reader.read();
            assertEquals("Dika's activities", activities.getName());
            assertEquals(0, activities.size());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralActivities() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralActivities.json");
        try {
            Activities activities = reader.read();
            assertEquals("Dika's activities", activities.getName());
            List<Activity> activityList = activities.getActivities();
            assertEquals(2, activities.size());
            LocalTime startTime1 = LocalTime.parse("11:00:00");
            LocalTime endTime1 = LocalTime.parse("11:15:00");
            LocalTime startTime2 = LocalTime.parse("11:15:00");
            LocalTime endTime2 = LocalTime.parse("11:46:00");
            try {
                checkActivity("Watch youtube", 1, startTime1, endTime1,
                        10, Activity.Status.FINISHED, activityList.get(0));
                checkActivity("Study CPSC 210", 2, startTime2, endTime2,
                        30, Activity.Status.OVERTIME, activityList.get(1));
            } catch (TimerException e) {
                fail("Unexpected TimerException");
            }
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

}
