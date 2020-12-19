package com.udacity.jdnd.course3.critter.schedule;

import com.udacity.jdnd.course3.critter.entity.*;
import com.udacity.jdnd.course3.critter.service.*;
import com.udacity.jdnd.course3.critter.user.EmployeeSkill;
import org.springframework.web.bind.annotation.*;

import java.time.ZoneId;
import java.util.*;

/**
 * Handles web requests related to Schedules.
 */
@RestController
@RequestMapping("/schedule")
public class ScheduleController {

    private ScheduleService scheduleService;

    private PetService petService;

    private CustomerService customerService;

    private EmployeeService employeeService;

    private SkillService skillService;

    public ScheduleController(ScheduleService scheduleService, PetService petService, CustomerService customerService, EmployeeService employeeService, SkillService skillService) {
        this.scheduleService = scheduleService;
        this.petService = petService;
        this.customerService = customerService;
        this.employeeService = employeeService;
        this.skillService = skillService;
    }

    @PostMapping
    public ScheduleDTO createSchedule(@RequestBody ScheduleDTO scheduleDTO) {
        Long id = this.scheduleService.add(this.transformFromScheduleDTO(scheduleDTO));
        Optional<ScheduleEntity> entity = this.scheduleService.getById(id);

        if(entity.isPresent()) {
            return this.transformToScheduleDTO(entity.get());
        } else {
            return null;
        }
    }

    @GetMapping
    public List<ScheduleDTO> getAllSchedules() {

        Iterable<ScheduleEntity> scheduleEntities = this.scheduleService.get();
        List<ScheduleDTO> scheduleDTOS = new ArrayList<>();

        for(ScheduleEntity entity: scheduleEntities) {
            scheduleDTOS.add(this.transformToScheduleDTO(entity));
        }

        return scheduleDTOS;
    }

    @GetMapping("/pet/{petId}")
    public List<ScheduleDTO> getScheduleForPet(@PathVariable long petId) {
        Optional<PetEntity> pet = this.petService.getById(petId);

        if(pet.isPresent()) {
            Iterable<ScheduleEntity> scheduleEntities = this.scheduleService.getByPetId(pet.get().getId());
            List<ScheduleDTO> scheduleDTOS = new ArrayList<>();

            for(ScheduleEntity entity: scheduleEntities) {
                scheduleDTOS.add(this.transformToScheduleDTO(entity));
            }

            return scheduleDTOS;
        } else {
            return new ArrayList<>();
        }
    }

    @GetMapping("/employee/{employeeId}")
    public List<ScheduleDTO> getScheduleForEmployee(@PathVariable long employeeId) {
        Optional<EmployeeEntity> employeeEntity = this.employeeService.getById(employeeId);

        if(employeeEntity.isPresent()) {
            Iterable<ScheduleEntity> scheduleEntities = this.scheduleService.getByEmployeeId(employeeEntity.get().getId());
            List<ScheduleDTO> scheduleDTOS = new ArrayList<>();

            for(ScheduleEntity entity: scheduleEntities) {
                scheduleDTOS.add(this.transformToScheduleDTO(entity));
            }

            return scheduleDTOS;
        } else {
            return new ArrayList<>();
        }
    }

    @GetMapping("/customer/{customerId}")
    public List<ScheduleDTO> getScheduleForCustomer(@PathVariable long customerId) {
        Optional<CustomerEntity> customerEntity = this.customerService.getById(customerId);

        if(customerEntity.isPresent()) {
            List<ScheduleDTO> scheduleDTOS = new ArrayList<>();

            for(PetEntity pet: customerEntity.get().getPets()) {
                Iterable<ScheduleEntity> scheduleEntities = this.scheduleService.getByPetId(pet.getId());

                for (ScheduleEntity entity : scheduleEntities) {
                    scheduleDTOS.add(this.transformToScheduleDTO(entity));
                }
            }
            return scheduleDTOS;
        } else {
            return new ArrayList<>();
        }
    }

    public ScheduleEntity transformFromScheduleDTO(ScheduleDTO dto) {
        ScheduleEntity entity = new ScheduleEntity();

        entity.setDate(java.util.Date.from(dto.getDate().atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant()));
        entity.setId(dto.getId());

        for(Long employeeid: dto.getEmployeeIds()) {
            Optional<EmployeeEntity> employee = this.employeeService.getById(employeeid);

            if(employee.isPresent()) {
                entity.getEmployees().add(employee.get());
            }
        }

        for(EmployeeSkill skill: dto.getActivities()) {
            Optional<SkillEntity> skillEntity = this.skillService.getSkillByName(skill);

            if(skillEntity.isPresent()) {
                entity.getActivities().add(skillEntity.get());
            } else {
                this.skillService.add(skill);
                entity.getActivities().add(this.skillService.getSkillByName(skill).get());
            }
        }


        for(Long id: dto.getPetIds()) {
            Optional<PetEntity> pet = this.petService.getById(id);

            if(pet.isPresent()) {
                entity.getPets().add(pet.get());
            }
        }

        return entity;
    }

    public ScheduleDTO transformToScheduleDTO(ScheduleEntity entity) {
        ScheduleDTO dto = new ScheduleDTO();

        dto.setId(entity.getId());
        dto.setDate(entity.getDate().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate());

        List<Long> employeeIds = new ArrayList<>();

        for(EmployeeEntity employeeEntity: entity.getEmployees()) {
            employeeIds.add(employeeEntity.getId());
        }

        dto.setEmployeeIds(employeeIds);

        Set<EmployeeSkill> skills = new HashSet<>();

        for(SkillEntity skillEntity: entity.getActivities()) {
            skills.add(skillEntity.getName());
        }

        dto.setActivities(skills);

        List<Long> petIds = new ArrayList<>();

        for(PetEntity petEntity: entity.getPets()) {
            petIds.add(petEntity.getId());
        }

        dto.setPetIds(petIds);

        return dto;
    }
}
