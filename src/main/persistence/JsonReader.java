package persistence;

import model.Activities;
import model.Activity;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.stream.Stream;

// inspired from https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git
// represents a reader that reads activities from JSON data stored in file
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads activities from file and returns it;
    // throws IOException if an error occurs reading data from file
    public Activities read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseActivities(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses activities from JSON object and returns it
    private Activities parseActivities(JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        Activities activities = new Activities(name);
        addActivities(activities, jsonObject);
        activities.activitiesToRecord();
        return activities;
    }

    // MODIFIES: activities
    // EFFECTS: parses activities from JSON object and adds them to activities
    private void addActivities(Activities activities, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("activities");
        for (Object json : jsonArray) {
            JSONObject nextActivity = (JSONObject) json;
            addActivity(activities, nextActivity);
        }

    }

    // MODIFIES: activities
    // EFFECTS: parses activity from JSON object and adds it to activities
    private void addActivity(Activities activities, JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        int id = jsonObject.getInt("id");
        int deadline = jsonObject.getInt("deadline");
        LocalTime startTime = LocalTime.parse(jsonObject.getString("start time"));
        LocalTime endTime = LocalTime.parse(jsonObject.getString("end time"));
        Activity.Status status = Activity.Status.valueOf(jsonObject.getString("status"));
        Activity activity = new Activity(name,id, startTime, endTime, deadline, status);
        activities.addActivity(activity);
    }

}
