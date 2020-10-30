package ui;

import model.*;
import persistence.exceptions.InvalidDataException;
import model.exceptions.NameInUseException;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static model.World.ALLOWED_DELTA;


// Structure of runApp, showCommands, and execute methods from TellerApp example
public class SpacetimeApp {
    private static final String JSON_STORE = "./data/world.json";
    private World world;
    private Scanner input;
    private JsonReader jsonReader;
    private JsonWriter jsonWriter;

    // EFFECTS: set up and run the spacetime application
    public SpacetimeApp() {
        world = new World();
        input = new Scanner(System.in);
        jsonReader = new JsonReader(JSON_STORE);
        jsonWriter = new JsonWriter(JSON_STORE);
        runApp();
    }

    // MODIFIES: this
    // EFFECTS: processes user input
    private void runApp() {
        boolean running = true;
        String command;

        while (running) {
            showCommands();
            command = input.next().toLowerCase();

            if (command.equals("exit")) {
                running = false;
            } else {
                execute(command);
            }
        }

        System.out.println("\nExiting.");
    }

    // EFFECTS: prints the commands available to the user to console
    private void showCommands() {
        System.out.println("\n Commands:");
        System.out.println("\t lf: List reference frames");
        System.out.println("\t af: Add a reference frame");
        System.out.println("\t rf: Remove a reference frame");
        System.out.println("\t le: List events");
        System.out.println("\t ae: Add an event");
        System.out.println("\t re: Remove an event");
        System.out.println("\t ve: View all events from a certain frame");
        System.out.println("\t li: Calculate the Lorentz Invariant of two events");
        System.out.println("\t s: Save the world to file");
        System.out.println("\t l: Load the world from file");
        System.out.println("\t exit: Exit the program");
    }

    // MODIFIES: this
    // EFFECTS: runs the appropriate helper method based on which command is inputted
    private void execute(String command) {
        if (command.equals("lf")) {
            listFrames();
        } else if (command.equals("le")) {
            listEvents();
        } else if (command.equals("af")) {
            addFrame();
        } else if (command.equals("ae")) {
            addEvent();
        } else if (command.equals("rf")) {
            removeFrame();
        } else if (command.equals("re")) {
            removeEvent();
        } else if (command.equals("ve")) {
            viewEvents();
        } else if (command.equals("li")) {
            lorentzInvariant();
        } else if (command.equals("s")) {
            saveWorld();
        } else if (command.equals("l")) {
            loadWorld();
        } else {
            System.out.println("Command not recognized.");
        }
    }

    // EFFECTS: lists out all available reference frames
    private void listFrames() {
        System.out.println("\nFrames:");
        MasterFrame master = world.getMasterFrame();
        System.out.println(master.getName() + " moving at " + master.getVelocity() + "c.");
        for (ReferenceFrame frame: world.getFrames()) {
            String speed = " moving at " + frame.getVelocity() + "c relative to " + master.getName() + ".";
            System.out.println(frame.getName() + speed);
        }
    }

    // EFFECTS: lists out all events
    private void listEvents() {
        System.out.println("\nEvents:");
        for (Event event: world.getEvents()) {
            String name = event.getName();
            String coords = " with x = " + event.getX() + " light seconds and t = " + event.getTime() + " seconds ";
            String frame = "in " + event.getFrame().getName() + ".";
            System.out.println(name + coords + frame);
        }
    }

    // MODIFIES: this
    // EFFECTS: creates a new frame with user-defined name relative velocity
    private void addFrame() {
        String name = selectString("What is the name of this frame?");
        ReferenceFrame frame = selectFrame("What frame are you defining this frame relative to?");
        double v = selectDouble("How fast is this frame moving (as a fraction of c)?", -1, 1);
        try {
            frame.boost(name, v);
            System.out.println("Frame added!");
        } catch (NameInUseException e) {
            e.printStackTrace();
            System.out.println("A frame with that name already exists!");
        }
    }

    // MODIFIES: this
    // EFFECTS: creates a new event with user-defined time, x, name, and frame
    private void addEvent() {
        String name = selectString("What is the name of this event?");
        ReferenceFrame frame = selectFrame("Which frame would you like to define the event in?");
        double time = selectDouble("When does the event occur (in seconds)?");
        double x = selectDouble("Where does the event occur (in light-seconds)?");
        world.addEvent(new Event(name, time, x, frame));
        System.out.println("Event added!");
    }

    // MODIFIES: this
    // EFFECTS: removes a given relative frame and moves all events defined in it to the masterFrame
    private void removeFrame() {
        RelativeFrame frame = selectRelativeFrame("Which frame would you like to delete?");
        for (Event event: world.getEvents()) {
            if (event.getFrame() == frame) {
                event.changeFrame(world.getMasterFrame());
            }
        }
        world.getMasterFrame().removeRelativeFrame(frame);
        String masterName = world.getMasterFrame().getName();
        System.out.println("Removed " + frame.getName() + "and moved all events defined in it to " + masterName + "!");
    }

    // MODIFIES: this
    // EFFECTS: removes a given event
    private void removeEvent() {
        Event event = selectEvent("Which event would you like to delete?");
        world.removeEvent(event);
        System.out.println("Removed " + event.getName() + "!");
    }

    // EFFECTS: shows the position and time of all events from the perspective of a a certain frame
    private void viewEvents() {
        ReferenceFrame frame = selectFrame("What frame would you like to view the events from?");
        List<Event> transformedEvents = new ArrayList<>();
        for (Event event: world.getEvents()) {
            transformedEvents.add(event.lorentzTransform(frame));
        }
        for (Event event: transformedEvents) {
            System.out.println(event.getName() + " occurs at t = " + event.getTime() + " and x = " + event.getX());
        }
    }

    // EFFECTS: calculates the Lorentz Invariant of two events, then says if they are
    //          spacelike, timelike, or null separated
    private void lorentzInvariant() {
        if (world.getEvents().isEmpty()) {
            System.out.println("You don't have any events to get the Lorentz Invariant of!");
        } else {
            Event event1 = selectEvent("What is the first event you want to compare?");
            Event event2 = selectEvent("What is the second event you want to compare?");
            double invariant = event1.lorentzInvariant(event2);
            String out = "The Lorentz Invariant of " + event1.getName() + " and " + event2.getName() + " is ";
            System.out.println(out + invariant);
            String separation;
            if (Math.abs(invariant) < ALLOWED_DELTA) {
                separation = "null";
            } else if (invariant > 0) {
                separation = "spacelike";
            } else {
                separation = "timelike";
            }
            System.out.println("This means that the two events are " + separation + " separated.");
        }
    }

    // EFFECTS: saves the world to file
    private void saveWorld() {
        try {
            jsonWriter.open();
            jsonWriter.write(world);
            jsonWriter.close();
            System.out.println("Saved world to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    // MODIFIES: this
    // EFFECTS: loads world from file
    private void loadWorld() {
        try {
            world = jsonReader.read();
            System.out.println("Loaded world from " + JSON_STORE);
        } catch (IOException | InvalidDataException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }

    // EFFECTS: prompts the user to select any ReferenceFrame in the world
    private ReferenceFrame selectFrame(String prompt) {
        System.out.println("\n" + prompt);
        System.out.println("(0) " + world.getMasterFrame().getName());
        for (int i = 0; i < world.getFrames().size(); i++) {
            RelativeFrame relativeFrame = world.getFrames().get(i);
            String listNumber = "(" + (i + 1) + ") ";
            String frameName = relativeFrame.getName();
            String speed = " moving at " + relativeFrame.getVelocity() + "c relative to the Stationary Frame";
            System.out.println(listNumber + frameName + speed);
        }
        int index = input.nextInt();

        if (index == 0) {
            return world.getMasterFrame();
        } else if ((index > 0) && (index <= world.getFrames().size())) {
            return world.getFrames().get(index - 1);
        } else {
            System.out.println("Invalid frame.");
            return selectFrame(prompt);
        }
    }

    // EFFECTS: prompts the user to select any RelativeFrame in the world
    private RelativeFrame selectRelativeFrame(String prompt) {
        System.out.println("\n" + prompt);
        for (int i = 0; i < world.getFrames().size(); i++) {
            RelativeFrame relativeFrame = world.getFrames().get(i);
            String listNumber = "(" + i + ") ";
            String frameName = relativeFrame.getName();
            String speed = " moving at " + relativeFrame.getVelocity() + "c relative to the Stationary Frame";
            System.out.println(listNumber + frameName + speed);
        }
        int index = input.nextInt();

        if ((index >= 0) && (index <= world.getFrames().size())) {
            return world.getFrames().get(index);
        } else {
            System.out.println("Invalid frame.");
            return selectRelativeFrame(prompt);
        }
    }

    // EFFECTS: prompts the user to select any Event in the world
    private Event selectEvent(String prompt) {
        System.out.println("\n" + prompt);
        for (int i = 0; i < world.getEvents().size(); i++) {
            Event event = world.getEvents().get(i);
            String listNumber = "(" + i + ") ";
            String eventDesc = event.getName() + " with x = " + event.getX() + " and t = " + event.getTime();
            String eventFrame = " in " + event.getFrame().getName();
            System.out.println(listNumber + eventDesc + eventFrame);
        }
        int index = input.nextInt();

        if ((index >= 0) && (index < world.getEvents().size())) {
            return world.getEvents().get(index);
        } else {
            System.out.println("Invalid event.");
            return selectEvent(prompt);
        }
    }

    // EFFECTS: prompts the user to input a string
    private String selectString(String prompt) {
        System.out.println("\n" + prompt);
        return input.next();
    }

    // EFFECTS: prompts the user to input a double
    private double selectDouble(String prompt) {
        System.out.println("\n" + prompt);
        return input.nextDouble();
    }

    // EFFECTS: prompts the user to input a double within the given bounds
    private double selectDouble(String prompt, double min, double max) {
        System.out.println("\n" + prompt);
        double value = input.nextDouble();
        if (value > min && value < max) {
            return value;
        } else {
            return selectDouble(prompt, min, max);
        }
    }
}
