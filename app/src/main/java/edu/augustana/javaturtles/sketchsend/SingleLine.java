package edu.augustana.javaturtles.sketchsend;

import android.graphics.Point;
import java.util.LinkedList;

/*
-Class for single Line class
-Creates color, width, and a linked list of points variables.
 */
public class SingleLine {
    private int color;
    private int width;
    private LinkedList<Point> pointsOnLine;

    /*
    -Constructor for Single Line
    -Initializes color, width, and first point in linked list
     */
    public SingleLine(int color, int width, Point start){
        this.color=color;
        this.width=width;
        pointsOnLine=new LinkedList<>();
        pointsOnLine.add(start);
    }

    /*
    -Another Single Line Constructor
    -initializes color, width, and all points on the line
     */
    public SingleLine(int color, int width, LinkedList<Point> pointsOnLine){
        this.color=color;
        this.width=width;
        this.pointsOnLine=pointsOnLine;
    }

    /*
    adds another point to the linked list
     */
    public void add(Point next){
        pointsOnLine.add(next);
    }

    /*
    -gets screenScaler, which is relation of w/h of phone that sent drawing
    and w/h of phone that is receiving drawing
    -multiplies line width by scaler
    -returns resized single line
     */
    public SingleLine resizeLine(double screenScalar){
        width=(int) (width*screenScalar);
        //makes sure minimum size reqs are met
        if(width<2){
            width=2;
        }
        //scales each point in linked list to scaler
        for(int i=0; i<pointsOnLine.size(); i++){
            Point oldPoint=pointsOnLine.get(i);
            Point newPoint=new Point((int) (oldPoint.x*screenScalar), (int) (oldPoint.y * screenScalar));
            pointsOnLine.set(i,newPoint);
        }
        return new SingleLine(color,width,pointsOnLine);
    }

    //returns color of line
    public int getColor(){return color;}

    //returns width of line
    public int getWidth(){return width;}

    //returns linked list of points for line
    public LinkedList<Point> getLine(){return pointsOnLine;}

    //gets first point in linked list and returns it
    public Point getPoint(){
        Point point=pointsOnLine.pollFirst();
        return point;
    }

    //@param index - int value used for specific position in linkedlist
    //returns point at specific index
    public Point getPoint(int index){
        return pointsOnLine.get(index);
    }

    //returns size of linkedlist
    public int getSize(){return pointsOnLine.size();}

}
