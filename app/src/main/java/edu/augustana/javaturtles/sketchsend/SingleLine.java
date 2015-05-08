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
    private ArrayList<Point> pointsOnLine;

    public SingleLine(int color, int width, Point start){
        this.color=color;
        this.width=width;
        pointsOnLine=new ArrayList<>();
        pointsOnLine.add(start);
    }

    public void add(Point next){
        pointsOnLine.add(next);
    }

    public int getColor(){return color;}

    public int getWidth(){return width;}

    public List<Point> getLine(){return pointsOnLine;}

    public int getSize(){return pointsOnLine.size();}

}
