package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entity.DayOfWeekEntity;
import com.udacity.jdnd.course3.critter.repository.DayOfWeekRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.Optional;

@Service
public class DayOfWeekService {
    @Autowired
    private DayOfWeekRepository dayOfWeekRepository;

    public Optional<DayOfWeekEntity> getByDayOfWeek(DayOfWeek dow) {
        Iterable<DayOfWeekEntity> dayOfWeeks = this.dayOfWeekRepository.findAll();

        for(DayOfWeekEntity entity: dayOfWeeks) {
            if(entity.getDayOfWeek().equals(dow)) {
                return Optional.of(entity);
            }
        }

        return Optional.empty();
    }

    public DayOfWeekEntity add(DayOfWeek dow) {
        DayOfWeekEntity entity = new DayOfWeekEntity();
        entity.setDayOfWeek(dow);

        return this.dayOfWeekRepository.save(entity);
    }
}
