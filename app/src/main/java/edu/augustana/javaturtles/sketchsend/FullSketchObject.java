package edu.augustana.javaturtles.sketchsend;

import java.util.LinkedList;

/**
 * Created by logankruse11 on 4/21/2015.
 */
public class FullSketchObject {
    private LinkedList<SingleLineObject> fullDrawing;

    public FullSketchObject(){
        fullDrawing=new LinkedList<>();
    }

    public void add(SingleLineObject newLineObject){
        fullDrawing.add(newLineObject);
    }

    public void undo(){
        fullDrawing.remove();
    }

    public void clear(){
        fullDrawing.clear();
    }

    public LinkedList<SingleLineObject> getFullDrawing(){return fullDrawing;}

    public int getSize(){return fullDrawing.size();}
}
