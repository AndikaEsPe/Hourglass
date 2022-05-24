package ui;

import exceptions.TimerException;
import model.Activities;
import model.Activity;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioSystem;

// represents the main window where the hourglass runs
public class GUI extends JFrame {
    private static final int FRAME_WIDTH = 700;
    private static final int FRAME_HEIGHT = 400;
    private static final String JSON_STORE = "./data/activities.json";
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private JPanel inputPanel;
    private JPanel recordPanel;
    private JPanel removePanel;
    private JTextField activityName;
    private JTextField target;
    private JButton startButton;
    private JButton stopButton;
    private JButton removeButton;
    private JLabel inputActivityLabel;
    private JLabel activitiesRecordLabel;
    private JLabel targetLabel;
    private JLabel iconLabel;
    private JList recordList;
    private JScrollPane scrollPane;
    private Activities activities;
    private Activity activity;
    private ImageIcon hourglass;


    public GUI() {
        init();
        int input = JOptionPane.showConfirmDialog(null, loadStatus(), "File Loaded", JOptionPane.DEFAULT_OPTION);
        respondToStart();
        respondToStop();
        respondToRemove();
        setupPanel();
        setupFrame();
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                String[] buttonLabels = new String[]{"Yes", "No", "Cancel"};
                String defaultOption = buttonLabels[0];
                int closing = JOptionPane.showOptionDialog(null,
                        "Do you want to save before exiting?",
                        "Warning",
                        JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.WARNING_MESSAGE,
                        null,
                        buttonLabels,
                        defaultOption);
                closeOption(closing);
            }
        });
    }

    // MODIFIES: this
    // EFFECTS: initializes fields in GUI
    public void init() {
        activities = new Activities("My activities");
        jsonReader = new JsonReader(JSON_STORE);
        activities.activitiesToRecord();
        loadActivities();
        initComponents();
        jsonWriter = new JsonWriter(JSON_STORE);
    }

    // MODIFIES: this
    // EFFECTS: initializes components
    public void initComponents() {
        activityName = new JTextField(20);
        target = new JTextField(3);
        startButton = new JButton("Start");
        stopButton = new JButton("Stop");
        removeButton = new JButton("remove");
        inputActivityLabel = new JLabel("Input activity:");
        activitiesRecordLabel = new JLabel("Activities record:");
        targetLabel = new JLabel("Target (in minutes):");
        inputPanel = new JPanel(new GridBagLayout());
        recordPanel = new JPanel(new GridBagLayout());
        removePanel = new JPanel(new GridBagLayout());
        recordList = new JList(activities.getActivitiesRecord());
        scrollPane = new JScrollPane(recordList,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        String sep = System.getProperty("file.separator");
        hourglass = new ImageIcon(new ImageIcon(System.getProperty("user.dir") + sep
                + "data" + sep + "hourglass.png").getImage().getScaledInstance(
                100, 100, Image.SCALE_DEFAULT));
        iconLabel = new JLabel();
    }

    // REQUIRES: init()
    // MODIFIES: this
    // EFFECTS: set up the panels with a corresponding button
    public void setupPanel() {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        inputPanel.add(inputActivityLabel);
        c.gridx = 1;
        c.insets = new Insets(10, 10, 10, 10);
        inputPanel.add(activityName, c);
        c.gridx = 2;
        inputPanel.add(targetLabel, c);
        c.gridx = 3;
        inputPanel.add(target, c);
        c.gridx = 4;
        inputPanel.add(startButton, c);
        c.gridx = 5;
        inputPanel.add(stopButton, c);
        c.gridx = 0;
        c.gridy = 0;
        recordPanel.add(activitiesRecordLabel, c);
        c.gridy = 1;
        recordPanel.add(scrollPane, c);
        c.gridx = 1;
        recordPanel.add(iconLabel, c);
        removePanel.add(removeButton);
    }

    // inspired from https://www.codejava.net/java-se/swing/preventing-jframe-window-from-closing
    // REQUIRES: windowClosing()
    // EFFECTS: give option to save, no, and cancel when window is close
    public void closeOption(int closing) {
        switch (closing) {
            case JOptionPane.YES_OPTION:
                System.out.println("Saved and quit");
                saveActivities();
                dispose();
                break;

            case JOptionPane.NO_OPTION:
                System.out.println("Quit without saving");
                dispose();
                break;

            case JOptionPane.CANCEL_OPTION:
                System.out.println("Cancelled quiting");
                break;
        }
    }

    // REQUIRES: init()
    // MODIFIES: this
    // EFFECTS: add activity and update jlist on click
    public void respondToStart() {
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!activityName.getText().isEmpty() && !target.getText().isEmpty()) {
                    addActivity();
                    activities.getActivitiesRecord().addElement(activity.giveActivityDescription());
                }
                iconLabel.setIcon(hourglass);
                playSound("button-09a.wav");
            }
        });
    }

    // REQUIRES: init()
    // MODIFIES: this
    // EFFECTS: stop activity and update jlist on click
    public void respondToStop() {
        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!activityName.getText().isEmpty() && !target.getText().isEmpty()) {
                    stopActivity();
                    activities.getActivitiesRecord().addElement(activity.giveActivityDescription());
                }
                iconLabel.setIcon(null);
                playSound("button-7.wav");
            }
        });
    }

    // REQUIRES: init()
    // MODIFIES: this
    // EFFECTS: stop activity, update jlist on click, remove activity from activities
    public void respondToRemove() {
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String valToDelete = (String) recordList.getSelectedValue();
                String subString = valToDelete.substring(valToDelete.indexOf("ID:") + 4);
                int result = Integer.parseInt(subString.substring(0, subString.indexOf("     ")));

                activities.getActivitiesRecord().removeElement(recordList.getSelectedValue());
                activities.removeActivity(result);
                playSound("button-10.wav");
            }
        });
    }

    // REQUIRES: setupPanel()
    // MODIFIES: this
    // EFFECTS: set up the frame
    public void setupFrame() {
        add(inputPanel, BorderLayout.NORTH);
        add(recordPanel, BorderLayout.CENTER);
        add(removeButton, BorderLayout.SOUTH);
//        add(iconPanel, BorderLayout.EAST);
        setTitle("Hourglass");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setVisible(true);
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
    }

    // EFFECTS: add Activity to the Activities
    public void addActivity() {
        String name = activityName.getText();
        int deadline = Integer.parseInt(target.getText());
        activity = new Activity(name);
        activity.start();
        activity.setDeadline(deadline);
        activity.setOngoing();
        activities.addActivity(activity);

    }

    // MODIFIES: this
    // EFFECTS: stop activity when button is pressed
    public void stopActivity() {
        try {
            String prev = activity.giveActivityDescription();
            activities.getActivitiesRecord().removeElement(prev);
            activity.stop();
            try {
                if (activity.isOvertime()) {
                    activity.setOvertime();
                } else {
                    activity.setFinished();
                }
            } catch (TimerException e) {
                System.out.println("Haven't started the activity");
            }
            saveActivities();
        } catch (NullPointerException e) {
            System.out.println("Please input activity");
        }

    }

    // MODIFIES: this
//    // EFFECTS: add activity string to activitiesRecord
//    public void addActivityToArray(String desc) {
//        String[] moreActivities = new String[activitiesRecord.length];
//
//        for (int i = 0; i < activitiesRecord.length; i++) {
//            moreActivities[i + 1] = activitiesRecord[i];
//        }
//        moreActivities[0] = desc;
//        activitiesRecord = moreActivities;
//    }


    // EFFECTS: saves the activities to file
    private void saveActivities() {
        try {
            jsonWriter.open();
            jsonWriter.write(activities);
            jsonWriter.close();
            System.out.println("Saved " + activities.getName() + " to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    // EFFECTS: print loading status
    private String loadStatus() {
        try {
            jsonReader.read();
            return "Loaded " + activities.getName() + JSON_STORE;
        } catch (IOException e) {
            return "Unable to read from file: " + JSON_STORE;
        }
    }

    // MODIFIES: this
    // EFFECTS: loads activities from file
    private void loadActivities() {
        try {
            activities = jsonReader.read();
            System.out.println("Loaded " + activities.getName() + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }

    // inspired from http://suavesnippets.blogspot.com/2011/06/add-sound-on-jbutton-click-in-java.html
    // EFFECTS: play sound with the given sound name
    public void playSound(String soundName) {
        try {
            String sep = System.getProperty("file.separator");
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(
                    System.getProperty("user.dir") + sep + "data" + sep + soundName));
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (Exception ex) {
            System.out.println("Error with playing sound.");
        }
    }

}



