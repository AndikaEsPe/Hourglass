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

public class JsonWriterTest extends JsonTest{

    @Test
    void testWriterInvalidFile() {
        try {
            Activities activities = new Activities("Dika's activities");
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyActivities() {
        try {
            Activities activities = new Activities("Dika's activities");
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyActivities.json");
            writer.open();
            writer.write(activities);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyActivities.json");
            activities = reader.read();
            assertEquals("Dika's activities", activities.getName());
            assertEquals(0, activities.size());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralActivities() {
        try {
            Activities activities = new Activities("Dika's activities");
            LocalTime startTime1 = LocalTime.parse("11:00:00");
            LocalTime endTime1 = LocalTime.parse("11:15:00");
            LocalTime startTime2 = LocalTime.parse("11:15:00");
            LocalTime endTime2 = LocalTime.parse("11:46:00");
            activities.addActivity(new Activity("Watch youtube", 1, startTime1, endTime1,
                    10, Activity.Status.FINISHED));
            activities.addActivity(new Activity("Study CPSC 210", 2, startTime2, endTime2,
                    30, Activity.Status.OVERTIME));
            JsonWriter writer = new JsonWriter("./data/testWriterGeneralActivities.json");
            writer.open();
            writer.write(activities);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralActivities.json");
            activities = reader.read();
            assertEquals("Dika's activities", activities.getName());
            List<Activity> activityList = activities.getActivities();
            assertEquals(2, activityList.size());
            try {
                checkActivity("Watch youtube", 1, startTime1, endTime1,
                        10, Activity.Status.FINISHED, activityList.get(0));
                checkActivity("Study CPSC 210", 2, startTime2, endTime2,
                        30, Activity.Status.OVERTIME, activityList.get(1));
            } catch (TimerException e) {
                fail("Unexpected TimerException");
            }
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }


    }

}
