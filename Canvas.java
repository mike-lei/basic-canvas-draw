
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.geom.*;


// Create a simple form
// Converts adapter to anonymous inner class
public class A2Basic {
    public static void main(String[] args) {
        // create a window
        JFrame frame = new JFrame("A2Basic");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        // create a panel and add components
        JPanel panel = new JPanel();

        DrawingModel drawing = new DrawingModel();
        StatusbarView statusBar = new StatusbarView(frame.getWidth(),  drawing);
        ToolbarView toolBar = new ToolbarView(frame.getWidth(),  drawing);
        CanvasView canvas = new CanvasView(frame.getHeight(), frame.getWidth(), drawing);
        drawing.setModels(statusBar, toolBar, canvas);

        // add panel to the window
        frame.add(panel);
        frame.add(canvas);
        frame.add(toolBar, BorderLayout.NORTH);
        frame.add(statusBar, BorderLayout.SOUTH);

        // set window behaviour and display it
        frame.setResizable(true);
        frame.setSize(800, 600);
        frame.setVisible(true);
    }

}

