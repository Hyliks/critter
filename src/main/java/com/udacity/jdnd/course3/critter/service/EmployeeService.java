package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entity.CustomerEntity;
import com.udacity.jdnd.course3.critter.entity.DayOfWeekEntity;
import com.udacity.jdnd.course3.critter.entity.EmployeeEntity;
import com.udacity.jdnd.course3.critter.entity.SkillEntity;
import com.udacity.jdnd.course3.critter.repository.CustomerRepository;
import com.udacity.jdnd.course3.critter.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

    public Long add(EmployeeEntity entity) {
        return this.employeeRepository.save(entity).getId();
    }

    public Optional<EmployeeEntity> getById(Long id) {
        return this.employeeRepository.findById(id);
    }

    public Iterable<EmployeeEntity> get() {
        return this.employeeRepository.findAll();
    }

    public List<EmployeeEntity> search(DayOfWeekEntity dow, List<SkillEntity> skills) {
        Iterable<EmployeeEntity> employees = this.employeeRepository.findAll();
        List<EmployeeEntity> result = new ArrayList<>();


        for(EmployeeEntity entity: employees) {
            boolean dowOk = false;
            boolean skillsOk = true;

            for(DayOfWeekEntity dowEntity: entity.getDaysAvailable()) {
                if(dowEntity.getDayOfWeek().equals(dow.getDayOfWeek())) {
                    dowOk = true;
                    break;
                }
            }

            for(SkillEntity requested: skills) {
                boolean singleSkillFound = false;

                for(SkillEntity found: entity.getSkills()) {
                    if(found.getName().equals(requested.getName())) {
                        singleSkillFound = true;
                        break;
                    }
                }

                if(!singleSkillFound) {
                    skillsOk = false;
                    break;
                }
            }


            if(skillsOk && dowOk) {
                result.add(entity);
            }
        }

        return result;
    }
}
