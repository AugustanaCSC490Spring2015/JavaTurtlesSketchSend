package edu.augustana.javaturtles.sketchsend;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.view.View;

import com.google.gson.Gson;

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


    private Timer timer;
    private TimerTask mTimerTask;
    private Handler handler=new Handler();
    private FullSketchObject ReceivedDrawing;

    private int screenWidth;
    private int screenHeight;


    public ReceivedDrawingViewer(Context context, String serializedDrawing) {
        super(context);
        receivedDrawingActivity = (Activity) context;

        backgroundPaint=new Paint();
        backgroundPaint.setColor(Color.WHITE);


        Gson deserializer=new Gson();
        ReceivedDrawing=deserializer.fromJson(serializedDrawing, FullSketchObject.class);
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        screenWidth = w;
        screenHeight = h;
        bitmap=Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888);
        bitmapCanvas =new Canvas(bitmap);
        bitmap.eraseColor(Color.WHITE);
        invalidate();
    }

    //Code from http://stackoverflow.com/questions/18788067/repeating-animation-with-timer

    //Need to do more research on timers to figure out how to do animations
    public void repeatAnim(){
        timer=new Timer();
        mTimerTask=new TimerTask(){
            public void run(){
                handler.post(new Runnable(){
                    public void run(){
//                        repeatAnimation();

                    }
                });
            }
        };
        timer.schedule(mTimerTask, 1000);
    }
}
