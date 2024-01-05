package com.example.datastructure_database;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.InspectableProperty;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.material.button.MaterialButtonToggleGroup;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DatabaseQueryAction extends Fragment {
    Button Enter;
    String action;
    Database database;
    EditText IndexValue;
    EditText InputValue;
    TextView result;
    TextView requirements;
    RadioGroup radioGroup;
    String returnValueType;
    ListView listView;

    String actionOfDatabase;
    MaterialButtonToggleGroup toggleGroup;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_database_query_action, container, false);


        //READING PREVIOUS DATABASE OBJECT, IF DOESNT EXIST CREATE A NEW ONE
        ReadingDatabase(getContext(), new ReadingDatabaseCallback() {
            @Override
            public void OnReadingDatabase(boolean Readable, Database SavedDatabase) {
                if (Readable) {
                    //READ THE PREVIOUS SAVED DATABASE OBJECT
                    database = SavedDatabase;
                } else {
                    //CREATE NEW DATABASE OBJECT IF HAVEN'T CREATED
                    database = new Database();
                    Log.d("ReadingObjectSavedState", "New Object Is Created");
                    Log.e("ReadingObjectSavedState", "Couldnt find any saved object");
                }
            }
        });


        //INSTANTIATE RADIO GROUP
        radioGroup= (RadioGroup) view.findViewById(R.id.RadioGroupAction);
        Enter = view.findViewById(R.id.Enter);
        toggleGroup = view.findViewById(R.id.InsertAction);
        IndexValue = view.findViewById(R.id.IndexEditText);
        InputValue = view.findViewById(R.id.ValueEditText);
        result = view.findViewById(R.id.Result);
        requirements = view.findViewById(R.id.RequirementsText);



        toggleGroup.addOnButtonCheckedListener((group, checkedId, isChecked) -> {

           if(isChecked){
               if (checkedId == R.id.button1) {
                   returnValueType="String";
               } else if (checkedId == R.id.button2) {
                   returnValueType="Character";
               } else if (checkedId == R.id.button3) {
                   returnValueType="Integer";
               } else if (checkedId == R.id.button4) {
                   returnValueType="Double";
               } else{
                   returnValueType=null;
               }
           }else{
               returnValueType=null;
           }
        });






        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            Log.d("RadioButton", "Checked ID: " + checkedId);

            if (checkedId == R.id.Insert) {
                Log.d("Insert", "Successfully select");
                actionOfDatabase="Insert";
            } else if (checkedId == R.id.Clear) {
                SetTextEmpty();
                actionOfDatabase="Clear";
            } else if (checkedId == R.id.Display) {
                SetTextEmpty();
                actionOfDatabase="Display";
            } else if (checkedId == R.id.Delete) {
                SetTextEmpty();
                actionOfDatabase="Delete";
            } else if (checkedId == R.id.Retrieve) {
                SetTextEmpty();
                actionOfDatabase="Retrieve";
            } else {
               actionOfDatabase=null;
            }
        });


        //WHEN ENTER BUTTON CLICKED, SAVE SELECTED RADIO BUTTON TO DECIDE WHICH ACTION TO TAKE
        Enter.setOnClickListener(v -> {
            String InputValueString = InputValue.getText().toString();
            Object element = (Object) InputValueString;

            if (actionOfDatabase == null) {
                requirements.setText("Please select an Action");
                return; // Exit early if actionOfDatabase is null
            }

            boolean errorOccurred = false;

            if (actionOfDatabase.equals("Insert")) {
                //INSERT DATABASE ACTION
                if (!AreValuesAndIndexFilled()) {
                    requirements.setText("Please Fill in Both Index and Values for Insertion");
                    errorOccurred = true;
                }

                if(!IsIndexValid(IndexValue.getText().toString())){
                    requirements.setText("Index requires minimum of 4 alphabetical and numeric characters");
                    errorOccurred = true;
                }

                if (!IsToggleButtonActivated()) {
                    requirements.setText("Please select data type to insert");
                    errorOccurred = true;
                }

                if (!ValueFitsVariableType(element)) {
                    result.setText("Value does not fit its type");
                    errorOccurred = true;
                }

                if (!errorOccurred) {
                    String InsertedValue = database.InsertNodeValueIntoDatabase(IndexValue.getText().toString(), returnValueType, InputValue.getText().toString());
                    SaveDatabaseObjectState(getContext(), database);

                    if (InsertedValue == null) {
                        result.setText("Index already Exists, try another index");
                    } else {
                        SetTextEmpty();
                        Toast.makeText(getContext(), "Successfully inserted index: " + IndexValue.getText().toString(), Toast.LENGTH_SHORT).show();}

                }
            } else if (actionOfDatabase.equals("Display")) {
                //DISPLAY DATABASE ACTION

                database.Display();
                DisplayDatabase(view);

                Toast.makeText(getContext(), "Successfully displayed the database", Toast.LENGTH_SHORT).show();

            } else if (actionOfDatabase.equals("Clear")) {
                //CLEAR DATABASE ACTION

                if(database.clear()){
                    Toast.makeText(getContext(), "Successfully clear the database", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getContext(), "Error in Clear database ", Toast.LENGTH_SHORT).show();}

            } else if (actionOfDatabase.equals("Delete")) {

                if(database.delete(IndexValue.getText().toString())){
                    Toast.makeText(getContext(), "Successfully deleted index in the database "+IndexValue.getText().toString(), Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getContext(), "Error in deleting index database "+IndexValue.getText().toString(), Toast.LENGTH_SHORT).show();}


            } else if (actionOfDatabase.equals("Retrieve")) {
                String RetrievedValue= database.get(IndexValue.getText().toString());
                if(RetrievedValue!=null){
                    result.setText(RetrievedValue);
                    Toast.makeText(getContext(), "Successfully retrieved index:  "+IndexValue.getText().toString(), Toast.LENGTH_SHORT).show();

            }else{
                    Toast.makeText(getContext(), "Error in retrieving index database "+IndexValue.getText().toString(), Toast.LENGTH_SHORT).show();}

            } else {
                requirements.setText("Enter a valid Action");
            }

            Log.d("EnterButton", "Successfully Entered button");
        });








        //AFTER EVERY ACTION, PLEASE USE THIS TO SAVE THE DATABASE CHANGES TO PREVENT LOSS OF UPDATE
        SaveDatabaseObjectState(getContext(), database);


        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SaveDatabaseObjectState(getContext(), database);
        Log.d("SavedState", "Successfully saved object in OnDestroy()");
    }


    public void DisplayDatabase(View view){
        List<Map <String, Object>> listMap = database.getHashMapValues();

            if(listMap!=null){
                listView = view.findViewById(R.id.ListViewDatabase);
                String[] from = {"ValueType", "Value","Index"};
                int[] to = {R.id.ValueType, R.id.Value, R.id.Index};

                SimpleAdapter adapter = new SimpleAdapter(
                        getContext(),
                        listMap,
                        R.layout.listview_item,
                        from,
                        to
                );

                listView.setAdapter(adapter);
            }else{
                Log.e("ListView", "ListView is null");
            }
    }


    public static void SaveDatabaseObjectState(Context context, Database database) {
        try {
            FileOutputStream fos = context.openFileOutput("filename.txt", Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(database);
            os.close();
            fos.close();
            Log.d("SavingState", "Successfully saved Object State");

        } catch (IOException e) {
            Log.e("SavingState", "ERROR in saving Database Object State", e);
            e.printStackTrace();
        }
    }


    public interface ReadingDatabaseCallback {
        void OnReadingDatabase(boolean Readable, Database database);
    }

    public static void ReadingDatabase(Context context, ReadingDatabaseCallback callback) {
        try {
            FileInputStream fis = context.openFileInput("filename.txt");
            ObjectInputStream is = new ObjectInputStream(fis);
            Database database = (Database) is.readObject();
            is.close();
            fis.close();
            callback.OnReadingDatabase(true, database);
            Log.d("ReadingObject", "Successfully read object");

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            callback.OnReadingDatabase(false, null);
        }
    }




    public void SetTextEmpty(){
        IndexValue.setText("");
        InputValue.setText("");
        requirements.setText("");
        result.setText("");
    }









    public boolean AreValuesAndIndexFilled(){

       if(!IndexValue.getText().toString().equals("") && IndexValue!=null && !InputValue.getText().toString().equals("") && InputValue!=null){
           return true;
       }else{
           return false;
       }
    }

    public boolean IsToggleButtonActivated(){
        if(returnValueType==null){
            return false;
        }
        return true;
    }


    public boolean ValueFitsVariableType(Object element) {
        if (element == null) {
            return false; // or handle as needed
        }

        if (returnValueType == null) {
            return false; // or handle as needed
        }



        if (returnValueType.equals("String")) {
            return element instanceof String;
        } else if (returnValueType.equals("Character")) {
            if (element instanceof Character) {
                return true;
            } else if (element instanceof String) {
                String stringValue = (String) element;
                // Check if the string has a length of 1 (i.e., it can be converted to a character)
                return stringValue.length() == 1;
            }
        } else if (returnValueType.equals("Integer")) {
            if (element instanceof Integer) {
                return true;
            } else if (element instanceof String) {
                try {
                    // Try to parse the string as an integer
                    Integer.parseInt((String) element);
                    return true;
                } catch (NumberFormatException e) {
                    // Not a valid integer
                    return false;
                }
            }
        } else if (returnValueType.equals("Double")) {
            if (element instanceof Double) {
                return true;
            } else if (element instanceof String) {
                try {
                    // Try to parse the string as a double
                    Double.parseDouble((String) element);
                    return true;
                } catch (NumberFormatException e) {
                    // Not a valid double
                    return false;
                }
            }
        }

        return false;
    }


    public boolean IsIndexValid(String value){
        String checker = value.replaceAll("[^a-zA-Z0-9]","");
        if(checker.length()>3){
            return true;
        }
        return false;
    }





}