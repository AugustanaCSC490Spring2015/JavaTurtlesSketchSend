package edu.augustana.javaturtles.sketchsend;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;


import java.util.LinkedList;
import java.util.NoSuchElementException;
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

    private TimerTask drawTask;
    private Timer timer = new Timer();
    private Handler handler = new Handler();
    private FullSketchObject receivedDrawing;

    private SingleLine currentLine;
    private LinkedList<Point> lineList;

    private Point lastPoint;
    private Point thisPoint;

    private String serializedString = "Not Set";

    private int currentColor;
    private int currentWidth;
    private int screenWidth;
    private int screenHeight;
    private int currentLineIndex = 0;

    private boolean continueLine = true;
    private boolean firstPoint = true;


    public ReceivedDrawingViewer(Context context, AttributeSet atts) {
        super(context, atts);
        receivedDrawingActivity = (Activity) context;

        backgroundPaint = new Paint();
        backgroundPaint.setColor(Color.WHITE);
        autoPainter = new Paint();
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        screenWidth = w;
        screenHeight = h;
        bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmapCanvas = new Canvas(bitmap);
        bitmap.eraseColor(Color.WHITE);
        invalidate();

        startNextLine(currentLineIndex);
        System.out.println(serializedString);
        currentLine = receivedDrawing.getSingleLine(0);
        System.out.println(currentLine.getColor() + "   " + currentLine.getWidth() + "    " + currentLine.getSize());
    }

    public void setDrawingString(String serializedString) {
        this.serializedString = serializedString;
        Gson deserializer = new Gson();
        receivedDrawing = deserializer.fromJson(serializedString, FullSketchObject.class);
        System.out.println(receivedDrawing.getIndexColor(0) + "");
        System.out.println(receivedDrawing.getSize());
    }

    public void startNextLine(int index) {
        currentLine = receivedDrawing.getSingleLine(index);
        Log.w(TAG, "Initialized Line object");
        lineList = currentLine.getLine();
        Log.w(TAG, "Initialized list:  " + lineList.size());
        currentColor = currentLine.getColor();
        Log.w(TAG, "Initialized Color");
        currentWidth = currentLine.getWidth();
        autoPainter.setColor(currentColor);
        autoPainter.setStrokeWidth(currentWidth);
        repeatAnim();
        Log.w(TAG, "Current Values: " + currentColor + "    " + currentWidth);
    }

    public void drawSegment() {
        if (firstPoint) {
            lastPoint = currentLine.getPoint();
            Log.w(TAG, lastPoint.x + "/" + lastPoint.y);
            bitmapCanvas.drawCircle(lastPoint.x, lastPoint.y, currentWidth / 2, autoPainter);
            firstPoint = false;
            invalidate();
        } else if (currentLine.getSize() > 0) {
            thisPoint = currentLine.getPoint();
            Log.w(TAG, thisPoint.x+"/"+thisPoint.y);
            bitmapCanvas.drawLine(lastPoint.x, lastPoint.y, thisPoint.x, thisPoint.y, autoPainter);
            bitmapCanvas.drawCircle(thisPoint.x, thisPoint.y, currentWidth / 2, autoPainter);
            lastPoint = thisPoint;
            invalidate();
        } else {
            currentLineIndex++;
            if(receivedDrawing.getSize()>=currentLineIndex){
                //STOP THE TIMAH
            }else{
                startNextLine(currentLineIndex);
            }
        }
    }

    //Code from http://stackoverflow.com/questions/18788067/repeating-animation-with-timer

    //FIX THE TIMAH

    //Need to do more research on timers to figure out how to do animations
    public void repeatAnim() {
//        drawTask = new TimerTask() {
//            @Override
//            public void run() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        while (continueLine) {
                            drawSegment();
                        }
                    }
                }, 250);
            }
        }

//        timer.schedule(drawTask, 3000, 250);
//    }


