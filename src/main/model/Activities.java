package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.JsonReader;
import persistence.Writable;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

// model is inspired from IntegerSetLecLab; https://github.students.cs.ubc.ca/CPSC210/IntegerSetLecLab.git
// represents a list of activities
public class Activities implements Writable {
    private String name;
    private List<Activity> activities;
    private DefaultListModel activitiesRecord;

    // EFFECTS: instantiates an empty activity list with a name
    public Activities(String name) {
        this.name = name;
        activities = new ArrayList<>();
        activitiesRecord = new DefaultListModel();
    }

    // MODIFIES: this
    // EFFECTS: add the given activity to the list
    public void addActivity(Activity activity) {
        activities.add(activity);
    }

    // REQUIRES: id is found in the activity inside the Activities
    // MODIFIES: this
    // EFFECTS: remove activity from the list with the given id
    public void removeActivity(int id) {
        activities.removeIf(activity -> activity.getId() == id);
    }

    // MODIFIES: this
    // EFFECTS: add activity description to activitiesRecord
    public void activitiesToRecord() {
        for (Activity a : activities) {
            activitiesRecord.addElement(a.giveActivityDescription());
        }
    }

    // EFFECTS: return true if activity id is found within the activity in the Activities, false otherwise
    public boolean contains(int id) {
        for (Activity a : activities) {
            if (a.getId() == id) {
                return true;
            }
        }
        return false;
    }

    // EFFECTS: return activitiesRecord
    public DefaultListModel getActivitiesRecord() {
        return activitiesRecord;
    }

    // EFFECTS: return activities name
    public String getName() {
        return name;
    }

    // EFFECTS: return the number of activities in the list
    public int size() {
        return activities.size();
    }

    // EFFECTS: return activities (accessor)
    public List<Activity> getActivities() {
        return activities;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("activities", activitiesToJson());
        return json;
    }

    // EFFECTS: returns activity in this activities as a JSON array
    private JSONArray activitiesToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Activity a : activities) {
            jsonArray.put(a.toJson());
        }

        return jsonArray;
    }
}
