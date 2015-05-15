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
    private Runnable r;
    private FullSketchObject receivedDrawing;

    private SingleLine currentLine;
    private LinkedList<Point> lineList;

    private Point lastPoint;
    private Point thisPoint;

    private String serializedString = "Not Set";

    private int currentColor;
    private int currentWidth;
    private int currentLineIndex = 0;

    private int screenWidth;
    private int screenHeight;
    private int senderScreenWidth;
    private int senderScreenHeight;

    private boolean firstPoint = true;
    private boolean stopRecursiveCall;


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
    }

    @Override
    protected void onDraw(Canvas canvas){
        // drawOnHiddenBitmap the background screen
        canvas.drawBitmap(bitmap, 0, 0, backgroundPaint);
    }

    public void setDrawingString(String serializedString) {
        this.serializedString = serializedString;
        Gson deserializer = new Gson();
        receivedDrawing = deserializer.fromJson(serializedString, FullSketchObject.class);
        System.out.println(receivedDrawing.getIndexColor(0) + "");
        System.out.println(receivedDrawing.getSize());
        Log.w(TAG, receivedDrawing.getSingleLine(0).getPoint(10).x+"    ");
        receivedDrawing.resize(screenWidth,screenHeight);
        Log.w(TAG, receivedDrawing.getSingleLine(0).getPoint(10).x+"    ");
    }

    public void startNextLine(int index) {
        currentLine = receivedDrawing.getSingleLine(index);
        lineList = currentLine.getLine();
        currentColor = currentLine.getColor();
        currentWidth = currentLine.getWidth();
        autoPainter.setColor(currentColor);
        autoPainter.setStrokeWidth(currentWidth);
        stopRecursiveCall=false;
        repeatAnim();
    }

    public void setStopRecursiveCall(Boolean recursiveCall){
        this.stopRecursiveCall=recursiveCall;
    }

    public void drawSegment() {
        Log.w(TAG, "Draw Segment Call");
        if (firstPoint) {
            lastPoint = currentLine.getPoint();
            Log.w(TAG, lastPoint.x + "/" + lastPoint.y);
            bitmapCanvas.drawCircle(lastPoint.x, lastPoint.y, currentWidth / 2, autoPainter);
            firstPoint = false;
            Log.w(TAG, currentLine.getSize()+"");
            invalidate();
        } else if (currentLine.getSize() > 0) {
            Log.w(TAG, "Starting the second point");
            thisPoint = currentLine.getPoint();
            Log.w(TAG, thisPoint.x+"/"+thisPoint.y);
            bitmapCanvas.drawLine(lastPoint.x, lastPoint.y, thisPoint.x, thisPoint.y, autoPainter);
            bitmapCanvas.drawCircle(thisPoint.x, thisPoint.y, currentWidth / 2, autoPainter);
            lastPoint = thisPoint;
            invalidate();
        } else {
            currentLineIndex++;
            if(receivedDrawing.getSize()<=currentLineIndex){
                Log.w(TAG, "REMOVING CALLBACKS");
                stopRecursiveCall=true;
                handler.removeCallbacks(null);
                handler.removeCallbacksAndMessages(r);
                Log.w(TAG, "REMOVED CALLBACKS");
            }else{
                Log.w(TAG, "CONTINUE LINE");
                firstPoint=true;
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
                 r= new Runnable() {
                    @Override
                    public void run() {
                        Log.w(TAG, "handler running");
                            drawSegment();
                        Log.w(TAG, "return from call");
                        if(!stopRecursiveCall) {
                            handler.postDelayed(r, 75);
                        }
                    }
                };
            handler.postDelayed(r,1000);
            }
        }

//        timer.schedule(drawTask, 3000, 250);
//    }


