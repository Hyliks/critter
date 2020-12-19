package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entity.CustomerEntity;
import com.udacity.jdnd.course3.critter.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    public Long add(CustomerEntity entity) {
        return this.customerRepository.save(entity).getId();
    }

    public Optional<CustomerEntity> getById(Long id) {
        return this.customerRepository.findById(id);
    }

    public Iterable<CustomerEntity> get() {
        return this.customerRepository.findAll();
    }
}
