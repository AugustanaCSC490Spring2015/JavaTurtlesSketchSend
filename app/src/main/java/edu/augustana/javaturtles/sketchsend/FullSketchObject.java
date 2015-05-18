package edu.augustana.javaturtles.sketchsend;

import android.util.Log;

import java.util.LinkedList;

/**
 * Created by logankruse11 on 4/21/2015.
 */
public class FullSketchObject {
    private LinkedList<SingleLine> fullDrawing;
    private int sentScreenWidth;
    private int sentScreenHeight;

    //Constructor for a new FullSketchObject
    public FullSketchObject(int width, int height){
        fullDrawing=new LinkedList<>();
        this.sentScreenWidth=width;
        this.sentScreenHeight=height;
    }

    // Adds a line to the full drawing linked list
    public void add(SingleLine newLineObject){
        fullDrawing.add(newLineObject);
    }

    public void resize(double receiverWidth, double receiverHeight){
        double screenScalar=0;
        if(sentScreenHeight!=0 && sentScreenWidth!=0){
            double widthRatio=receiverWidth/sentScreenWidth;
            double heightRatio=receiverHeight/sentScreenHeight;
            screenScalar=Math.min(widthRatio,heightRatio);
        }else{
            screenScalar=1;
        }
        Log.w("FULL SKETCH OBJECT", "Sender w: "+sentScreenWidth+" Sender h: "+sentScreenHeight +" Receiver w: "+receiverWidth+" Receiver h: "+receiverHeight + " ScreenScalar: "+ screenScalar);
        for(int i=0; i<fullDrawing.size(); i++){
            fullDrawing.set(i,fullDrawing.get(i).resizeLine(screenScalar));
        }
    }

    // Returns the size of the drawing list
    public int getSize(){return fullDrawing. size();}

    //returns the color of the line at index i
    public int getIndexColor(int i){return fullDrawing.get(i).getColor();}

    //returns the line at index i
    public SingleLine getSingleLine(int i){return fullDrawing.get(i);}
}
