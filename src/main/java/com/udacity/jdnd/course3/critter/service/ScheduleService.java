package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entity.EmployeeEntity;
import com.udacity.jdnd.course3.critter.entity.PetEntity;
import com.udacity.jdnd.course3.critter.entity.ScheduleEntity;
import com.udacity.jdnd.course3.critter.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ScheduleService {
    @Autowired
    private ScheduleRepository scheduleRepository;


    public Iterable<ScheduleEntity> get() {
        return this.scheduleRepository.findAll();
    }

    public Optional<ScheduleEntity> getById(Long id) {
        return this.scheduleRepository.findById(id);
    }

    public Long add(ScheduleEntity entity) {
        return this.scheduleRepository.save(entity).getId();
    }

    public Iterable<ScheduleEntity> getByPetId(Long petId) {
        Iterable<ScheduleEntity> entities = this.scheduleRepository.findAll();
        List<ScheduleEntity> result = new ArrayList<ScheduleEntity>();

        for(ScheduleEntity entity: entities) {
            for(PetEntity pet: entity.getPets()) {
                if(pet.getId() == petId) {
                    result.add(entity);
                    break;
                }
            }
        }

        return result;
    }

    public Iterable<ScheduleEntity> getByEmployeeId(Long employeeId) {
        Iterable<ScheduleEntity> entities = this.scheduleRepository.findAll();
        List<ScheduleEntity> result = new ArrayList<ScheduleEntity>();

        for(ScheduleEntity entity: entities) {
            for(EmployeeEntity employeeEntity: entity.getEmployees()) {
                if(employeeEntity.getId() == employeeId) {
                    result.add(entity);
                    break;
                }
            }
        }

        return result;
    }
}
