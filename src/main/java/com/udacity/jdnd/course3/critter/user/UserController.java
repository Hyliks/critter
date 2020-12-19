package com.udacity.jdnd.course3.critter.user;

import com.udacity.jdnd.course3.critter.entity.*;
import com.udacity.jdnd.course3.critter.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.util.*;

/**
 * Handles web requests related to Users.
 *
 * Includes requests for both customers and employees. Splitting this into separate user and customer controllers
 * would be fine too, though that is not part of the required scope for this class.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private EmployeeService employeeService;

    private CustomerService customerService;

    private UserService userService;

    private PetService petService;

    private DayOfWeekService dayOfWeekService;

    private SkillService skillService;

    public UserController(EmployeeService employeeService, CustomerService customerService,
                          UserService userService, PetService petService,
                          DayOfWeekService dayOfWeekService, SkillService skillService) {
        this.employeeService = employeeService;
        this.customerService = customerService;
        this.userService = userService;
        this.petService = petService;
        this.dayOfWeekService = dayOfWeekService;
        this.skillService = skillService;
    }

    @PostMapping("/customer")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO){
        CustomerEntity entity = this.transformFromCustomerDTO(customerDTO);

        //Optional<UserEntity> foundCustomer = this.userService.getByName(entity.getName());

        //if(!foundCustomer.isPresent()) {
            Long id = this.customerService.add(entity);
            return this.transformToCustomerDTO(this.customerService.getById(id).get());
        //} else {
        //    return null;
        //}
    }

    @GetMapping("/customer")
    public List<CustomerDTO> getAllCustomers(){
        List<CustomerDTO> result = new ArrayList<>();

        for(CustomerEntity entity: this.customerService.get()) {
            result.add(this.transformToCustomerDTO(entity));
        }

        return result;
    }

    @GetMapping("/customer/pet/{petId}")
    public CustomerDTO getOwnerByPet(@PathVariable long petId){
        Optional<PetEntity> entity = this.petService.getById(petId);

        if(entity.isPresent()) {
            return this.transformToCustomerDTO(entity.get().getOwner().get(0));
        } else {
            return null;
        }
    }

    @PostMapping("/employee")
    public EmployeeDTO saveEmployee(@RequestBody EmployeeDTO employeeDTO) {
        EmployeeEntity entity = this.transformFromEmployeeDTO(employeeDTO);

        //Optional<UserEntity> foundCustomer = this.userService.getByName(entity.getName());

        //if(!foundCustomer.isPresent()) {
            Long id = this.employeeService.add(entity);
            return this.transformToEmployeeDTO(this.employeeService.getById(id).get());
        //} else {
        //    return null;
        //}
    }

    @PostMapping("/employee/{employeeId}")
    public EmployeeDTO getEmployee(@PathVariable long employeeId) {
        return this.transformToEmployeeDTO(this.employeeService.getById(employeeId).orElse(null));
    }

    @PutMapping("/employee/{employeeId}")
    public void setAvailability(@RequestBody Set<DayOfWeek> daysAvailable, @PathVariable long employeeId) {
        Optional<EmployeeEntity> entity = this.employeeService.getById(employeeId);

        if(entity.isPresent()) {
            List<DayOfWeekEntity> days = entity.get().getDaysAvailable();

            days.clear();

            for(DayOfWeek dow: daysAvailable) {
                Optional<DayOfWeekEntity> dowEntity = this.dayOfWeekService.getByDayOfWeek(dow);

                if(dowEntity.isPresent()) {
                    days.add(dowEntity.get());
                } else {
                    DayOfWeekEntity newDowEntity = this.dayOfWeekService.add(dow);
                    days.add(newDowEntity);
                }
            }
        }
    }

    @GetMapping("/employee/availability")
    public List<EmployeeDTO> findEmployeesForService(@RequestBody EmployeeRequestDTO employeeDTO) {
        Set<EmployeeSkill> skills = employeeDTO.getSkills();
        List<SkillEntity> skillEntities = new ArrayList<>();

        for(EmployeeSkill skill: skills) {
            Optional<SkillEntity> skillEntity = this.skillService.getSkillByName(skill);
            if(skillEntity.isPresent()) {

            } else {
                this.skillService.add(skill);
                skillEntity = this.skillService.getSkillByName(skill);
            }

            skillEntities.add(skillEntity.get());
        }

        Optional<DayOfWeekEntity> dow = this.dayOfWeekService.getByDayOfWeek(employeeDTO.getDate().getDayOfWeek());

        if(dow.isPresent()) {

            List<EmployeeEntity> employeeEntities = this.employeeService.search(dow.get(), skillEntities);
            List<EmployeeDTO> employeeDTOS = new ArrayList<>();

            for (EmployeeEntity entity : employeeEntities) {
                employeeDTOS.add(this.transformToEmployeeDTO(entity));
            }

            return employeeDTOS;
        } else {
            return new ArrayList<>();
        }
    }

    protected CustomerEntity transformFromCustomerDTO(CustomerDTO dto) {
        if(dto != null) {
            CustomerEntity entity = new CustomerEntity();
            entity.setNotes(dto.getNotes());

            List<Long> petids = dto.getPetIds();

            if(petids != null) {
                for (Long id : petids) {
                    Optional<PetEntity> pet = this.petService.getById(id);

                    if (pet.isPresent()) {
                        entity.getPets().add(pet.get());
                    }
                }
            }

            entity.setPhoneNumber(dto.getPhoneNumber());
            entity.setId(dto.getId());
            entity.setName(dto.getName());

            return entity;
        } else {
            return null;
        }
    }

    protected CustomerDTO transformToCustomerDTO(CustomerEntity entity) {
        if(entity != null) {
            CustomerDTO dto = new CustomerDTO();
            dto.setId(entity.getId());
            dto.setName(entity.getName());
            dto.setNotes(entity.getNotes());

            List<Long> petIds = new ArrayList<>();

            for(PetEntity pet: entity.getPets()) {
                petIds.add(pet.getId());
            }

            dto.setPetIds(petIds);
            dto.setPhoneNumber(entity.getPhoneNumber());

            return dto;
        } else {
            return null;
        }
    }

    protected EmployeeEntity transformFromEmployeeDTO(EmployeeDTO dto) {
        if(dto != null) {
            EmployeeEntity entity = new EmployeeEntity();

            Set<DayOfWeek> daysAvailable = dto.getDaysAvailable();

            if(daysAvailable != null) {
                for (DayOfWeek dow : daysAvailable) {
                    Optional<DayOfWeekEntity> dowEntity = this.dayOfWeekService.getByDayOfWeek(dow);
                    if (dowEntity.isPresent()) {

                    } else {
                        this.dayOfWeekService.add(dow);
                        dowEntity = this.dayOfWeekService.getByDayOfWeek(dow);
                    }

                    entity.getDaysAvailable().add(dowEntity.get());
                }
            }

            Set<EmployeeSkill> skills = dto.getSkills();

            for(EmployeeSkill skill: skills) {
                Optional<SkillEntity> skillEntity = this.skillService.getSkillByName(skill);
                if(skillEntity.isPresent()) {

                } else {
                    this.skillService.add(skill);
                    skillEntity = this.skillService.getSkillByName(skill);
                }

                entity.getSkills().add(skillEntity.get());
            }

            entity.setId(dto.getId());
            entity.setName(dto.getName());

            return entity;
        } else {
            return null;
        }
    }

    protected EmployeeDTO transformToEmployeeDTO(EmployeeEntity entity) {
        if(entity != null) {
            EmployeeDTO dto = new EmployeeDTO();

            Set<DayOfWeek> dows = new HashSet<>();

            for(DayOfWeekEntity dow: entity.getDaysAvailable()) {
                dows.add(dow.getDayOfWeek());
            }

            dto.setDaysAvailable(dows);

            if(dto.getDaysAvailable().size() == 0) {
                dto.setDaysAvailable(null);
            }

            dto.setId(entity.getId());
            dto.setName(entity.getName());

            Set<EmployeeSkill> skills = new HashSet<>();

            for(SkillEntity skill: entity.getSkills()) {
                skills.add(skill.getName());
            }

            dto.setSkills(skills);

            return dto;
        } else {
            return null;
        }
    }
}
