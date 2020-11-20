package ui.models;

import model.Event;
import model.ReferenceFrame;

import javax.swing.table.AbstractTableModel;
import java.util.List;

// A table model representing a table displaying the positions of a list of events in spacetime
public class TransformTableModel extends AbstractTableModel {

    private static final String[] columnNames = {"Event Name", "Time (seconds)", "Position (light-seconds)"};
    private Object[][] data;

    // MODIFIES: this
    // EFFECTS: sets the data in the table to the given events' names and spacetime coordinates in the given frame
    public void setData(List<Event> events, ReferenceFrame frame) {
        data = new Object[events.size()][3];
        for (int i = 0; i < events.size(); i++) {
            Event event = events.get(i);
            String name = event.getName();
            Event transformedEvent = event.lorentzTransform(frame);
            double time = transformedEvent.getTime();
            double posX = transformedEvent.getX();
            data[i][0] = name;
            data[i][1] = time;
            data[i][2] = posX;
        }
        fireTableDataChanged();
    }

    // EFFECTS: returns the name of a given column
    @Override
    public String getColumnName(int col) {
        return columnNames[col];
    }

    // EFFECTS: returns the number of rows in the table
    @Override
    public int getRowCount() {
        return data.length;
    }

    // EFFECTS: returns the number of columns in the table
    @Override
    public int getColumnCount() {
        return 3;
    }

    // EFFECTS: returns the value contained in a given cell
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return data[rowIndex][columnIndex];
    }

}
