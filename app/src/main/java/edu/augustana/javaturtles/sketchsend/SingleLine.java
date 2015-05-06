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

    //Code from http://stackoverflow.com/questions/4841952/convert-arraylistmycustomclass-to-jsonarray
    public JSONObject toJSON(){
        Gson gson=new Gson();
        JSONObject finishedLine=new JSONObject();
        String _color=color+"";
        String _width=width+"";
        gson.toJson(width);
        try {
            finishedLine.put("Color", _color);
            finishedLine.put("Width",_width);
            JSONArray coordinates=new JSONArray();
            JSONObject thisPoint=new JSONObject();
            for (int i=0; i < pointsOnLine.size(); i++) {
                thisPoint.put("Point", pointsOnLine.get(i).toString());
                coordinates.put(thisPoint);
            }
            finishedLine.put("Points",coordinates);

        }catch(JSONException e){
            e.printStackTrace();
        }
        return finishedLine;
    }
}
