package gcdc.tabbedapp1.controllers;


import com.gcdc.canopen.CanOpen;
import widgets.FuelGaugeView;

/**
 * Created by gcdc on 6/14/16.
 */
public class GaugeController extends ViewController {
    FuelGaugeView gauge;
    Float min;
    Float max;
    public GaugeController(FuelGaugeView gauge, CanOpen canOpen, int index, int subindex){
        super(canOpen,index,subindex);
        min = (float)0; //default
        max = (float)100; //default
        this.gauge = gauge;
    }
    public void setRange(Float min, Float max){
        this.min = min;
        this.max = max;
    }
    @Override
    protected void onProgressUpdate(Integer... currentValue){
        if(currentValue[0].intValue()<min){
            gauge.setFuelLevel(0);
        }else if(currentValue[0]>max){
            gauge.setFuelLevel(1);
        }else{
            float percentage;
            float rangeSize = max-min;
            float choppedValue = (float)currentValue[0].intValue()-min;
            percentage = choppedValue/rangeSize;
            gauge.setFuelLevel(percentage);
        }

    }
}
