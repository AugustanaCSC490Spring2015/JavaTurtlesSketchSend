package edu.augustana.javaturtles.sketchsend;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;


public class ReceivedDrawing extends ActionBarActivity {

    private ReceivedDrawingViewer theReceivedViewer;
    public String serializedDrawing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_received_drawing);

        ActionBar myBar = getSupportActionBar();
        myBar.hide();

        theReceivedViewer = (ReceivedDrawingViewer) findViewById(R.id.the_received_viewer);

        Bundle bundle = getIntent().getExtras();
        serializedDrawing = bundle.getString("stringToRedraw");

        theReceivedViewer.setDrawingString(serializedDrawing);
    }

    @Override
    protected void onPause(){
        super.onPause();
        theReceivedViewer.setStopRecursiveCall(true);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        theReceivedViewer.setStopRecursiveCall(true);
    }

}
