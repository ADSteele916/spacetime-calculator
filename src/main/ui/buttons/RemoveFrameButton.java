package ui.buttons;

import ui.SpacetimeGUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RemoveFrameButton extends Button {

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

        @Override
        public void actionPerformed(ActionEvent e) {
            ui.buttonNoise();
            ui.removeFrame();
        }
    }
}
