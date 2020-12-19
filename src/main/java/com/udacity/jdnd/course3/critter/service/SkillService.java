package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entity.SkillEntity;
import com.udacity.jdnd.course3.critter.repository.SkillRepository;
import com.udacity.jdnd.course3.critter.user.EmployeeSkill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SkillService {
    @Autowired
    private SkillRepository skillRepository;

    public Optional<SkillEntity> getSkillByName(EmployeeSkill skill) {
        Iterable<SkillEntity> skills = this.skillRepository.findAll();

        for(SkillEntity skillEntity: skills) {
            if(skillEntity.getName().equals(skill)) {
                return Optional.of(skillEntity);
            }
        }

        return Optional.empty();
    }

    public Long add(EmployeeSkill skill) {
        SkillEntity entity = new SkillEntity();
        entity.setName(skill);
        return this.skillRepository.save(entity).getId();
    }
}
