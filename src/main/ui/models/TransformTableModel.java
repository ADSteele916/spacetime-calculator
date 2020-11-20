package ui.models;

import model.Event;
import model.ReferenceFrame;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class TransformTableModel extends AbstractTableModel {

    private static final String[] columnNames = {"Event Name", "Time (seconds)", "Position (light-seconds)"};
    private Object[][] data;

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

    @Override
    public String getColumnName(int col) {
        return columnNames[col];
    }

    @Override
    public int getRowCount() {
        return data.length;
    }

    @Override
    public int getColumnCount() {
        return 3;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return data[rowIndex][columnIndex];
    }

}
