package edu.augustana.javaturtles.sketchsend;

import android.graphics.Point;

import java.util.LinkedList;

/**
 * Created by logankruse11 on 4/21/2015.
 */
public class SingleLine {
    private final String test="T";
    private int color;
    private int width;
    private LinkedList<Point> pointsOnLine;

    public SingleLine(int color, int width, Point start){
        this.color=color;
        this.width=width;
        pointsOnLine=new LinkedList<>();
        pointsOnLine.add(start);
    }

    public void add(Point next){
        pointsOnLine.add(next);
    }

    public int getColor(){return color;}

    public int getWidth(){return width;}

    public LinkedList<Point> getLine(){return pointsOnLine;}

    public int getSize(){return pointsOnLine.size();}
}
