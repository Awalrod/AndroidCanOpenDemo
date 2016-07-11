package gcdc.tabbedapp1.controllers;

import android.widget.TextView;

import com.gcdc.canopen.CanOpen;

/**
 * Created by gcdc on 6/15/16.
 */
public class TextController extends ViewController {
    TextView textView;
    public TextController(TextView textView, CanOpen canOpen, int index, int subindex){
        super(canOpen,index,subindex);
        this.textView = textView;
    }

    @Override
    protected void onProgressUpdate(Integer... currentValue) {
        textView.setText(currentValue[0].toString());
    }
}
