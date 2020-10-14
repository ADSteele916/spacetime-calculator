package ui;

import model.Event;
import model.ReferenceFrame;
import model.RelativeFrame;
import model.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static model.World.ALLOWED_DELTA;

public class SpacetimeApp {
    private World world;
    private Scanner input;

    // EFFECTS: set up and run the spacetime application
    public SpacetimeApp() {
        world = new World();
        input = new Scanner(System.in);
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
        System.out.println("\t af: Add a reference frame");
        System.out.println("\t rf: Remove a reference frame");
        System.out.println("\t ae: Add an event");
        System.out.println("\t re: Remove an event");
        System.out.println("\t ve: View all events from a certain frame");
        System.out.println("\t li: Calculate the Lorentz Invariant of two events");
        System.out.println("\t exit: Exit the program");
    }

    private void execute(String command) {
        if (command.equals("af")) {
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
            if (world.getEvents().isEmpty()) {
                System.out.println("You don't have any events to get the Lorentz Invariant of!");
            } else {
                lorentzInvariant();
            }
        } else {
            System.out.println("Command not recognized.");
        }
    }

    private void addFrame() {
        String name = selectString("What is the name of this frame?");
        ReferenceFrame frame = selectFrame("What frame are you defining this frame relative to?");
        double v = selectDouble("How fast is this frame moving (as a fraction of c)?", -1, 1);
        frame.boost(name, v);
        System.out.println("Frame added!");
    }

    private void addEvent() {
        String name = selectString("What is the name of this event?");
        ReferenceFrame frame = selectFrame("Which frame would you like to define the event in?");
        double time = selectDouble("When does the event occur (in seconds)?");
        double x = selectDouble("Where does the event occur (in light-seconds)?");
        world.addEvent(new Event(name, time, x, frame));
        System.out.println("Event added!");
    }

    private void removeFrame() {
        RelativeFrame frame = selectRelativeFrame("Which frame would you like to delete?");
        world.getMasterFrame().removeRelativeFrame(frame);
    }

    private void removeEvent() {
        Event event = selectEvent("Which event would you like to delete?");
        world.removeEvent(event);
    }

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

    private String selectString(String prompt) {
        System.out.println("\n" + prompt);
        return input.next();
    }

    private double selectDouble(String prompt) {
        System.out.println("\n" + prompt);
        return input.nextDouble();
    }

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
