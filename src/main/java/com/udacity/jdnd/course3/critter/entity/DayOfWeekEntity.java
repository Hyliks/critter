package com.udacity.jdnd.course3.critter.entity;

import javax.persistence.*;
import java.time.DayOfWeek;

@Entity
@Table(name = "dayOfWeek")
public class DayOfWeekEntity {
    @Id
    @GeneratedValue
    private long id;

    @Enumerated(EnumType.ORDINAL)
    private DayOfWeek dayOfWeek;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }
}
