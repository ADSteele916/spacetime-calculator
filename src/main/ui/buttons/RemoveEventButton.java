package ui.buttons;

import ui.SpacetimeGUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RemoveEventButton extends Button {

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

        @Override
        public void actionPerformed(ActionEvent e) {
            ui.buttonNoise();
            ui.removeEvent();
        }
    }
}
