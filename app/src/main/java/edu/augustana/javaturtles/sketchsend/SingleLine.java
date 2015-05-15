package edu.augustana.javaturtles.sketchsend;

import android.graphics.Point;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by logankruse11 on 4/21/2015.
 */
public class SingleLine {
    private int color;
    private int width;
    private LinkedList<Point> pointsOnLine;

    public SingleLine(int color, int width, Point start){
        this.color=color;
        this.width=width;
        pointsOnLine=new LinkedList<>();
        pointsOnLine.add(start);
    }

    public SingleLine(int color, int width, LinkedList<Point> pointsOnLine){
        this.color=color;
        this.width=width;
        this.pointsOnLine=pointsOnLine;
    }

    public void add(Point next){
        pointsOnLine.add(next);
    }

    public SingleLine resizeLine(double screenScalar){
        width=(int) (width*screenScalar);
        if(width<2){
            width=2;
        }
        for(int i=0; i<pointsOnLine.size(); i++){
            Point oldPoint=pointsOnLine.get(i);
            Point newPoint=new Point((int) (oldPoint.x*screenScalar), (int) (oldPoint.y * screenScalar));
            pointsOnLine.set(i,newPoint);
        }

        return new SingleLine(color,width,pointsOnLine);
    }

    public int getColor(){return color;}

    public int getWidth(){return width;}

    public LinkedList<Point> getLine(){return pointsOnLine;}

    public Point getPoint(){
        Point point=pointsOnLine.pollFirst();
        return point;
    }

    public Point getPoint(int index){
        return pointsOnLine.get(index);
    }

    public int getSize(){return pointsOnLine.size();}

}
