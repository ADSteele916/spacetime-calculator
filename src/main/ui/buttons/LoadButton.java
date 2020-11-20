package ui.buttons;

import ui.SpacetimeGUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Represents a button that loads a world from disk
public class LoadButton extends Button {

    // EFFECTS: constructs an LoadButton with given ui and parent component
    public LoadButton(SpacetimeGUI ui, JComponent parent) {
        super(ui, parent);
    }

    @Override
    protected void createButton() {
        button = new JButton("Load World");
    }

    @Override
    protected void addListener() {
        button.addActionListener(new LoadButtonClickHandler());
    }

    private class LoadButtonClickHandler implements ActionListener {

        // EFFECTS: plays a sound and loads a world from file
        @Override
        public void actionPerformed(ActionEvent e) {
            ui.buttonNoise();
            ui.loadWorld();
        }
    }
}
