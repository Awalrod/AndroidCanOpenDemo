package gcdc.tabbedapp1.controllers;

import android.os.AsyncTask;

import com.gcdc.can.CanMessage;
import com.gcdc.canopen.CanOpen;
import com.gcdc.canopen.CanOpenListener;
import com.gcdc.canopen.SubEntry;


/**
 * Created by gcdc on 6/14/16.
 */
public abstract class ViewController extends AsyncTask<Object,Integer,Object> implements CanOpenListener {


    int index;
    int subindex;
    CanOpen canOpen;
    public ViewController(CanOpen canOpen, int index, int subindex){

        this.canOpen = canOpen;
        this.index = index;
        this.subindex = subindex;
    }

    public void stopListening(){
        try {
            canOpen.getObjDict().getSubEntry(index,subindex).removeListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Object doInBackground(Object[] params) {
        try {
            System.out.println("Running stuff in Background");
            canOpen.start();
            canOpen.getObjDict().getSubEntry(index,subindex).addListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    protected abstract void onProgressUpdate(Integer... currentValue);

    @Override
    public void onObjDictChange(SubEntry se) {
        try {
            publishProgress(se.getInt());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMessage(CanMessage canMessage) {
        //unused
    }

    @Override
    public void onEvent(CanOpen co, int state) {
        //unused
    }
}
