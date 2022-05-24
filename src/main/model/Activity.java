package model;

import exceptions.TimerException;
import org.json.JSONObject;
import persistence.Writable;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

// model is inspired from TellerApp; https://github.students.cs.ubc.ca/CPSC210/TellerApp.git
// represents an activity with its name, date and time when the activity is started and finished, and its status
public class Activity implements Writable {

    // represents activity status; not yet started, ongoing, overtime, finished
    public enum Status {
        NOT_YET_STARTED,
        ONGOING,
        OVERTIME,
        FINISHED
    }

    private static int nextActivityId = 1;   // tracks id of next activity added
    private int id;                          // activity id
    private String name;                     // name of the activity
    private LocalTime startTime;             // when the activity is started
    private LocalTime endTime;               // when the activity is finished
    private Status status;                   // the current activity's status
    private LocalDate date;                  // the current date
    private long deadline;                   // the activity deadline in minute

    // REQUIRES: activityName has a non-zero length
    // EFFECTS: instantiates an Activity with name set to activityName, set status to not yet started.
    //         activity id is a positive integer not assigned to any other activity.
    public Activity(String activityName) {
        id = nextActivityId++;
        name = activityName;
        status = Status.NOT_YET_STARTED;
    }

    // REQUIRES: start() and stop()
    // EFFECTS: instantiates an Activity with all fields set up
    public Activity(String activityName, int id, LocalTime startTime, LocalTime endTime, int deadline, Status status) {
        this.id = id;
        nextActivityId = ++id;
        this.name = activityName;
        this.deadline = deadline;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
    }

    // EFFECTS:  return activity id
    public int getId() {
        return id;
    }

    // EFFECTS: return activity name
    public String getName() {
        return name;
    }

    // EFFECTS: set status to ONGOING
    public void setOngoing() {
        status = Status.ONGOING;
    }

    // EFFECTS: set status to FINISHED
    public void setFinished() {
        status = Status.FINISHED;
    }

    // EFFECTS: set status to OVERTIME
    public void setOvertime() {
        status = Status.OVERTIME;
    }

    // EFFECTS: return activity status
    public Status getStatus() {
        return status;
    }

    // EFFECTS: return startTime
    public LocalTime getStartTime() throws TimerException {
        if (startTime == null) {
            throw new TimerException();
        }
        return startTime;
    }

    // EFFECTS: return stopTime
    public LocalTime getEndTime() throws TimerException {
        if (endTime == null) {
            throw new TimerException();
        }
        return endTime;
    }

    // MODIFIES: this
    // EFFECTS: set startTime to current local time
    public void start() {
        startTime = LocalTime.now();
    }

    // REQUIRES: start()
    // MODIFIES: this
    // EFFECTS: set endTime to current local time
    public void stop() {
        endTime = LocalTime.now();
    }

    // REQUIRES: target in minutes
    // MODIFIES: this
    // EFFECTS: set deadline to target
    public void setDeadline(int target) {
        deadline = target;
    }

    // EFFECTS: return deadline in minutes
    public long getDeadline() {
        return deadline;
    }

    // MODIFIES: this
    // EFFECTS: set status to OVERTIME if endTime - startTime > deadline and return true, false otherwise
    public boolean isOvertime() throws TimerException {
        if (Duration.between(getStartTime(), getEndTime()).getSeconds() > deadline * 60) {
            setOvertime();
            return true;
        }
        return false;
    }

    // REQUIRES: setDeadline()
    // EFFECTS: give activity general description
    public String giveActivityDescription() {
        return getName() + "     ID: " + getId() + "     Status: " + getStatus().name()
                + "     Deadline: " + getDeadline() + " minutes\n";
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("id", id);
        json.put("start time", startTime.toString());
        json.put("end time", endTime.toString());
        json.put("deadline", deadline);
        json.put("status", status);
        return json;
    }

}
