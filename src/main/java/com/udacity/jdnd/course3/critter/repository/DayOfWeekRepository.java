package com.udacity.jdnd.course3.critter.repository;

import com.udacity.jdnd.course3.critter.entity.DayOfWeekEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DayOfWeekRepository extends JpaRepository<DayOfWeekEntity, Long> {
}
