package com.satya.invandmodule;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.satya.invandmodule.fragments.EventDatesFragment;
import com.satya.invandmodule.fragments.EventLocationFragment;
import com.satya.invandmodule.fragments.EventMediaFragment;
import com.satya.invandmodule.fragments.EventTitleFragment;

public class MainActivity extends AppCompatActivity {

    public EventDatesFragment eventDatesFragment;
    public EventTitleFragment eventTitleFragment;
    public EventMediaFragment eventMediaFragment;
    public EventLocationFragment eventLocationFragment;
    public Button nextBtn , backBtn;
    private String currentFragmentName = "event_title";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nextBtn = (Button)findViewById(R.id.next_btn);
        backBtn = (Button)findViewById(R.id.back_btn);
        backBtn.setVisibility(View.INVISIBLE);
//        eventTitleFragment = new EventTitleFragment();
//        getSupportFragmentManager().beginTransaction().add(R.id.frame_layout,eventTitleFragment).commit();
        eventLocationFragment = new EventLocationFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.frame_layout,eventLocationFragment).commit();

//        eventMediaFragment = new EventMediaFragment();
//        getSupportFragmentManager().beginTransaction().add(R.id.frame_layout,eventMediaFragment).commit();


        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickNextBtn();
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                clickBackBtn();
            }
        });
    }

    private void clickNextBtn(){

        if(currentFragmentName.equalsIgnoreCase("event_title")){

            if(eventTitleFragment.event_title.getText().toString().length() > 0){
                backBtn.setVisibility(View.VISIBLE);
                currentFragmentName = "event_dates";
                moveToDatesFragmentFromTitle();
                //moveToEventDatesScreen();
            }else {
                eventTitleFragment.showMessage("Fill Title and move to next..");
            }

        }else if(currentFragmentName.equalsIgnoreCase("event_dates")){
            //not.

        }


    }

    private void clickBackBtn(){
        if(currentFragmentName.equalsIgnoreCase("event_dates")){
            backBtn.setVisibility(View.INVISIBLE);

            moveToTitleFramentFromdates();
            currentFragmentName = "event_title";
        }


    }

    private void moveToDatesFragmentFromTitle(){
        getSupportFragmentManager().beginTransaction().hide(eventTitleFragment).commit();
        if(eventDatesFragment == null){
            eventDatesFragment = new EventDatesFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.frame_layout,eventDatesFragment).commit();
        }else {
            getSupportFragmentManager().beginTransaction().show(eventDatesFragment).commit();
        }
    }



    private void moveToTitleFramentFromdates(){
        eventTitleFragment.event_title.setFocusable(false);
        eventTitleFragment.event_title.setFocusableInTouchMode(false);
        getSupportFragmentManager().beginTransaction().hide(eventDatesFragment).commit();
        getSupportFragmentManager().beginTransaction().show(eventTitleFragment).commit();
    }
}
