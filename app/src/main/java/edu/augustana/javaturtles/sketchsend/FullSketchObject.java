package edu.augustana.javaturtles.sketchsend;

import java.util.LinkedList;

/**
 * Created by logankruse11 on 4/21/2015.
 */
public class FullSketchObject {
    private LinkedList<SingleLine> fullDrawing;

    public FullSketchObject(){
        fullDrawing=new LinkedList<>();
    }

    public void add(SingleLine newLineObject){
        fullDrawing.add(newLineObject);
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

    public SingleLine getSingleLine(int i){return fullDrawing.get(i);}
}
