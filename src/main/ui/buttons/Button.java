package ui.buttons;

import ui.SpacetimeGUI;

import javax.swing.*;

public abstract class Button {

    protected JButton button;
    protected SpacetimeGUI ui;

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
