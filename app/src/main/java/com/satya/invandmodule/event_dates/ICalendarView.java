package com.satya.invandmodule.event_dates;

import java.util.Date;

/**
 * Created by sivakrishna on 10/10/17.
 */

public interface ICalendarView {

    public abstract void setMessage(String paramString);

    public abstract void updateAll(Date paramDate1, Date paramDate2, boolean paramBoolean);

    public abstract void updateDeparture(Date paramDate);

}
