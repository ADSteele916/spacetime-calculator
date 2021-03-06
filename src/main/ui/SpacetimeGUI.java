package ui;

import model.Event;
import model.ReferenceFrame;
import model.RelativeFrame;
import model.World;
import model.exceptions.FasterThanLightException;
import model.exceptions.NameInUseException;
import persistence.JsonReader;
import persistence.JsonWriter;
import persistence.exceptions.InvalidDataException;
import ui.buttons.*;
import ui.exceptions.PopupCancelException;
import ui.models.NoSelectionModel;
import ui.models.TransformTableModel;

import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static model.World.ALLOWED_DELTA;

// A runnable graphical user interface for the Spacetime Calculator
public class SpacetimeGUI extends JFrame implements ListSelectionListener {

    private static final String JSON_STORE = "./data/world.json";
    private static final String BUTTON_SOUND = "./data/button.wav";
    private static final File BUTTON_SOUND_FILE = new File(BUTTON_SOUND).getAbsoluteFile();
    public static final int WIDTH = 800;
    public static final int HEIGHT = 450;

    private World world;
    private ReferenceFrame currentFrame;
    private JsonReader jsonReader;
    private JsonWriter jsonWriter;

    private DefaultListModel<ReferenceFrame> frameListModel;
    private JList<Event> eventList;
    private JList<ReferenceFrame> frameList;
    private DefaultListModel<Event> eventListModel;
    private TransformTableModel transformTableModel;
    private JPanel uiPanel;

    // EFFECTS: constructs and opens the GUI
    public SpacetimeGUI() {
        super("Spacetime Calculator");
        setupFields();
        setupGraphics();
        updateEvents();
    }

    // MODIFIES: this
    // EFFECTS: sets initial values for the SpacetimeGUI's fields.
    private void setupFields() {
        world = new World();
        currentFrame = world.getMasterFrame();
        jsonReader = new JsonReader(JSON_STORE);
        jsonWriter = new JsonWriter(JSON_STORE);
        uiPanel = new JPanel();
        uiPanel.setLayout(new BoxLayout(uiPanel, BoxLayout.PAGE_AXIS));
    }

    // MODIFIES: this
    // EFFECTS: instantiates the GUI
    private void setupGraphics() {
        setLayout(new BorderLayout());
        setMinimumSize(new Dimension(WIDTH, HEIGHT));
        add(uiPanel);
        setupTable();
        setupPanels();
        setupButtons();
        transformTableModel.setData(world.getEvents(), currentFrame);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: instantiates the GUI's table of event positions in spacetime
    private void setupTable() {
        transformTableModel = new TransformTableModel();
        JTable table = new JTable();
        table.setModel(transformTableModel);
        table.setFillsViewportHeight(true);

        JScrollPane tableScrollPane = new JScrollPane(table);
        JPanel tableArea = new JPanel();
        tableArea.setLayout(new GridLayout(1, 1));
        tableArea.add(tableScrollPane);
        uiPanel.add(tableScrollPane, BorderLayout.CENTER);
    }

    // MODIFIES: this
    // EFFECTS: instantiates the GUI's panels containing lists of events and frames
    private void setupPanels() {
        JPanel worldArea = new JPanel();
        worldArea.setLayout(new GridLayout(0, 2));
        worldArea.setPreferredSize(new Dimension(0, 300));
        uiPanel.add(worldArea, BorderLayout.NORTH);

        setupFrames();
        JScrollPane frameScrollPane = new JScrollPane(frameList);
        worldArea.add(frameScrollPane, BorderLayout.WEST);

        setupEvents();
        JScrollPane eventScrollPane = new JScrollPane(eventList);
        worldArea.add(eventScrollPane, BorderLayout.EAST);

    }

    // MODIFIES: this
    // EFFECTS: instantiates the GUI's list of frames
    private void setupFrames() {
        frameListModel = new DefaultListModel<>();
        frameListModel.addElement(world.getMasterFrame());
        frameList = new JList<>(frameListModel);
        frameList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        frameList.getSelectionModel().addListSelectionListener(this);
        frameList.setSelectedIndex(0);
    }

    // MODIFIES: this
    // EFFECTS: instantiates the GUI's list of events
    private void setupEvents() {
        eventListModel = new DefaultListModel<>();
        eventList = new JList<>(eventListModel);
        eventList.setSelectionModel(new NoSelectionModel());
    }

    // MODIFIES: this
    // EFFECTS: instantiates the GUI's buttons
    private void setupButtons() {
        JPanel buttonArea = new JPanel();
        buttonArea.setLayout(new GridLayout(3, 2));
        uiPanel.add(buttonArea, BorderLayout.SOUTH);

        new AddFrameButton(this, buttonArea);
        new AddEventButton(this, buttonArea);
        new RemoveFrameButton(this, buttonArea);
        new RemoveEventButton(this, buttonArea);
        new SaveButton(this, buttonArea);
        new LoadButton(this, buttonArea);
        new InvariantButton(this, buttonArea);
    }

    // MODIFIES: this
    // EFFECTS: updates transformTableModel's data
    private void updateTable() {
        transformTableModel.setData(world.getEvents(), currentFrame);
    }

    // MODIFIES: this
    // EFFECTS: clears and repopulates frameListModel with all of the world's reference frames
    private void updateFrames() {
        frameListModel.clear();
        for (ReferenceFrame frame: world.getAllFrames()) {
            frameListModel.addElement(frame);
        }
        updateTable();
    }

    // MODIFIES: this
    // EFFECTS: clears and repopulates eventListModel with all of the world's events
    private void updateEvents() {
        eventListModel.clear();
        for (Event event: world.getEvents()) {
            eventListModel.addElement(event);
        }
        updateTable();
    }

    // MODIFIES: this
    // EFFECTS: prompts the user to add a reference frame to the world
    public void addFrame() {
        try {
            String name = stringPrompt("What is the frame's name?");
            ReferenceFrame frame = framePrompt("What frame would you like to define this frame relative to?",
                    world.getAllFrames());
            String velocityQuestion = "How fast is " + name + " moving relative to " + frame + " (as a fraction of c)?";
            double velocity = doublePrompt(velocityQuestion);
            frame.boost(name, velocity);
        } catch (PopupCancelException e) {
            return;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "That's not a valid velocity!", "", JOptionPane.ERROR_MESSAGE);
            return;
        } catch (NameInUseException e) {
            JOptionPane.showMessageDialog(this,
                    "A reference frame with that name already exists!", "", JOptionPane.ERROR_MESSAGE);
            return;
        } catch (FasterThanLightException e) {
            JOptionPane.showMessageDialog(this,
                    "Nothing can move as quickly as light!", "", JOptionPane.ERROR_MESSAGE);
            return;
        }
        updateFrames();
    }

    // MODIFIES: this
    // EFFECTS: prompts the user to add an event to the world
    public void addEvent() {
        try {
            String name = stringPrompt("What is the frame's name?");
            String frameQuestion = "What frame would you like to define this event in?";
            ReferenceFrame frame = framePrompt(frameQuestion, world.getAllFrames());
            double t = doublePrompt("When does the event occur (in seconds)?");
            double x = doublePrompt("Where does the event occur (in light-seconds)?");
            world.addEvent(new Event(name, t, x, frame));
        } catch (PopupCancelException e) {
            return;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "That's not a valid input!", "", JOptionPane.ERROR_MESSAGE);
            return;
        }
        updateEvents();
    }

    // MODIFIES: this
    // EFFECTS: if the world has relative frames in it, prompts the user to select one to delete
    //          moves all events defined in the deleted frame to the masterFrame
    public void removeFrame() {
        if (!world.getRelativeFrames().isEmpty()) {
            List<ReferenceFrame> frames = new ArrayList<>(world.getRelativeFrames());
            try {
                RelativeFrame frame = (RelativeFrame) framePrompt("What frame would you like to delete?", frames);
                for (Event event : world.getEvents()) {
                    if (event.getFrame() == frame) {
                        event.changeFrame(world.getMasterFrame());
                    }
                }
                world.getMasterFrame().removeRelativeFrame(frame);
            } catch (PopupCancelException e) {
                return;
            }
            updateFrames();
        }
    }

    // MODIFIES: this
    // EFFECTS: if the world has events in it, prompts the user to select one to delete
    public void removeEvent() {
        if (!world.getEvents().isEmpty()) {
            try {
                Event event = eventPrompt("What event would you like to delete?", world.getEvents());
                world.removeEvent(event);
            } catch (PopupCancelException e) {
                return;
            }
            updateEvents();
        }
    }

    public void lorentzInvariant() {
        if (!world.getEvents().isEmpty()) {
            try {
                Event event1 = eventPrompt("What is the first event?", world.getEvents());
                Event event2 = eventPrompt("What is the second event?", world.getEvents());
                double invariant = event1.lorentzInvariant(event2);
                String out = "The Lorentz Invariant of " + event1.getName() + " and " + event2.getName() + " is "
                        + invariant + ". ";
                String separation;
                if (Math.abs(invariant) < ALLOWED_DELTA) {
                    separation = "null";
                } else if (invariant > 0) {
                    separation = "spacelike";
                } else {
                    separation = "timelike";
                }
                String separated = "This means that the two events are " + separation + " separated.";
                JOptionPane.showMessageDialog(this, out + separated, "",
                        JOptionPane.PLAIN_MESSAGE);
            } catch (PopupCancelException e) {
                return;
            }
        }
    }

    // EFFECTS: prompts the user to input a string
    //          throws PopupCancelException if the user presses "Cancel"
    private String stringPrompt(String prompt) throws PopupCancelException {
        String input = JOptionPane.showInputDialog(prompt);
        if (input == null) {
            throw new PopupCancelException();
        }
        return input;
    }

    // EFFECTS: prompts the user to input a double
    //          throws PopupCancelException if the user presses "Cancel"
    //          throws NumberFormatException if the user enters an invalid input
    private double doublePrompt(String prompt) throws NumberFormatException, PopupCancelException {
        String input = stringPrompt(prompt);
        return Double.parseDouble(input);
    }

    // REQUIRES: !frameList.isEmpty()
    // EFFECTS: prompts the user to select a reference frame from the given list
    //          throws PopupCancelException if the user presses "Cancel"
    private ReferenceFrame framePrompt(String prompt, List<ReferenceFrame> frameList) throws PopupCancelException {
        ReferenceFrame[] frameArray = frameList.toArray(new ReferenceFrame[0]);
        ReferenceFrame frame = (ReferenceFrame) JOptionPane.showInputDialog(this, prompt, "",
                JOptionPane.PLAIN_MESSAGE, null, frameArray, frameArray[0]);
        if (frame == null) {
            throw new PopupCancelException();
        }
        return frame;
    }

    // REQUIRES: !eventList.isEmpty()
    // EFFECTS: prompts the user to select an event from the given list
    //          throws PopupCancelException if the user presses "Cancel"
    private Event eventPrompt(String prompt, List<Event> eventList) throws PopupCancelException {
        Event[] eventArray = eventList.toArray(new Event[0]);
        Event event = (Event) JOptionPane.showInputDialog(this, prompt, "",
                JOptionPane.PLAIN_MESSAGE, null, eventArray, eventArray[0]);
        if (event == null) {
            throw new PopupCancelException();
        }
        return event;
    }

    // EFFECTS: saves the world to file
    public void saveWorld() {
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
    public void loadWorld() {
        try {
            world = jsonReader.read();
            System.out.println("Loaded world from " + JSON_STORE);
        } catch (IOException | InvalidDataException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
        updateEvents();
        updateFrames();
    }

    // EFFECTS: plays a "click" noise
    public void buttonNoise() {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(BUTTON_SOUND_FILE);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
            e.printStackTrace();
        }
    }

    // MODIFIES: this
    // EFFECTS: if frameList is done updating, update currentFrame and the transformation table
    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            if (frameList.getSelectedIndex() == -1) {
                currentFrame = world.getMasterFrame();
                frameList.setSelectedIndex(0);
            } else {
                currentFrame = world.getAllFrames().get(frameList.getSelectedIndex());
            }
            updateTable();
        }
    }
}
