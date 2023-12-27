package com.example.datastructure_database;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ReplaceFragment(new DatabaseQueryAction());
    }





    public void ReplaceFragment (Fragment fragment){
       FragmentManager fm = getSupportFragmentManager();
       FragmentTransaction ft= fm.beginTransaction();
       ft.replace(R.id.QueryFragmentConstraint,fragment);
       ft.commit();

    }
}