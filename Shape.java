/*
*  Shape: See ShapeDemo for an example how to use this class.
*
*/
import java.util.*;
import java.awt.*;
import java.awt.geom.*;
import javax.vecmath.*;

// simple shape model class
class Shape {
    int id;
    // shape points
    ArrayList<Point2d> points;
    int maxX, maxY, minX, minY, centerX, centerY; // bounding box
    AffineTransform AT;
    Shape(int i){
        id = i;
    }


    public int getID(){
        return id;
    }

    //get number of points
    public int getLength(){
        return points.size();
    }

    public void clearPoints() {
        points = new ArrayList<Point2d>();
        pointsChanged = true;
    }
  
    // add a point to end of shape
    public void addPoint(Point2d p) {
        if (points == null) clearPoints();
        points.add(p);
        pointsChanged = true;
    }    

    // add a point to end of shape
    public void addPoint(double x, double y) {
        addPoint(new Point2d(x, y));  
    }

    public int npoints() {
        return points.size();
    }

    // shape is polyline or polygon
    Boolean isClosed = false; 

    public Boolean getIsClosed() {
        return isClosed;
    }

    public void setIsClosed(Boolean isClosed) {
        this.isClosed = isClosed;
    }    

    // if polygon is filled or not
    Boolean isFilled = false; 

    public Boolean getIsFilled() {
        return isFilled;
    }

    public void setIsFilled(Boolean isFilled) {
        this.isFilled = isFilled;
    }

    // if polyline is selected or not
    Boolean isSelected = false;

    public Boolean getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(Boolean isSelected) {
        this.isSelected = isSelected;
    }

    // drawing attributes
    Color colour = Color.BLACK;
    float strokeThickness = 3.0f;

    public Color getColour() {
		return colour;
	}

	public void setColour(Color colour) {
		this.colour = colour;
	}

    public float getStrokeThickness() {
		return strokeThickness;
	}

	public void setStrokeThickness(float strokeThickness) {
		this.strokeThickness = strokeThickness;
	}

    // shape's transform

    // quick hack, get and set would be better
    float scale = 1.0f; // scaling index
    public float getScale(){return scale;}
    public void setScale(float s){ scale = s; }

    double theta = 0; // rotation angle
    public double getTheta(){return theta;}
    public void setTheta(double t){theta = Math.toRadians(t);}
    // some optimization to cache points for drawing
    Boolean pointsChanged = false; // dirty bit
    int[] xpoints, ypoints;
    int npoints = 0;

    void cachePointsArray() {
        xpoints = new int[points.size()];
        ypoints = new int[points.size()];
        for (int i=0; i < points.size(); i++) {
            xpoints[i] = (int)points.get(i).x;
            ypoints[i] = (int)points.get(i).y;
        }
        maxX = Arrays.stream(xpoints).max().getAsInt(); // calculate the bounding box
        minX = Arrays.stream(xpoints).min().getAsInt();
        maxY = Arrays.stream(ypoints).max().getAsInt();
        minY = Arrays.stream(ypoints).min().getAsInt();
        centerX = minX + ((maxX - minX) / 2);
        centerY = minY + ((maxY - minY) / 2);

        npoints = points.size();
        pointsChanged = false;
    }
	
    
    // let the shape draw itself
    // (note this isn't good separation of shape View from shape Model)
    public void draw(Graphics2D g2) {
        // don't draw if points are empty (not shape)
        if (points == null) return;

        // see if we need to update the cache
        if (pointsChanged) {
            cachePointsArray();
        }

        // save the current g2 transform matrix

        AffineTransform M = g2.getTransform();

        // multiply in this shape's transform
        // (uniform scale)
        g2.translate(centerX, centerY);
        g2.scale(scale, scale);
        g2.translate(-centerX, -centerY);
        g2.rotate(theta, centerX, centerY);

        // call drawing functions
        g2.setColor(colour);
        if (isFilled) {
            g2.fillPolygon(xpoints, ypoints, npoints);
        } else {
            // can adjust stroke size using scale
            g2.setStroke(new BasicStroke(strokeThickness / scale));
            if (isClosed) {
                g2.drawPolygon(xpoints, ypoints, npoints);
            }
            if (isSelected) {
                g2.setColor(Color.YELLOW);
                g2.setStroke(new BasicStroke(6.0f / scale));
                g2.drawPolyline(xpoints, ypoints, npoints);
            }
            g2.setColor(Color.BLACK);
            g2.setStroke(new BasicStroke(strokeThickness / scale));
            g2.drawPolyline(xpoints, ypoints, npoints);


        }
        // reset the transform to what it was before we drew the shape
        g2.setTransform(M);
        AT = M;
    }




    
   
    // let shape handle its own hit testing
    // (x,y) is the point to test against
    // (x,y) needs to be in same coordinate frame as shape, you could add
    // a panel-to-shape transform as an extra parameter to this function
    // (note this isn't good separation of shape Controller from shape Model)    
    public boolean hittest(double x, double y)
    {
        boolean trueFlag;
        trueFlag = false;
    	if (points != null) {
            int L = this.getLength();
            for(int i = 0; i < L;  i = i + 1){
                try {
                    Point2d p1 = points.get(i);
                    Point2D pm = new Point((int) x, (int) y);
                    Point2D p3 = new Point((int) p1.getX(), (int) p1.getY());
                     AT.inverseTransform(p3, pm);
                    double distance = Math.hypot(p3.getX()-x, p3.getY()-y);
                    if (distance <= 5){
                        setIsSelected(true);
                        trueFlag = true;
                    }
                }
                catch(NoninvertibleTransformException e){
                    System.out.print("NoninvertibleTransformException");
                }

            }
    	}
    	if (!trueFlag){
    	    setIsSelected(false);
        }
    	return trueFlag;
    }

}
