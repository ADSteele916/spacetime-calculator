package ui;

import model.World;

import javax.swing.*;
import java.awt.*;

public class SpacetimeGUI extends JFrame {

    public static final int WIDTH = 800;
    public static final int HEIGHT = 450;

    private World world;

    public SpacetimeGUI() {
        super("Spacetime Visualizer");
        setupFields();
        setupGraphics();
    }

    private void setupFields() {
        world = new World();
    }

    private void setupGraphics() {
        setLayout(new BorderLayout());
        setMinimumSize(new Dimension(WIDTH, HEIGHT));
        setupButtons();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void setupButtons() {
        JPanel buttonArea= new JPanel();
        buttonArea.setLayout(new GridLayout(2, 2));
        add(buttonArea, BorderLayout.SOUTH);
    }

}
