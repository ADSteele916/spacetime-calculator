package ui.models;

import javax.swing.*;

// An extended version of Swing's DefaultListSelectionModel without the capability to select items
public class NoSelectionModel extends DefaultListSelectionModel {

    // MODIFIES: this
    // EFFECTS: deselects all items in the list no matter the input
    @Override
    public void setSelectionInterval(int index0, int index1) {
        super.setSelectionInterval(-1, -1);
    }
}
