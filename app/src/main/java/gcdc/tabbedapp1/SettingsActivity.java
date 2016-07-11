package gcdc.tabbedapp1;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SettingsActivity extends AppCompatActivity {
    SettingsActivity instance;
    EditText ipEditText;
    EditText portEditText;
    LinearLayout linearLayout;
    ArrayList<ProfileFragment> profileFragments;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ipEditText = (EditText) findViewById(R.id.ip_edit);
        ipEditText.setText(getSharedPreferences(GlobalConstantContainer.SETTINGS_FILENAME,Context.MODE_PRIVATE).getString(GlobalConstantContainer.IP_KEY,"IP Address"));
        portEditText = (EditText) findViewById(R.id.port_edit);
        portEditText.setText(Integer.toString(getSharedPreferences(GlobalConstantContainer.SETTINGS_FILENAME,Context.MODE_PRIVATE).getInt(GlobalConstantContainer.PORT_KEY,2000)));
        linearLayout = (LinearLayout)findViewById(R.id.linear_layout);
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);
        instance = this;
        profileFragments = new ArrayList<>();
        makeProfileFragments();
    }

    public void makeProfileFragments(){
        for(ProfileFragment profileFragment: profileFragments){
            profileFragment.removeSelf();
        }
        profileFragments.clear();
        String unsplitFilenames = getSharedPreferences(GlobalConstantContainer.SETTINGS_FILENAME,Context.MODE_PRIVATE).getString(GlobalConstantContainer.FILENAMES_KEY,null);
//        System.out.println(unsplitFilenames);
        if(unsplitFilenames != null){
            String[] splitFilenames = unsplitFilenames.split(",");
            for(int i = 0; i < splitFilenames.length; i++){
                String name = splitFilenames[i];
                FragmentManager fragmentManager = getFragmentManager();
                FrameLayout frameLayout = new FrameLayout(this);
                frameLayout.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.MATCH_PARENT));
                frameLayout.setId(i+1);
                linearLayout.addView(frameLayout);

                SharedPreferences profileInfo = getSharedPreferences(name,Context.MODE_PRIVATE);


                try {
                    Integer index = profileInfo.getInt(GlobalConstantContainer.INDEX_KEY,0);
                    Integer subindex = profileInfo.getInt(GlobalConstantContainer.SUBINDEX_KEY,0);
                    Float min = profileInfo.getFloat(GlobalConstantContainer.MIN_KEY, 0);
                    Float max = profileInfo.getFloat(GlobalConstantContainer.MAX_KEY, 0);
                    ProfileFragment profileFragment = ProfileFragment.newInstance(name, index, subindex, min, max);
                    fragmentManager.beginTransaction().replace(frameLayout.getId(), profileFragment).commit();
                    profileFragments.add(profileFragment);
                }catch(NumberFormatException e){
                    e.printStackTrace();
                    Toast.makeText(instance, "ERROR Displaying fragments", Toast.LENGTH_LONG).show();
                }
            }
        }else{
            Toast.makeText(instance, "No Profiles Found", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        String ipText = ipEditText.getText().toString().trim();
        String portText = portEditText.getText().toString().trim();
        SharedPreferences.Editor editor = getSharedPreferences(GlobalConstantContainer.SETTINGS_FILENAME,Context.MODE_PRIVATE).edit();

        if(isValidIP(ipText) && isValidPort(portText)){
            editor.putString(GlobalConstantContainer.IP_KEY,ipText);
            editor.putInt(GlobalConstantContainer.PORT_KEY,Integer.parseInt(portText));
            editor.apply();
        }
    }

    public boolean isValidIP(String ip){
        String pattern = "(\\d+\\.){3}(\\d+)";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(ip);
        return m.matches();
    }
    public boolean isValidPort(String port){
        String pattern = "\\d+";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(port);
        return m.matches();
    }

    public void add_profile_clicked(View view){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final GridLayout gridLayout = (GridLayout)getLayoutInflater().inflate(R.layout.add_dialog,null);

        alert.setView(gridLayout);


        alert.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                try {
                    EditText edit_name = (EditText) gridLayout.findViewById(R.id.edit_name);
                    String name = edit_name.getText().toString().trim();
                    EditText edit_index = (EditText) gridLayout.findViewById(R.id.edit_index);
                    Integer index = Integer.parseInt(edit_index.getText().toString().trim(),16);
                    EditText edit_subindex = (EditText) gridLayout.findViewById(R.id.edit_subindex);
                    Integer subindex = Integer.parseInt(edit_subindex.getText().toString().trim());
                    EditText edit_min = (EditText) gridLayout.findViewById(R.id.edit_min);
                    Float min = Float.parseFloat(edit_min.getText().toString().trim());
                    EditText edit_max = (EditText) gridLayout.findViewById(R.id.edit_max);
                    Float max = Float.parseFloat(edit_max.getText().toString().trim());

                    addProfileInfo(name, index, subindex, min, max);
                }catch(NumberFormatException e){
                    Toast.makeText(instance, "ERROR: Invalid Number",
                            Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //Do Nothing
            }
        });

        alert.show();
    }

    private void addProfileInfo(String name, Integer index, Integer subindex, Float min, Float max){
        SharedPreferences sharedPreferences1 = getSharedPreferences(GlobalConstantContainer.SETTINGS_FILENAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences1.edit();
        String oldFilenames = sharedPreferences1.getString(GlobalConstantContainer.FILENAMES_KEY,null);
        StringBuilder filenamesBuilder;
        System.out.println(oldFilenames);
        if(oldFilenames == null){
            filenamesBuilder = new StringBuilder();
            filenamesBuilder.append(name);
        }else{
            filenamesBuilder = new StringBuilder(oldFilenames);
            if(oldFilenames.contains(name)){
                Toast.makeText(instance, "Profile Name Already Exists",
                        Toast.LENGTH_LONG).show();
                return;
            }else{
                filenamesBuilder.append(","+name);
            }
        }
        editor.putString(GlobalConstantContainer.FILENAMES_KEY,filenamesBuilder.toString());
        editor.apply();

        SharedPreferences sharedPreferences2 = getSharedPreferences(name,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor2 = sharedPreferences2.edit();

        editor2.putInt(GlobalConstantContainer.INDEX_KEY,index);
        editor2.putInt(GlobalConstantContainer.SUBINDEX_KEY,subindex);
        editor2.putFloat(GlobalConstantContainer.MIN_KEY,min);
        editor2.putFloat(GlobalConstantContainer.MAX_KEY,max);
        editor2.apply();

        makeProfileFragments();
    }

    public void edit_profile_clicked(View view) {
        ProfileFragment profileToEdit = null;
        for(ProfileFragment profileFragment : profileFragments){
            if(profileFragment.ownerOfButton((Button)view)){
                profileToEdit = profileFragment;
            }
        }
        final String oldName = profileToEdit.getName();
        SharedPreferences settings = getSharedPreferences(oldName,Context.MODE_PRIVATE);
        String oldIndex = Integer.toString(settings.getInt(GlobalConstantContainer.INDEX_KEY,0),16);
        String oldSubIndex = Integer.toString(settings.getInt(GlobalConstantContainer.SUBINDEX_KEY, 0));
        String oldMin = Float.toString(settings.getFloat(GlobalConstantContainer.MIN_KEY,0));
        String oldMax = Float.toString(settings.getFloat(GlobalConstantContainer.MAX_KEY,0));

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final GridLayout gridLayout = (GridLayout)getLayoutInflater().inflate(R.layout.add_dialog,null);
        final EditText edit_name = (EditText) gridLayout.findViewById(R.id.edit_name);
        edit_name.setText(oldName);
        final EditText edit_index = (EditText) gridLayout.findViewById(R.id.edit_index);
        edit_index.setText(oldIndex);
        final EditText edit_subindex = (EditText) gridLayout.findViewById(R.id.edit_subindex);
        edit_subindex.setText(oldSubIndex);
        final EditText edit_min = (EditText) gridLayout.findViewById(R.id.edit_min);
        edit_min.setText(oldMin);
        final EditText edit_max = (EditText) gridLayout.findViewById(R.id.edit_max);
        edit_max.setText(oldMax);
        alert.setView(gridLayout);
        alert.setPositiveButton("Save Changes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                try {

                    String name = edit_name.getText().toString().trim();

                    Integer index = Integer.parseInt(edit_index.getText().toString().trim(),16);

                    Integer subindex = Integer.parseInt(edit_subindex.getText().toString().trim());

                    Float min = Float.parseFloat(edit_min.getText().toString().trim());

                    Float max = Float.parseFloat(edit_max.getText().toString().trim());

                    editProfileInfo(oldName, name, index, subindex, min, max);
                }catch(NumberFormatException e){
                    Toast.makeText(instance, "ERROR: Invalid Number",
                            Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //Do Nothing
            }
        });

        alert.show();
    }
    public void editProfileInfo(String oldName, String name, Integer index, Integer subindex, Float min, Float max){
        SharedPreferences sharedPreferences1 = getSharedPreferences(GlobalConstantContainer.SETTINGS_FILENAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences1.edit();
        String oldFilenames = sharedPreferences1.getString(GlobalConstantContainer.FILENAMES_KEY,null);
        StringBuilder filenamesBuilder = new StringBuilder();
        for(String s : oldFilenames.split(",")){
            if(!s.equals(oldName)){
                filenamesBuilder.append(s+",");
            }
        }
        filenamesBuilder.append(name);
        editor.putString(GlobalConstantContainer.FILENAMES_KEY,filenamesBuilder.toString());
        editor.apply();

        SharedPreferences.Editor editor2 = getSharedPreferences(name,Context.MODE_PRIVATE).edit();
        editor2.putInt(GlobalConstantContainer.INDEX_KEY,index);
        editor2.putInt(GlobalConstantContainer.SUBINDEX_KEY,subindex);
        editor2.putFloat(GlobalConstantContainer.MIN_KEY,min);
        editor2.putFloat(GlobalConstantContainer.MAX_KEY,max);
        editor2.apply();

        makeProfileFragments();

    }

    public void delete_profile_clicked(View view){
        ProfileFragment profileToBeRemoved = null;
        for(ProfileFragment profileFragment: profileFragments){
            if(profileFragment.ownerOfButton((Button)view)){
                profileToBeRemoved = profileFragment;
                String name = profileFragment.getName();
                String strFilenames = getSharedPreferences(GlobalConstantContainer.SETTINGS_FILENAME,Context.MODE_PRIVATE).getString(GlobalConstantContainer.FILENAMES_KEY,null);
                String[] splitFilenames = strFilenames.split(",");
                ArrayList<String> filenames = new ArrayList<>(Arrays.asList(splitFilenames));
                StringBuilder filenamesBuilder = new StringBuilder();
                for(String filename : filenames){
                    if(!name.equals(filename)){
                        filenamesBuilder.append(filename + ",");
                    }
                }
                if(filenamesBuilder.length()>0){
                    filenamesBuilder.deleteCharAt(filenamesBuilder.length()-1);
                    getSharedPreferences(GlobalConstantContainer.SETTINGS_FILENAME,Context.MODE_PRIVATE).edit().putString(GlobalConstantContainer.FILENAMES_KEY,filenamesBuilder.toString()).apply();
                }else{
                    getSharedPreferences(GlobalConstantContainer.SETTINGS_FILENAME,Context.MODE_PRIVATE).edit().remove(GlobalConstantContainer.FILENAMES_KEY).apply();
                }



                profileFragment.removeSelf();
            }
        }
        profileFragments.remove(profileToBeRemoved);
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()== android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class ProfileFragment extends Fragment{

        TextView name_text;
        TextView index_text;
        TextView subindex_text;
        TextView min_text;
        TextView max_text;

        Button edit;
        Button delete;



        public static ProfileFragment newInstance(String name, Integer index, Integer subindex, Float min, Float max) {
            ProfileFragment fragment = new ProfileFragment();
            Bundle args = new Bundle();
            args.putString(GlobalConstantContainer.NAME_KEY,name);
            args.putInt(GlobalConstantContainer.INDEX_KEY,index);
            args.putInt(GlobalConstantContainer.SUBINDEX_KEY,subindex);
            args.putFloat(GlobalConstantContainer.MIN_KEY,min);
            args.putFloat(GlobalConstantContainer.MAX_KEY,max);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
            GridLayout rootView = (GridLayout) inflater.inflate(R.layout.profile_fragment,container,false);
            name_text = (TextView)rootView.findViewById(R.id.name_text);
            index_text = (TextView)rootView.findViewById(R.id.index_text);
            subindex_text = (TextView)rootView.findViewById(R.id.subindex_text);
            min_text = (TextView)rootView.findViewById(R.id.min_text);
            max_text = (TextView)rootView.findViewById(R.id.max_text);
            edit = (Button)rootView.findViewById(R.id.edit_button);
            delete = (Button)rootView.findViewById(R.id.delete_button);

            Bundle args = getArguments();
            name_text.setText(args.getString(GlobalConstantContainer.NAME_KEY));
            index_text.setText(Integer.toString(args.getInt(GlobalConstantContainer.INDEX_KEY),16));
            subindex_text.setText(Integer.toString(args.getInt(GlobalConstantContainer.SUBINDEX_KEY)));
            min_text.setText(Float.toString(args.getFloat(GlobalConstantContainer.MIN_KEY)));
            max_text.setText(Float.toString(args.getFloat(GlobalConstantContainer.MAX_KEY)));

            return rootView;
        }

        public boolean ownerOfButton(Button button){
            return edit.equals(button) || delete.equals(button);
        }

        public void removeSelf(){
            getActivity().getFragmentManager().beginTransaction().remove(this).commit();
        }

        public String getName(){
            return name_text.getText().toString();
        }
    }
}
