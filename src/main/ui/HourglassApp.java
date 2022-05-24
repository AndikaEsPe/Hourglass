//package ui;
//
//import model.Activities;
//import model.Activity;
//import persistence.JsonReader;
//import persistence.JsonWriter;
//
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.util.Scanner;
//
//// inspired from TellerApp; https://github.students.cs.ubc.ca/CPSC210/TellerApp.git
//// Hourglass application
//public class HourglassApp {
//    private static final String JSON_STORE = "./data/activities.json";
//    private Scanner input;
//    private Activities activities;
//    private JsonWriter jsonWriter;
//    private JsonReader jsonReader;
//
//    // EFFECTS: runs the hourglass application
//    public HourglassApp() throws FileNotFoundException {
//        init();
//        jsonWriter = new JsonWriter(JSON_STORE);
//        jsonReader = new JsonReader(JSON_STORE);
//        runHourglass();
//    }
//
//    // MODIFIES: this
//    // EFFECTS: processes user input
//    private void runHourglass() {
//        boolean keepGoing = true;
//        String command;
//        input = new Scanner(System.in);
//
//        while (keepGoing) {
//            displayMenu();
//            command = input.nextLine();
//            command = command.toLowerCase();
//
//            if (command.equals("q")) {
//                keepGoing = false;
//            } else {
//                processCommand(command);
//            }
//        }
//        System.out.println("\nDrink, breathe, alive!");
//    }
//
//    // MODIFIES: this
//    // EFFECTS: initialize Activities and Scanner
//    private void init() {
//        input = new Scanner(System.in);
//        activities = new Activities("Dika's activities");
//    }
//
//    // MODIFIES: this
//    // EFFECTS: processes user command
//    private void processCommand(String command) {
//        switch (command) {
//            case "t":
//                giveActivitiesRecord();
//                break;
//            case "r":
//                removeWithId();
//                input.nextLine();
//                break;
//            case "a":
//                addActivity();
//                break;
//            case "s":
//                saveActivities();
//                break;
//            case "l":
//                loadActivities();
//                break;
//        }
//    }
//
//    // REQUIRES: processCommand()
//    // MODIFIES: this
//    // EFFECTS: add activity when a is pressed
//    private void addActivity() {
//        System.out.println("Input activity:");
//        String activityName = input.nextLine();
//        Activity a = new Activity(activityName);
//        System.out.println(activityName + "     ID: " + a.getId() + "     Status: " + a.getStatus().name());
//        System.out.println("\nInput target (in minutes):");
//        int target = input.nextInt();
//        a.setDeadline(target);
//        start(a);
//        stop(a);
//        giveActivityDescription(a);
//        activities.addActivity(a);
//        input.nextLine();
//    }
//
//    // REQUIRES: processCommand()
//    // MODIFIES: this
//    // EFFECTS: give description of activity so far and setup activity when s is pressed to start
//    private void start(Activity a) {
//        giveActivityDescription(a);
//        promptValidS("start");
//        a.start();
//        a.setOngoing();
//    }
//
//    // REQUIRES: processCommand()
//    // MODIFIES: this
//    // EFFECTS: give description of activity so far and setup activity when s is pressed to stop
//    private void stop(Activity a) {
//        giveActivityDescription(a);
//        promptValidS("stop");
//        a.stop();
//        if (a.isOvertime()) {
//            a.setOvertime();
//        } else {
//            a.setFinished();
//        }
//    }
//
//    // REQUIRES: processCommand()
//    // EFFECTS: prompts user to input s
//    private void promptValidS(String desc) {
//        System.out.println("Press \"s\" when you " + desc + " the activity");
//        String stop = input.next();
//        stop = stop.toLowerCase();
//        if (!stop.equals("s")) {
//            boolean keepGoing = true;
//            while (keepGoing) {
//                System.out.println("Invalid input! \nPress \"s\" when you start the activity");
//                stop = input.next();
//                stop = stop.toLowerCase();
//                if (stop.equals("s")) {
//                    keepGoing = false;
//                }
//            }
//        }
//    }
//
//    // EFFECTS: displays menu of options to user
//    private void displayMenu() {
//        System.out.println("\n Select from:");
//        System.out.println("\ta -> add new activity");
//        System.out.println("\tr -> remove activity");
//        System.out.println("\tt -> track record");
//        System.out.println("\tq -> quit");
//        System.out.println("\ts -> save current activities");
//        System.out.println("\tl -> load previous activities");
//
//    }
//
//    // REQUIRES: setDeadline()
//    // EFFECTS: give activity general description
//    private void giveActivityDescription(Activity a) {
//        System.out.println(a.getName() + "     ID: " + a.getId() + "     Status: " + a.getStatus().name()
//                + "     Deadline: " + a.getDeadline() + " minutes\n");
//    }
//
//    // EFFECTS: give activities record so far
//    private void giveActivitiesRecord() {
//        if (activities.size() == 0) {
//            System.out.println("No activity record yet!");
//        } else {
//            for (Activity a : activities.getActivities()) {
//                giveActivityDescription(a);
//            }
//        }
//    }
//
//    // REQUIRES: id is found if activities not empty
//    // MODIFIES: this
//    // EFFECTS: remove activity with the given id from list
//    private void removeWithId() {
//        if (activities.size() == 0) {
//            System.out.println("No activity to be removed!");
//        } else {
//            System.out.println("Enter activity id you want to remove:");
//            int id = input.nextInt();
//            activities.removeActivity(id);
//        }
//    }
//
//    // EFFECTS: saves the activities to file
//    private void saveActivities() {
//        try {
//            jsonWriter.open();
//            jsonWriter.write(activities);
//            jsonWriter.close();
//            System.out.println("Saved " + activities.getName() + " to " + JSON_STORE);
//        } catch (FileNotFoundException e) {
//            System.out.println("Unable to write to file: " + JSON_STORE);
//        }
//    }
//
//    // MODIFIES: this
//    // EFFECTS: loads activities from file
//    private void loadActivities() {
//        try {
//            activities = jsonReader.read();
//            System.out.println("Loaded " + activities.getName() + JSON_STORE);
//        } catch (IOException e) {
//            System.out.println("Unable to read from file: " + JSON_STORE);
//        }
//    }
//}
