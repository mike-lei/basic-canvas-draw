
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class StatusbarView extends JPanel{
    int numStrokes = 0;
    private DrawingModel model;
    JLabel statusLabel;
    StatusbarView(int width, DrawingModel dm){
        model = dm;
        this.setLayout(new BorderLayout(2, 2));
        statusLabel = new JLabel(numStrokes + " Strokes");
        statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
        this.add(statusLabel);
        this.setBorder(BorderFactory.createEtchedBorder());
        this.setBackground(Color.LIGHT_GRAY);
        this.setPreferredSize(new Dimension(width, 20));
    }

    public void updateNumStrokes(int i){

        statusLabel.setText(i + " Strokes");
    }

    public void updateSelected(int i, int points, float scale, int rotation){
        statusLabel.setText(i + " Strokes"+", Selection ("+points+" points, scale: "+scale+", rotation "+rotation+")");
    }
}
