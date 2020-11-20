package ui.buttons;

import ui.SpacetimeGUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SaveButton extends Button {

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

        @Override
        public void actionPerformed(ActionEvent e) {
            ui.buttonNoise();
            ui.saveWorld();
        }
    }
}
