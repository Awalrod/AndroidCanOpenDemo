package gcdc.tabbedapp1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;


import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import widgets.FuelGaugeView;

import java.util.ArrayList;


import gcdc.tabbedapp1.controllers.GaugeController;
import gcdc.tabbedapp1.controllers.TextController;

public class MainActivity extends AppCompatActivity{


    private SectionsPagerAdapter mSectionsPagerAdapter;


    private ViewPager mViewPager;
    public ArrayList<String> profileNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences settings = getSharedPreferences(GlobalConstantContainer.MAIN_FILENAME, Context.MODE_PRIVATE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Gets which profile each monitor had selected
        //
        ArrayList<Integer> selections = new ArrayList<>();
        String strSelections = settings.getString(GlobalConstantContainer.SELECTIONS_KEY,null);
        if (strSelections == null) {
            for (int i = 0; i < 10; i++) {
                selections.add(0);
            }
        } else {
            selections = decodeSelections(strSelections);
        }

        // Sets up the tabs
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(),10,selections);    //number of sections to create. Cannot dynamically create new sections :(
        mViewPager = (ViewPager) findViewById(R.id.container);                                          //"selections" is passed to the adapter so it can set the fragments initial selection on the porfile chooser
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(10); //This was the golden fix. Makes sure all pages states are kept in memory

        //Reads the profiles from settings
        //profiles are stored in a string separated by commas
        //There is probably a better way to do this
        String filenames = settings.getString(GlobalConstantContainer.FILENAMES_KEY,null); //"fn1,fn2,fn3,..."
        profileNames = new ArrayList<>();
        if(filenames != null){
            for(String s: filenames.split(",")){
                profileNames.add(s);
                System.out.println(s);
            }
        }

        CanOpenSingleton.setIpAndPort("10.10.3.1",2000);//Default Values
    }

    //onPause() is called for various events that put the activity in the background
    //We want to save important data
    @Override
    public void onPause(){
        super.onPause();
        saveData();
    }



    public void saveData(){
        SharedPreferences settings = getSharedPreferences(GlobalConstantContainer.MAIN_FILENAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();

        //This saves the selections
        ArrayList<Integer> selections = mSectionsPagerAdapter.getSelections();
        StringBuilder selectionBuilder = new StringBuilder();
        for(Integer selection : selections){
            selectionBuilder.append(selection);
            selectionBuilder.append(",");
        }
        selectionBuilder.deleteCharAt(selectionBuilder.length()-1);//don't want that last comma

        editor.putString(GlobalConstantContainer.SELECTIONS_KEY, selectionBuilder.toString());

        //saves names of the profiles
        StringBuilder profileBuilder = new StringBuilder("");
        if(profileNames.size()!=0){
            profileBuilder = new StringBuilder();
            for(String profile : profileNames){
                profileBuilder.append(profile);
                profileBuilder.append(",");
            }
            profileBuilder.deleteCharAt(profileBuilder.length()-1);//don't want that last comma
        }
        editor.putString(GlobalConstantContainer.FILENAMES_KEY,profileBuilder.toString());
        editor.apply();//editor.apply() saves changes to file (GlobalConstantContainer.MAIN_FILENAME) and executes in background, unlike editor.commit()

    }



    //Used this to change string of ints separated by commas into a list of int
    private ArrayList<Integer> decodeSelections(String selections){
        ArrayList<Integer> decodedSelections = new ArrayList<>();
        for(String s: selections.split(",")){
            decodedSelections.add(Integer.parseInt(s));
        }
        return decodedSelections;
    }

    //Creates that little three dot/bar thing at the top
    //menu_main is found in res/menu/menu_main
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //This event is triggered anytime a user clicks on an option in the option menu
    //Currently (7/11/16) there is only one option, "Settings"
    //The actionBar automatically handles navigation events, like returning to parent activity,
    //  but only if parent activity is properly set in AndroidManifest.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            try {
                startActivity(intent);
            }catch(RuntimeException e){
                e.printStackTrace();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**Description
     *This class creates the Fragments that will populate
     *the tabs.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        private int count;
        private ArrayList<PlaceholderFragment> fragments;
        private ArrayList<Integer> selections;

        public SectionsPagerAdapter(FragmentManager fm,int count, ArrayList<Integer> savedSelections) {
            super(fm);

            fragments = new ArrayList<>();
            this.count = count;
            selections = savedSelections;

            //Arguments must be passed to a Fragment through a bundle
            //A Bundle is just a set of key-value pairs
            for(int i = 0; i < 10; i++){
                Bundle args = new Bundle();
                args.putInt(GlobalConstantContainer.SELECTIONS_KEY,selections.get(i));
                PlaceholderFragment placeholderFragment = PlaceholderFragment.newInstance(args);
                fragments.add(placeholderFragment);
            }
        }
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        @Override
        public Fragment getItem(int position) {

            StringBuilder encodedSelections = new StringBuilder();

            for(Integer sel : selections){
                encodedSelections.append(sel);
            }

            PlaceholderFragment placeholderFragment = null;
            try{
                placeholderFragment = fragments.get(position);
                Bundle args = new Bundle();
                args.putInt(GlobalConstantContainer.SELECTIONS_KEY,placeholderFragment.getSelection());

            }catch(IndexOutOfBoundsException e){
                e.printStackTrace();
            }

            return placeholderFragment;
        }

        @Override
        public int getCount() {
            return count;
        }

        //updates the selections list to match what profiles the user has selected
        private void updateSelections(){
            for(int i = 0; i<fragments.size(); i+=1){
                selections.set(i,fragments.get(i).getSelection());
            }
        }


        public ArrayList<Integer> getSelections(){
            updateSelections();
            return selections;
        }
    }


    /**Description
     * Fragments are kinda just chunks of UI but different
     * They are meant to be self-contained, not relying on code outside the fragment
     */
    public static class PlaceholderFragment extends Fragment implements View.OnTouchListener, AdapterView.OnItemSelectedListener, View.OnClickListener {

        private GaugeController gaugeController;
        private TextController textController;

        private Spinner spinner;
        private Button startButton;
        private Button stopButton;
        private FuelGaugeView gaugeView;
        private TextView textView;

        public int currentSpinnerSelection;

        private boolean first = true;

        public static PlaceholderFragment newInstance(Bundle args) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            fragment.setArguments(args);
            return fragment;
        }
        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            startButton = (Button)rootView.findViewById(R.id.start_button);
            startButton.setOnClickListener(this);
            stopButton = (Button)rootView.findViewById(R.id.stop_button);
            stopButton.setOnClickListener(this);
            gaugeView = (FuelGaugeView)rootView.findViewById(R.id.gauge);
            textView = (TextView)rootView.findViewById(R.id.intVal);
            spinner = (Spinner)rootView.findViewById(R.id.spinner);
            spinner.setOnTouchListener(this);
            spinner.setAdapter(getProfileOptions());
            Bundle args = getArguments();
            spinner.setOnItemSelectedListener(this);
            System.out.println("Creating Fragment");

            return rootView;
        }




        @Override
        public void onSaveInstanceState(Bundle outstate){
            outstate.putInt(GlobalConstantContainer.SELECTIONS_KEY,spinner.getSelectedItemPosition());
            super.onSaveInstanceState(outstate);
        }


        public ArrayAdapter<String> getProfileOptions(){
            ArrayAdapter adapter;
            SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(GlobalConstantContainer.SETTINGS_FILENAME, Context.MODE_PRIVATE);
            String strOptions = sharedPreferences.getString(GlobalConstantContainer.FILENAMES_KEY,null);

            String[] options;
            if(strOptions == null){
                options = new String[]{};
            }else {
                options = strOptions.split(",");
            }
            adapter = new ArrayAdapter(this.getActivity(),android.R.layout.simple_spinner_dropdown_item,options);
            return adapter;
        }

        public Integer getSelection(){
            return currentSpinnerSelection;
        }

        public void setSelection(Integer selection){
            spinner.setSelection(selection);
            currentSpinnerSelection = selection;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            spinner.setAdapter(getProfileOptions());
            try {
                setSelection(currentSpinnerSelection);
            }catch(IndexOutOfBoundsException e){
                e.printStackTrace();
                setSelection(0);
            }
            return false;
        }

        //onItemSelected gets called on initialization (stupid android devs)
        //so we have to ignore that first call
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if(!first){
                currentSpinnerSelection = position;
                System.out.println(position);
            }else{
                first = false;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }

        //creates new ViewControllers and starts controlling UI elements
        private void startListening(){
            String ip = getActivity().getSharedPreferences(GlobalConstantContainer.SETTINGS_FILENAME,Context.MODE_PRIVATE).getString(GlobalConstantContainer.IP_KEY,null);
            Integer port = getActivity().getSharedPreferences(GlobalConstantContainer.SETTINGS_FILENAME,Context.MODE_PRIVATE).getInt(GlobalConstantContainer.PORT_KEY,2000);
            CanOpenSingleton.setIpAndPort(ip,port);//update CanOpen to whatever the settings have
            /**ISSUE
             * If CanOpen is already running and the IP and port are changed in the settings,
             * CanOpen will NOT close and restart with the new IP and port.
             * The IP and port at the time of CanOpen starting are permanent until the app restarts
             */



            //clear any current controllers
            //if we don't do this the views being controlled will continue getting messages from their old profile
            if(gaugeController != null){
                gaugeController.stopListening();
            }
            if(textController != null){
                textController.stopListening();
            }

            gaugeController = new GaugeController(gaugeView,CanOpenSingleton.getInstance(),getIndex(),getSubindex());
            gaugeController.setRange(getMin(),getMax());
            gaugeController.execute();

            textController = new TextController(textView,CanOpenSingleton.getInstance(),getIndex(),getSubindex());
            textController.execute();
        }

        //Does not actually stop CanOpen
        private void stopListening(){
            gaugeController.stopListening();
            textController.stopListening();
        }

        //Following four methods get the current profile selected and return its info
        private Float getMin(){
            Float min;
            String profileName = (String) spinner.getItemAtPosition(currentSpinnerSelection);
            min = getActivity().getSharedPreferences(profileName,Context.MODE_PRIVATE).getFloat(GlobalConstantContainer.MIN_KEY,0);
            return min;
        }
        private Float getMax(){
            Float max;
            String profileName = (String) spinner.getItemAtPosition(currentSpinnerSelection);
            max = getActivity().getSharedPreferences(profileName,Context.MODE_PRIVATE).getFloat(GlobalConstantContainer.MAX_KEY,0);
            return max;
        }
        private Integer getIndex(){
            Integer index;
            String profileName = (String) spinner.getItemAtPosition(currentSpinnerSelection);
            index = getActivity().getSharedPreferences(profileName,Context.MODE_PRIVATE).getInt(GlobalConstantContainer.INDEX_KEY,0);
            return index;
        }
        private Integer getSubindex(){
            Integer subIndex;
            String profileName = (String) spinner.getItemAtPosition(currentSpinnerSelection);
            subIndex = getActivity().getSharedPreferences(profileName,Context.MODE_PRIVATE).getInt(GlobalConstantContainer.SUBINDEX_KEY,0);
            return subIndex;
        }

        @Override
        public void onClick(View v) {
            if(v == startButton){
                startListening();
            }
            if(v == stopButton){
                stopListening();
            }
        }
    }
}
