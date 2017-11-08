
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.*;

class ToolbarView extends JPanel{
    DrawingModel model;
    JLabel scaleSliderValve = new JLabel("1.0");
    JLabel rotateSliderValve = new JLabel("0");
    scaleSlider scaleSlider_obj;
    rotateSlider rotateSlider_obj;
    deleteButton deleteButton_obj;
    public ToolbarView (int width, DrawingModel dm){
        model = dm;
        this.setPreferredSize(new Dimension(width, 50));
        this.setBorder(BorderFactory.createEtchedBorder());
        this.setBackground(Color.LIGHT_GRAY);
        this.setLayout(new FlowLayout(FlowLayout.LEFT));

        //Create delete button
        deleteButton_obj = new deleteButton(dm);

        //Create scale slider
        JLabel scaleSliderLabel = new JLabel("Scale");
        scaleSliderLabel.setBorder(BorderFactory.createEmptyBorder(0,50,0,0));
        scaleSlider_obj = new scaleSlider(5,20,10, model);

        //Create rotate slider
        JLabel rotateSliderLabel = new JLabel("Rotate");
        rotateSlider_obj = new rotateSlider(-180,180,0, model);
        rotateSliderLabel.setBorder(BorderFactory.createEmptyBorder(0,50,0,0));


        this.add(deleteButton_obj);
        this.add(scaleSliderLabel);
        this.add(scaleSlider_obj);
        this.add(scaleSliderValve);
        this.add(rotateSliderLabel);
        this.add(rotateSlider_obj);
        this.add(rotateSliderValve);
    }


    class scaleSlider extends JSlider implements ChangeListener{
        double preciseScale;
        DrawingModel model;
        scaleSlider(int min, int max, int val, DrawingModel dm){
            model = dm;
           this.addChangeListener(this);
           this.setMinimum(min);
           this.setValue(val);
           this.setMaximum(max);
           this.setPreferredSize(new Dimension(180,30));
           this.setEnabled(false);
        }
        public void stateChanged(ChangeEvent e) {
            JSlider source = (JSlider)e.getSource();

                int value = (int)source.getValue();
                    preciseScale = (double)value / 10;
                    notifyLabelChange(1, preciseScale); //change slider label of scaleSlider

                int L = model.getLength();
                for(int i = 0; i < L;  i = i + 1){
                    Shape s = model.strokeList.get(i);
                    if (s.getIsSelected()){
                        s.setScale((float)preciseScale);
                        model.notifyCanvasview();
                        break;
                    }
                }
        }
        //public void setEnable(boolean status){
        //    if (status) {this.setEnabled(true);} else {this.setEnabled(false);}
       // }
    }

    class rotateSlider extends JSlider implements ChangeListener{
        int value;
        DrawingModel model;
        rotateSlider(int min, int max, int val, DrawingModel dm){
            model = dm;
            this.addChangeListener(this);
            this.setMinimum(min);
            this.setValue(val);
            value = val;
            this.setMaximum(max);
            this.setPreferredSize(new Dimension(180,30));
            this.setEnabled(false);
        }
        public void stateChanged(ChangeEvent e) {
            JSlider source = (JSlider)e.getSource();

                value = (int)source.getValue();
                notifyLabelChange(2, value); //change slider label of rotateSlider

            int L = model.getLength();
            for(int i = 0; i < L;  i = i + 1){
                Shape s = model.strokeList.get(i);
                if (s.getIsSelected()){
                    s.setTheta((double)value);
                    model.notifyCanvasview();
                    break;
                }
            }
        }
        //public void setEnable(boolean status){
        //    if (status) {this.setEnabled(true);} else {this.setEnabled(false);}
        //}
    }

    public void notifyLabelChange(int slider, double value){
        switch (slider){
            case 1:
                float f = (float)value;
                scaleSliderValve.setText(Float.toString(f));
                break;
            case 2:
                int v = (int) value;
                rotateSliderValve.setText(Integer.toString(v));
                break;
            default:
                break;
        }
    }

    public void notifySliderChange(int slider, int value){
        switch (slider){
            case 1:
                scaleSlider_obj.setValue(value);
                break;
            case 2:
                rotateSlider_obj.setValue(value);
                break;
            default:
                break;
        }
    }

    public void notifyStatusChange(boolean status){
        scaleSlider_obj.setEnabled(status);
        rotateSlider_obj.setEnabled(status);
        deleteButton_obj.setEnabled(status);
    }

    class deleteButton extends JButton{
        DrawingModel model;
        deleteButton(DrawingModel dm){
            this.setEnabled(false);
            model = dm;
            this.setText("Delete");
            this.addMouseListener(new MouseAdapter(){
                public void mousePressed(MouseEvent e){
                            model.deleteStroke();
                }
            });
        }
    }
}
