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

    public FullSketchObject(int width, int height){
        fullDrawing=new LinkedList<>();
        this.sentScreenWidth=width;
        this.sentScreenHeight=height;
    }

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
            Log.w("FULL SKETCH OBJECT", "Resizing the line loop");
            fullDrawing.set(i,fullDrawing.get(i).resizePoints(screenScalar));
        }
    }

    public void undo(){
        fullDrawing.remove();
    }

    public void clear(){
        fullDrawing.clear();
    }

    public LinkedList<SingleLine> getFullDrawing(){return fullDrawing;}

    public int getSize(){return fullDrawing. size();}

    public int getIndexColor(int i){return fullDrawing.get(i).getColor();}

    public int getSentScreenWidth(){return sentScreenWidth;}

    public int getSentScreenHeight(){return sentScreenHeight;}

    public SingleLine getSingleLine(int i){return fullDrawing.get(i);}
}
