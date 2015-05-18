package edu.augustana.javaturtles.sketchsend;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;


public class ReceivedDrawing extends ActionBarActivity {

    private ReceivedDrawingViewer theReceivedViewer;
    public String serializedDrawing;
//Catches intent extra that contains the stringToRedraw and calls the setDrawingString
    //to pass it into RecievedDrawingViewer
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_received_drawing);

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
