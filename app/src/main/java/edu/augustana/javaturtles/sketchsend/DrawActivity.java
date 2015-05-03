package edu.augustana.javaturtles.sketchsend;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;


public class DrawActivity extends ActionBarActivity {

    private Button colorSelect;
    private Button widthSelect;
    private Button sendSketch;

    private int selectedWidth=10;
    private int selectedColor= Color.BLACK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);

        DrawingView theDrawingView =  (DrawingView) findViewById(R.id.the_drawing_view);
        Log.w("DRAW_ACTIVITY", "drawing view = " + theDrawingView);


        colorSelect = (Button) findViewById(R.id.colorButton);
        colorSelect.setOnClickListener(colorSelectHandler);

        widthSelect = (Button) findViewById(R.id.widthButton);
        widthSelect.setOnClickListener(widthSelectHandler);

        sendSketch = (Button) findViewById(R.id.sendSketchButton);
        sendSketch.setOnClickListener(sendSketchHandler);



    }

    View.OnClickListener colorSelectHandler=new View.OnClickListener(){
        @Override
        public void onClick(View v) {

        }
    };

    View.OnClickListener widthSelectHandler=new View.OnClickListener(){
        @Override
        public void onClick(View v) {

        }
    };

    View.OnClickListener sendSketchHandler = new View.OnClickListener(){
        @Override
        public void onClick(View v) {

        }
    };

//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle(R.string.pick_color)
//                .setItems(R.array.colors_array, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        selectedColor=which;
//                    }
//                });
//        return builder.create();
//    }

    public int getSelectedColor(){ return selectedColor;}
}
