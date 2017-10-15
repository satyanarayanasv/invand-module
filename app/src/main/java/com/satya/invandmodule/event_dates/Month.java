package com.satya.invandmodule.event_dates;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by sivakrishna on 10/10/17.
 */

public class Month  {
    private ArrayList<Day> monthDays = new ArrayList<>();
    private String monthName;
    private Date cuurentCalander;


    public ArrayList<Day> getMonthDays() {
        return monthDays;
    }

    public void setMonthDays(ArrayList<Day> monthDays) {
        this.monthDays = monthDays;
    }

    public String getMonthName() {
        return monthName;
    }

    public void setMonthName(String monthName) {
        this.monthName = monthName;
    }

    public Date getCuurentCalander() {
        return cuurentCalander;
    }

    public void setCuurentCalander(Date cuurentCalander) {
        this.cuurentCalander = cuurentCalander;
    }
}
