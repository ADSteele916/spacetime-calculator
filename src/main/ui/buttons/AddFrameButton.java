package ui.buttons;

import ui.SpacetimeGUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Represents a button that adds a frame to the world
public class AddFrameButton extends Button {

    // EFFECTS: constructs an AddFrameButton with given ui and parent component
    public AddFrameButton(SpacetimeGUI ui, JComponent parent) {
        super(ui, parent);
    }

    @Override
    protected void createButton() {
        button = new JButton("Add Frame");
    }

    @Override
    protected void addListener() {
        button.addActionListener(new AddFrameButtonClickHandler());
    }

    private class AddFrameButtonClickHandler implements ActionListener {

        // EFFECTS: plays a sound and prompts the user to add a frame
        @Override
        public void actionPerformed(ActionEvent e) {
            ui.buttonNoise();
            ui.addFrame();
        }
    }
}
