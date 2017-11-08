import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;

import javax.vecmath.*;

class CanvasView extends JComponent{
    DrawingModel model;
    Shape shape;
    private DrawingModel drawingModel;
    boolean dragged;

    CanvasView (int width, int height, DrawingModel dm){
        model = dm;
        this.setPreferredSize(new Dimension(width, height));

        this.addMouseListener(new MouseAdapter(){
            public void mousePressed(MouseEvent e) {
                model.hit_test(e.getX(),e.getY());
                dragged = false;
                shape = new Shape(model.getIDCount());
                model.incrementIDCount();
                model.newStroke(shape);

            }

            public void mouseReleased(MouseEvent e){
                if (dragged){
                    model.setTotalStroke(model.getTotalStroke() + 1);
                    model.notifyStatusbar();
                }
                // change shape type
                // shape.setIsClosed(true);
                // shape.setIsFilled(true);
                shape.setColour(Color.BLACK);

                // try setting scale to something other than 1
                shape.scale = 1.0f;

                repaint();
            }
        });

        this.addMouseMotionListener(new MouseAdapter(){
            public void mouseDragged(MouseEvent e) {
                dragged = true;
                shape.addPoint(e.getX(), e.getY());
                repaint();
            }
        });
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g; // cast to get 2D drawing methods
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,  // antialiasing look nicer
                RenderingHints.VALUE_ANTIALIAS_ON);

        int L = model.getLength();
        for(int i = 0; i < L;  i = i + 1){
            Shape s = model.strokeList.get(i);
            if (s != null){
                s.draw(g2);
            }
        }
    }

    public void rp(){
        repaint();
    }


}