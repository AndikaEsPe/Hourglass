package persistence;

import exceptions.TimerException;
import model.Activity;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonTest {
    protected void checkActivity(String name, int id, LocalTime startTime, LocalTime endTime, int deadline,
                                 Activity.Status status, Activity activity) throws TimerException {
        assertEquals(name, activity.getName());
        assertEquals(id, activity.getId());
        assertEquals(startTime, activity.getStartTime());
        assertEquals(endTime, activity.getEndTime());
        assertEquals(deadline, activity.getDeadline());
        assertEquals(status, activity.getStatus());
    }
}
