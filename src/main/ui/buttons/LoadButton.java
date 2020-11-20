package ui.buttons;

import ui.SpacetimeGUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoadButton extends Button {

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

        @Override
        public void actionPerformed(ActionEvent e) {
            ui.buttonNoise();
            ui.loadWorld();
        }
    }
}
