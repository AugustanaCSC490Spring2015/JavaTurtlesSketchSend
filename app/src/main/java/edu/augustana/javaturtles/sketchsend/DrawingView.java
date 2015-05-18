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

import com.google.gson.Gson;

public class DrawingView extends View {
    private static final String TAG = "SketchSendView"; // for Log.w(TAG, ...)

    private Activity myDrawActivity; // keep a reference to the main Activity

    private Paint backgroundPaint;
    private Bitmap bitmap;
    private Canvas bitmapCanvas;

    private FullSketchObject currentDrawing;

    private SingleLine newLine;
    private Point lastDraw;
    private Point thisDraw;
    private boolean firstPoint=true;

    private int lineColor=Color.BLACK;
    private int lineWidth=20;
    private int screenWidth;
    private int screenHeight;


    public DrawingView(Context context, AttributeSet atts) {
        super(context, atts);
        Log.w(TAG, "drawing view created");
        myDrawActivity = (Activity) context;

        backgroundPaint=new Paint();
        backgroundPaint.setColor(Color.WHITE);
    }

    // called when the size changes (and first time, when view is created)
    // Creates the bitmap and canvas to be drawn on
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        screenWidth = w;
        screenHeight = h;
        currentDrawing=new FullSketchObject(screenWidth, screenHeight);
        bitmap=Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888);
        bitmapCanvas =new Canvas(bitmap);
        bitmap.eraseColor(Color.WHITE);
        invalidate();
    }

    // Set color based on color pick dialog
    public void setColorSelected(int colorSelected){
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
            lineColor=Color.rgb(104,38,165);
        }else if(colorSelected==6){
            lineColor=Color.BLACK;
        }else if(colorSelected==7) {
            lineColor=Color.rgb(83,36,21);
        }else{
            lineColor=Color.WHITE;
        }
    }

    // Set width based on width pick dialog
    public void setWidthSelected(int selectedWidth){
        lineWidth=selectedWidth*2;
    }

    @Override
    protected void onDraw(Canvas canvas){
        // drawOnHiddenBitmap the background screen
        canvas.drawBitmap(bitmap, 0, 0, backgroundPaint);
    }

    // Draw circle at each point and line segment connected
    public void drawOnHiddenBitmap(Canvas canvas){
        Paint painter=new Paint();
        painter.setColor(lineColor);
        painter.setStrokeWidth(lineWidth);

        if(firstPoint) canvas.drawCircle(lastDraw.x, lastDraw.y,newLine.getWidth()/2,painter);
        canvas.drawLine(lastDraw.x, lastDraw.y, thisDraw.x,thisDraw.y, painter);
        canvas.drawCircle(thisDraw.x, thisDraw.y, newLine.getWidth()/2, painter);
        lastDraw=thisDraw;
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

    //Starts new line on first Action Down event
    private void startLine(int x,int y){
        firstPoint=true;
        lastDraw=new Point(x,y);
        thisDraw=new Point(x,y); //Ensures draw if only single point
        newLine=new SingleLine(lineColor,lineWidth,lastDraw);
    }

    //Continues a line while dragging across screen
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

    //Ends line and adds it to current drawing
    private void endLine(int x, int y)
    {
        newLine.add(new Point(x,y));
        drawOnHiddenBitmap(bitmapCanvas);
        invalidate();
        currentDrawing.add(newLine);
    }

    //Serializes the Current drawing to be sent via parse
    public String createGson(){
        Gson gson=new Gson();
        String serializedDrawingObject=gson.toJson(currentDrawing);
        return serializedDrawingObject;
    }
}
