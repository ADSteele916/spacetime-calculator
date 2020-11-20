package ui.buttons;

import ui.SpacetimeGUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Represents a button that saves a world to disk
public class SaveButton extends Button {

    // EFFECTS: constructs an SaveButton with given ui and parent component
    public SaveButton(SpacetimeGUI ui, JComponent parent) {
        super(ui, parent);
    }

    @Override
    protected void createButton() {
        button = new JButton("Save World");
    }

    @Override
    protected void addListener() {
        button.addActionListener(new SaveButtonClickHandler());
    }

    private class SaveButtonClickHandler implements ActionListener {

        // EFFECTS: plays a sound and saves the world to file
        @Override
        public void actionPerformed(ActionEvent e) {
            ui.buttonNoise();
            ui.saveWorld();
        }
    }
}
