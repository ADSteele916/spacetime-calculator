package ui.buttons;

import ui.SpacetimeGUI;

import javax.swing.*;

// Represents a button that is added to a parent component in a ui.
public abstract class Button {

    protected JButton button;
    protected SpacetimeGUI ui;

    // EFFECTS: Constructs a button with given ui, creates the button adds it to a parent, and adds a listener
    public Button(SpacetimeGUI ui, JComponent parent) {
        this.ui = ui;
        createButton();
        addToParent(parent);
        addListener();
    }

    // EFFECTS: creates button to activate tool
    protected abstract void createButton();

    // EFFECTS: adds a listener for this tool
    protected abstract void addListener();

    // MODIFIES: parent
    // EFFECTS:  adds the given button to the parent component
    public void addToParent(JComponent parent) {
        parent.add(button);
    }

}
