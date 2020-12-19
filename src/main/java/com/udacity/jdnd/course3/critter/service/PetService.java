package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entity.PetEntity;
import com.udacity.jdnd.course3.critter.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PetService {
    @Autowired
    private PetRepository petRepository;

    public Optional<PetEntity> getById(Long id) {
        return this.petRepository.findById(id);
    }

    public Optional<PetEntity> getByName(String name) {
        Iterable<PetEntity> entities = this.petRepository.findAll();

        for(PetEntity pet: entities) {
            if(pet.getName().equals(name)) {
                return Optional.of(pet);
            }
        }

        return Optional.empty();
    }

    public Long add(PetEntity entity) {
        return this.petRepository.save(entity).getId();
    }

    public Iterable<PetEntity> get() {
        return this.petRepository.findAll();
    }
}
