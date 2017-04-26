package com.beacon.drunkengranite.forwardspam;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.beacon.drunkengranite.forwardspam.Adapters.HomeAdapter;
import com.beacon.drunkengranite.forwardspam.Fragments.AddToBlock;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;


public class MainActivity extends AppCompatActivity implements AddToBlock.onResponse, HomeAdapter.removeNumber {

    public static final String CACHE_USER_NAME = "USER";
    public static final String CACHE_PHONES_NAME = "PHONELIST";
    public static final int PREFERENCES = Context.MODE_PRIVATE;
    public FloatingActionButton mainFAB;

    SharedPreferences sharedPreferences;
    ArrayList<String> phoneList;
    Toolbar toolbar;
    RelativeLayout parentContainer;
    HomeAdapter adapter;
    RecyclerView mainList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        parentContainer = (RelativeLayout) findViewById(R.id.activity_main);
        sharedPreferences = getSharedPreferences(CACHE_USER_NAME, PREFERENCES);
        mainList = (RecyclerView) findViewById(R.id.phoneList);
        phoneList = new ArrayList<>(sharedPreferences.getStringSet(CACHE_PHONES_NAME, new HashSet<String>()));
        adapter = new HomeAdapter(phoneList, this);


        mainList.setLayoutManager(new LinearLayoutManager(this));
        mainList.setAdapter(adapter);


        mainFAB = (FloatingActionButton) findViewById(R.id.addFAB);
        mainFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddBar();
                deflateFAB();
            }
        });

        Intent intent  = new Intent(this, CallService.class);
        intent.setData(Uri.parse("init"));
        startService(intent);
        Toast.makeText(this, "Started Service", Toast.LENGTH_SHORT).show();
    }

    public void showAddBar() {
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(0, R.anim.slide_down)
                .replace(R.id.toolbar, AddToBlock.createNewAddBlock(this))
                .addToBackStack("ADD_FRAGMENT")
                .commit();
    }

    public void inflateFAB(){
        mainFAB.setVisibility(View.VISIBLE);
    }

    public void deflateFAB(){
        mainFAB.setVisibility(View.GONE);
    }

    public void saveNewBlockList(){
        sharedPreferences.edit()
                .putStringSet(CACHE_PHONES_NAME, new HashSet<>(phoneList))
                .apply();
    }

    @Override
    public void addToList(String number) {
        phoneList.add(number);
        saveNewBlockList();
    }

    @Override
    public void removeNumber(String number) {
        if(phoneList.contains(number)) {
            phoneList.remove(number);
            saveNewBlockList();
        }
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        if(getSupportFragmentManager().getBackStackEntryCount() == 0){
            this.inflateFAB();
        }
    }

}
