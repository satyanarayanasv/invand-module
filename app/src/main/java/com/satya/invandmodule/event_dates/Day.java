package com.satya.invandmodule.event_dates;

import java.util.Date;

/**
 * Created by sivakrishna on 10/10/17.
 */

public class Day  {
    private Date date;
    private boolean isSelected , isInRange;


    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isInRange() {
        return isInRange;
    }

    public void setInRange(boolean inRange) {
        isInRange = inRange;
    }
}
