package ui.buttons;

import ui.SpacetimeGUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Represents a button that adds an event to the world
public class InvariantButton extends Button {

    // EFFECTS: constructs an AddEventButton with given ui and parent component
    public InvariantButton(SpacetimeGUI ui, JComponent parent) {
        super(ui, parent);
    }

    @Override
    protected void createButton() {
        button = new JButton("Calculate Lorentz Invariant");
    }

    @Override
    protected void addListener() {
        button.addActionListener(new InvariantButtonClickHandler());
    }

    private class InvariantButtonClickHandler implements ActionListener {

        // EFFECTS: plays a sound and prompts the user to calculate a Lorentz Invariant
        @Override
        public void actionPerformed(ActionEvent e) {
            ui.buttonNoise();
            ui.lorentzInvariant();
        }
    }
}
