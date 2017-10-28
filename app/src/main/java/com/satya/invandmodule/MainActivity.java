package com.satya.invandmodule;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.satya.invandmodule.fragments.EventDatesFragment;
import com.satya.invandmodule.fragments.EventLocationFragment;
import com.satya.invandmodule.fragments.EventMediaFragment;
import com.satya.invandmodule.fragments.EventSummeryFragment;
import com.satya.invandmodule.fragments.EventTitleFragment;
import com.satya.invandmodule.fragments.ValidatesToMove;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public EventDatesFragment eventDatesFragment;
    public EventTitleFragment eventTitleFragment;
    public EventMediaFragment eventMediaFragment;
    public EventLocationFragment eventLocationFragment;
    public EventSummeryFragment eventSummeryFragment;

    public ValidatesToMove currentFragment ;
    public Button nextBtn , backBtn;
    private String currentFragmentName = "event_title";
    ArrayList<Fragment> fragments = new ArrayList<>();
    public int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nextBtn = (Button)findViewById(R.id.next_btn);
        backBtn = (Button)findViewById(R.id.back_btn);
        backBtn.setVisibility(View.INVISIBLE);
        loadAllFragments();
        getSupportFragmentManager().beginTransaction().add(R.id.frame_layout,fragments.get(count)).commit();
        currentFragment = (ValidatesToMove)fragments.get(count);
        showButtons();




        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickNextBtn();
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickBackBtn();
            }
        });
    }


    private void loadAllFragments(){
        fragments.clear();
        eventTitleFragment = new EventTitleFragment();
        eventDatesFragment = new EventDatesFragment();
        eventMediaFragment = new EventMediaFragment();
        eventLocationFragment = new EventLocationFragment();
        eventSummeryFragment = new EventSummeryFragment();

        fragments.add(eventTitleFragment);
        fragments.add(eventDatesFragment);
        fragments.add(eventMediaFragment);
        fragments.add(eventLocationFragment);
        fragments.add(eventSummeryFragment);
    }

    private void clickNextBtn() {

        if (currentFragment.isDataValid()) {

            count = count + 1;
                if (fragments.get(count).isAdded() || fragments.get(count).isHidden()) {
                    getSupportFragmentManager().beginTransaction().hide(fragments.get(count - 1)).commit();
                    getSupportFragmentManager().beginTransaction().show(fragments.get(count)).commit();

                } else {
                    getSupportFragmentManager().beginTransaction().hide(fragments.get(count - 1)).commit();
                    getSupportFragmentManager().beginTransaction().add(R.id.frame_layout, fragments.get(count)).commit();

                }

                currentFragment = (ValidatesToMove) fragments.get(count);
                showButtons();
    }else {
            Toast.makeText(getApplicationContext(),"Enter Details Move Next ",Toast.LENGTH_SHORT).show();
        }

    }
    public void showButtons(){
        nextBtn.setVisibility(currentFragment.isShowNext() ? View.VISIBLE : View.INVISIBLE);
        backBtn.setVisibility(currentFragment.isShowBack() ? View.VISIBLE : View.INVISIBLE);
    }


    private void clickBackBtn(){

       if(count >0){
           count --;
           if(fragments.get(count).isAdded() || fragments.get(count).isHidden()){
               getSupportFragmentManager().beginTransaction().show(fragments.get(count)).commit();
               getSupportFragmentManager().beginTransaction().hide(fragments.get(count+1)).commit();
           }else {
               getSupportFragmentManager().beginTransaction().add(R.id.frame_layout,fragments.get(count)).commit();
           }
           currentFragment = (ValidatesToMove)fragments.get(count);
           showButtons();
       }



    }

}
