package ui.buttons;

import ui.SpacetimeGUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Represents a button that removes a frame from the world
public class RemoveFrameButton extends Button {

    // EFFECTS: constructs an RemoveFrameButton with given ui and parent component
    public RemoveFrameButton(SpacetimeGUI ui, JComponent parent) {
        super(ui, parent);
    }

    @Override
    protected void createButton() {
        button = new JButton("Remove Frame");
    }

    @Override
    protected void addListener() {
        button.addActionListener(new RemoveFrameButtonClickHandler());
    }

    private class RemoveFrameButtonClickHandler implements ActionListener {

        // EFFECTS: plays a sound and prompts the user to remove a frame
        @Override
        public void actionPerformed(ActionEvent e) {
            ui.buttonNoise();
            ui.removeFrame();
        }
    }
}
