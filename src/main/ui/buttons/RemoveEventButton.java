package ui.buttons;

import ui.SpacetimeGUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Represents a button that removes an event from the world
public class RemoveEventButton extends Button {

    // EFFECTS: constructs an RemoveEventButton with given ui and parent component
    public RemoveEventButton(SpacetimeGUI ui, JComponent parent) {
        super(ui, parent);
    }

    @Override
    protected void createButton() {
        button = new JButton("Remove Event");
    }

    @Override
    protected void addListener() {
        button.addActionListener(new RemoveEventButtonClickHandler());
    }

    private class RemoveEventButtonClickHandler implements ActionListener {

        // EFFECTS: plays a sound and prompts the user to remove an event
        @Override
        public void actionPerformed(ActionEvent e) {
            ui.buttonNoise();
            ui.removeEvent();
        }
    }
}
