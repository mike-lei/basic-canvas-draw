/*
* CS 349 Java Code Examples
*
* ShapeDemo    Demo of Shape class: draw shapes using mouse.
*
*/
import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.util.*;
import javax.vecmath.*;

public class DrawingModel{
    ArrayList<Shape> strokeList;
    private StatusbarView sbv;
    private ToolbarView tbv;
    private CanvasView cv;
    int totalStroke = 0;
    int IDcount = 0;
    public DrawingModel(){
        strokeList = new ArrayList<Shape>();
    }

    public void setModels(StatusbarView statusbar, ToolbarView toolbar, CanvasView canvas){
        sbv = statusbar;
        tbv = toolbar;
        cv = canvas;
    }

    public void notifyStatusbar(){
        sbv.updateNumStrokes(totalStroke);
    }

    public void notifyCanvasview(){ // notify CanvasView to refresh.
        cv.rp();
    }

    public void newStroke(Shape s){
        strokeList.add(s);
    }

    public int getLength(){
        return strokeList.size();
    }

    public int getTotalStroke() {return totalStroke;}
    public void setTotalStroke(int i){totalStroke = i;}

    public int getIDCount(){return IDcount;}
    public void incrementIDCount(){IDcount = IDcount + 1;}


    public void deleteStroke(){
        int L = getLength();
        for(int i = 0; i < L;  i = i + 1){
            Shape s = strokeList.get(i);
            if (s.getIsSelected()){
               strokeList.remove(i);
               setTotalStroke(getTotalStroke()-1);
                notifyCanvasview();
                notifyStatusbar();
                break;
            }
        }
    }

    public void hit_test(double x, double y){
        int L = this.getLength();
        int hitID = -1;
        for(int i = 0; i < L;  i = i + 1){
            Shape s = strokeList.get(i);
            if (s.hittest(x,y)){
               hitID = s.getID();
                // if a stroke is selected, update the toolbar
                tbv.notifyStatusChange(true);
                tbv.notifyLabelChange(1, (double)s.getScale());
                tbv.notifyLabelChange(2, s.getTheta());
                tbv.notifySliderChange(1, (int)s.getScale()*10);
                tbv.notifySliderChange(2, (int)s.getTheta());
                sbv.updateSelected(totalStroke, s.getLength(), s.getScale(), (int)s.getTheta());
            }
        }
        for(int i = 0; i < L;  i = i + 1){ // only the top stroke will be selected
            Shape s = strokeList.get(i);
            if (s.getID() != hitID){
                s.setIsSelected(false);
            }
        notifyCanvasview();
        }
        if (hitID == -1){
            tbv.notifyStatusChange(false);
            notifyStatusbar();
        }
    }
}

