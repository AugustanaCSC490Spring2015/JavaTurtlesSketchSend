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
import android.view.View;
import com.google.gson.Gson;
import java.util.LinkedList;

public class ReceivedDrawingViewer extends View {

    private static final String TAG = "SketchSendView";

    private Activity receivedDrawingActivity; // keep a reference to the main Activity

    private Paint backgroundPaint;
    private Bitmap bitmap;
    private Canvas bitmapCanvas;
    private Paint autoPainter;

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

    private boolean firstPoint = true;
    private boolean stopRecursiveCall;

/*
    Constructor that sets up the canvas
 */
    public ReceivedDrawingViewer(Context context, AttributeSet atts) {
        super(context, atts);
        receivedDrawingActivity = (Activity) context;

        backgroundPaint = new Paint();
        backgroundPaint.setColor(Color.WHITE);
        autoPainter = new Paint();
    }
/*
    Checks scales the drawing for different devices
 */
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        screenWidth = w;
        screenHeight = h;
        bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmapCanvas = new Canvas(bitmap);
        bitmap.eraseColor(Color.WHITE);
        invalidate();
        receivedDrawing.resize(screenWidth,screenHeight);

        startNextLine(currentLineIndex);
    }

    @Override
    protected void onDraw(Canvas canvas){
        // drawOnHiddenBitmap the background screen
        canvas.drawBitmap(bitmap, 0, 0, backgroundPaint);
    }
/*
    Takes in the serialized String and deserializes it into a FullSketchObject
    to be drawn
 */
    public void setDrawingString(String serializedString) {
        this.serializedString = serializedString;
        Gson deserializer = new Gson();
        receivedDrawing = deserializer.fromJson(serializedString, FullSketchObject.class);
        System.out.println(receivedDrawing.getIndexColor(0) + "");
        System.out.println(receivedDrawing.getSize());
    }
/*
    Checks for new color or width and moves to the next line
 */
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
/*
    Takes in a boolean parameter to see if the recursion should stop
 */
    public void setStopRecursiveCall(Boolean recursiveCall){
        this.stopRecursiveCall=recursiveCall;
    }
/*
    Draws a particular line segment.
 */
    public void drawSegment() {
        if (firstPoint) {
            lastPoint = currentLine.getPoint();
            bitmapCanvas.drawCircle(lastPoint.x, lastPoint.y, currentWidth / 2, autoPainter);
            firstPoint = false;
            invalidate();
        } else if (currentLine.getSize() > 0) {
            thisPoint = currentLine.getPoint();
            bitmapCanvas.drawLine(lastPoint.x, lastPoint.y, thisPoint.x, thisPoint.y, autoPainter);
            bitmapCanvas.drawCircle(thisPoint.x, thisPoint.y, currentWidth / 2, autoPainter);
            lastPoint = thisPoint;
            invalidate();
        } else {
            currentLineIndex++;
            if(receivedDrawing.getSize()<=currentLineIndex){
                stopRecursiveCall=true;
                handler.removeCallbacks(null);
                handler.removeCallbacksAndMessages(r);
            }else{
                firstPoint=true;
                startNextLine(currentLineIndex);
            }
        }
    }

    //Code from http://stackoverflow.com/questions/18788067/repeating-animation-with-timer
    //Checks if the recursive call needs to continue and draws the next segment
    public void repeatAnim() {
                 r= new Runnable() {
                    @Override
                    public void run() {
                            drawSegment();
                        if(!stopRecursiveCall) {
                            handler.postDelayed(r, 75);
                        }
                    }
                };
            handler.postDelayed(r,1000);
            }
        }


