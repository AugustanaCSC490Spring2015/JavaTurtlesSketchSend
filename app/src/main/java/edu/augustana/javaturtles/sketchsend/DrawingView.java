package edu.augustana.javaturtles.sketchsend;

import android.app.Activity;
import android.app.Fragment;
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
    private static final String TAG = "SketchSendView"; // for Log.w(TAG, ...)

    private Activity myDrawActivity; // keep a reference to the main Activity

    private Paint backgroundPaint;
    private Bitmap bitmap;
    private Canvas bitmapCanvas;

    private SingleLine newLine;
    private Point lastDraw;
    private Point thisDraw;
    private boolean firstPoint=true;

    private FullSketchObject currentDrawing;

    private int lineColor=Color.BLACK;
    private int lineWidth=10;
    private int circleRadius;
    private int screenWidth;
    private int screenHeight;

    private String lineStyle="Straight";

    public DrawingView(Context context, AttributeSet atts) {
        super(context, atts);
        Log.w(TAG, "drawing view created");
        myDrawActivity = (Activity) context;

        backgroundPaint=new Paint();
        backgroundPaint.setColor(Color.WHITE);

        currentDrawing=new FullSketchObject();

        //colorSelected=myDrawActivity.getSelectedColor();
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
        bitmap=Bitmap.createBitmap(screenWidth,screenHeight,Bitmap.Config.ARGB_8888);
        currentDrawing=new FullSketchObject();
    }


    public void setColorSelected(int colorSelected){
        Log.w(TAG, colorSelected+"");
        if(colorSelected==0){
            lineColor=Color.RED;
        }else if(colorSelected==1){
            lineColor=Color.rgb(255,175,0);
        }else if(colorSelected==2){
            lineColor=Color.YELLOW;
        }else if(colorSelected==3){
            lineColor=Color.GREEN;
        }else if(colorSelected==4){
            lineColor=Color.BLUE;
        }else if(colorSelected==5){
            lineColor=Color.rgb(255,0,175);
        }else if(colorSelected==6){
            lineColor=Color.BLACK;
        }else{
            lineColor=Color.WHITE;
        }
    }

    public void setWidthSelected(int selectedWidth){
        lineWidth=selectedWidth*2;
    }

    @Override
    protected void onDraw(Canvas canvas){
        // drawOnHiddenBitmap the background screen
        canvas.drawBitmap(bitmap, 0, 0, backgroundPaint);
    }

    public void drawOnHiddenBitmap(Canvas canvas){
        Paint tester=new Paint();
        tester.setColor(newLine.getColor());
        tester.setStrokeWidth(lineWidth);

//        LinkedList<Point> thisLine=newLine.getLine();
        if(firstPoint) canvas.drawCircle(lastDraw.x, lastDraw.y,newLine.getWidth()/2,tester);
        canvas.drawLine(lastDraw.x, lastDraw.y, thisDraw.x,thisDraw.y, tester);
        canvas.drawCircle(thisDraw.x, thisDraw.y, newLine.getWidth()/2, tester);
        lastDraw=thisDraw;
    }

    public void updateView(Canvas canvas) {
        if (canvas != null) {
            canvas.drawRect(0, 0, screenWidth, screenHeight, backgroundPaint);
        }
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
        firstPoint=true;
        lastDraw=new Point(x,y);
        thisDraw=new Point(x,y); //Ensures draw if only single point
        newLine=new SingleLine(lineColor,lineWidth,lastDraw);
    }

    private void dragLine(int x, int y)
    {
        firstPoint=false;
        if((Math.abs(x-lastDraw.x) + Math.abs(y-lastDraw.y))>=2){
            thisDraw=new Point(x,y);
            newLine.add(thisDraw);
            drawOnHiddenBitmap(bitmapCanvas);
            invalidate();
        }
    }

    private void endLine(int x, int y)
    {
        newLine.add(new Point(x,y));
        currentDrawing.add(newLine);
        drawOnHiddenBitmap(bitmapCanvas);
        invalidate();

    }

}
