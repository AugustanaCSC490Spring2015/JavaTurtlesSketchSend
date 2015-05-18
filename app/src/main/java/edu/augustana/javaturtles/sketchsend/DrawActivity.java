package edu.augustana.javaturtles.sketchsend;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;


public class DrawActivity extends ActionBarActivity {

    private final String TAG = "DrawActivity";

    private ImageButton colorSelect;
    private ImageButton widthSelect;
    private ImageButton sendSketch;

    private TextView customWidth;
    private DrawingView theDrawingView;

    private int selectedWidth = 20;
    public int selectedColor = Color.BLACK;
    private ActionBar myBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);

        myBar = getSupportActionBar();
        myBar.setTitle("Sketch!");

        theDrawingView = (DrawingView) findViewById(R.id.the_drawing_view);

        //create and attach listeners to image buttons
        colorSelect = (ImageButton) findViewById(R.id.colorButton);
        colorSelect.setOnClickListener(colorSelectHandler);

        widthSelect = (ImageButton) findViewById(R.id.widthButton);
        widthSelect.setOnClickListener(widthSelectHandler);

        sendSketch = (ImageButton) findViewById(R.id.sketchSendButton);
        sendSketch.setOnClickListener(sendSketchHandler);


    }

    //Create on click listener to launch color pick dialog
    View.OnClickListener colorSelectHandler = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(DrawActivity.this);
            builder.setTitle(R.string.color_selector)
                    .setItems(R.array.colors_array, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            selectedColor = which;
                            theDrawingView.setColorSelected(selectedColor);
                        }
                    })
                    .show();
        }
    };

    //Create on click listener to launch width pick dialog
    View.OnClickListener widthSelectHandler = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(DrawActivity.this);
            LayoutInflater inflater = DrawActivity.this.getLayoutInflater();
            builder.setTitle("Width Selector");
            View dialogCustomView = inflater.inflate(R.layout.width_select_view, null);
            builder.setView(dialogCustomView);
            customWidth = (TextView)dialogCustomView.findViewById(R.id.custom_width);
            final SeekBar widthSeekBar = (SeekBar)dialogCustomView.findViewById(R.id.widthSeekBar);
            widthSeekBar.setProgress(selectedWidth);
            customWidth.setText("" + selectedWidth);
            widthSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    selectedWidth = progress + 1;
                    customWidth.setText("" + selectedWidth);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }
            });
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    theDrawingView.setWidthSelected(selectedWidth);
                }
            });
            builder.setCancelable(false);
            builder.show();
        }
    };

    //Create on click listener to send serialized string to contacts list to be sent
    View.OnClickListener sendSketchHandler = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String serializedDrawing = theDrawingView.createGson();
            System.out.println(serializedDrawing);
            Intent selectContacts = new Intent(DrawActivity.this, ContactList.class);
            selectContacts.putExtra("stringToDraw", serializedDrawing);
            selectContacts.putExtra("setSendVisible", true);
            startActivity(selectContacts);
        }
    };
}

