package edu.augustana.javaturtles.sketchsend;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.LinkedList;

public class DrawingView extends View {
    private static final String TAG = "SketchSend"; // for Log.w(TAG, ...)

    private Activity drawActivity; // keep a reference to the main Activity


    private Paint backgroundPaint;
    private Bitmap bitmap;
    private Canvas bitmapCanvas;

    private SingleLine newLine;
    private Point lastDraw;

    private FullSketchObject currentDrawing;

    private int screenWidth;
    private int screenHeight;

    private String lineStyle="Straight";

    public DrawingView(Context context, AttributeSet atts) {
        super(context, atts);
        Log.w(TAG, "drawing view created");
        drawActivity = (Activity) context;

        backgroundPaint=new Paint();
        backgroundPaint.setColor(Color.WHITE);

        currentDrawing=new FullSketchObject();
    }

    // called when the size changes (and first time, when view is created)
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        screenWidth = w;
        screenHeight = h;
        bitmap=Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888);
        bitmapCanvas =new Canvas(bitmap);
        bitmap.eraseColor(Color.WHITE);
        invalidate();
    }

    public void clearCanvas(){

    }

    public void colorSelect(int color){
    }

    public void setLineStyle(){

    }

    @Override
    protected void onDraw(Canvas canvas){
        // drawOnHiddenBitmap the background screen
        canvas.drawBitmap(bitmap, 0, 0, backgroundPaint);
    }

    public void drawOnHiddenBitmap(Canvas canvas){
        Paint tester=new Paint();
        tester.setColor(newLine.getColor());

        int widthTester=8;
        LinkedList<Point> thisLine=newLine.getLine();
        for(int i=0; i<newLine.getSize(); i++) {
            Point thisPointToDraw = thisLine.poll();
            canvas.drawCircle(thisPointToDraw.x, thisPointToDraw.y, newLine.getWidth(), tester);
            Log.w(TAG, i + " dots drawn");
            Log.w(TAG, newLine.getSize() + " dots in sketch");
            Log.w(TAG, currentDrawing.getSize() + "Sketches in current drawing");
        }
    }

    public void updateView(Canvas canvas) {
        if (canvas != null) {
            canvas.drawRect(0, 0, screenWidth, screenHeight, backgroundPaint);
        }
    }

    // stop the game; may be called by the MainGameFragment onPause
    public void stopDrawing() {

    }

    // release resources; may be called by MainGameFragment onDestroy
    public void releaseResources() {
        // release any resources (e.g. SoundPool stuff)
    }


    @Override
    public boolean onTouchEvent(MotionEvent e){

        if(e.getAction() == MotionEvent.ACTION_DOWN){
            startLine((int) e.getX(), (int) e.getY());
        }else if(e.getAction() == MotionEvent.ACTION_UP){
            endLine((int)e.getX(),(int) e.getY());
        }else{
            dragLine((int) e.getX(), (int) e.getY());
        }
        return true;
    }

    private void startLine(int x,int y){
        lastDraw=new Point(x,y);
        newLine=new SingleLine(Color.BLACK,5,lastDraw);
    }

    private void dragLine(int x, int y)
    {
        if((Math.abs(x-lastDraw.x) + Math.abs(y-lastDraw.y))>=2){
            lastDraw=new Point(x,y);
            newLine.add(lastDraw);
        }
        drawOnHiddenBitmap(bitmapCanvas);
        invalidate();
    }

    private void endLine(int x, int y)
    {
        newLine.add(new Point(x,y));
        currentDrawing.add(newLine);
        drawOnHiddenBitmap(bitmapCanvas);
        invalidate();

    }

}
