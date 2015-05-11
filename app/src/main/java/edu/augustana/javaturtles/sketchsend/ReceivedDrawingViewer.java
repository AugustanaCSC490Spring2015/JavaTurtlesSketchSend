package edu.augustana.javaturtles.sketchsend;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Handler;
import android.view.View;

import com.google.gson.Gson;


import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by logankruse11 on 5/8/2015.
 */
public class ReceivedDrawingViewer extends View {

    private static final String TAG = "SketchSendView"; // for Log.w(TAG, ...)

    private Activity receivedDrawingActivity; // keep a reference to the main Activity

    private Paint backgroundPaint;
    private Bitmap bitmap;
    private Canvas bitmapCanvas;
    private Paint autoPainter;

    private Handler handler=new Handler();
    private FullSketchObject receivedDrawing;

    private SingleLine currentLine;
    private LinkedList<Point> lineList;

    private Point lastPoint;
    private Point thisPoint;

    private int currentColor;
    private int currentWidth;
    private int screenWidth;
    private int screenHeight;
    private int currentLineIndex=0;

    private boolean continueLine = true;
    private boolean firstPoint=true;


    public ReceivedDrawingViewer(Context context, String serializedDrawing) {
        super(context);
        receivedDrawingActivity = (Activity) context;

        backgroundPaint=new Paint();
        backgroundPaint.setColor(Color.WHITE);
        autoPainter=new Paint();


        Gson deserializer=new Gson();
        receivedDrawing=deserializer.fromJson(serializedDrawing, FullSketchObject.class);
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        screenWidth = w;
        screenHeight = h;
        bitmap=Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888);
        bitmapCanvas =new Canvas(bitmap);
        bitmap.eraseColor(Color.WHITE);
        invalidate();

        startNextLine(currentLineIndex);
    }

    public void startNextLine(int index){
        currentLine=receivedDrawing.getSingleLine(index);
        lineList= currentLine.getLine();
        currentColor=currentLine.getColor();
        currentWidth=currentLine.getWidth();
        autoPainter.setColor(currentColor);
        autoPainter.setStrokeWidth(currentWidth);
        repeatAnim();
    }

    public void drawSegment(){
        if(firstPoint){
            lastPoint=lineList.poll();
            bitmapCanvas.drawCircle(lastPoint.x, lastPoint.y, currentWidth/2, autoPainter);
        }
        if(lineList.size()>0) {
            thisPoint = lineList.poll();
            bitmapCanvas.drawLine(lastPoint.x, lastPoint.y, thisPoint.x, thisPoint.y, autoPainter);
            bitmapCanvas.drawCircle(thisPoint.x, thisPoint.y, currentWidth / 2, autoPainter);
            lastPoint = thisPoint;
            invalidate();
        }else{
            startNextLine(currentLineIndex++);
        }
    }

    //Code from http://stackoverflow.com/questions/18788067/repeating-animation-with-timer

    //Need to do more research on timers to figure out how to do animations
    public void repeatAnim(){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                while (continueLine) {
                    drawSegment();
                }
            }
        },250);
    }
}

