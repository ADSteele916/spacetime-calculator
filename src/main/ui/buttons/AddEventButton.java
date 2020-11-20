package ui.buttons;

import ui.SpacetimeGUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Represents a button that adds an event to the world
public class AddEventButton extends Button {

    // EFFECTS: constructs an AddEventButton with given ui and parent component
    public AddEventButton(SpacetimeGUI ui, JComponent parent) {
        super(ui, parent);
    }

    @Override
    protected void createButton() {
        button = new JButton("Add Event");
    }

    @Override
    protected void addListener() {
        button.addActionListener(new AddEventButtonClickHandler());
    }

    private class AddEventButtonClickHandler implements ActionListener {

        // EFFECTS: plays a sound and prompts the user to add an event
        @Override
        public void actionPerformed(ActionEvent e) {
            ui.buttonNoise();
            ui.addEvent();
        }
    }
}
