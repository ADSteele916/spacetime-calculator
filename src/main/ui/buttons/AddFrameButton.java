package ui.buttons;

import ui.SpacetimeGUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddFrameButton extends Button {

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

        @Override
        public void actionPerformed(ActionEvent e) {
            ui.buttonNoise();
            ui.addFrame();
        }
    }
}
