package com.udacity.jdnd.course3.critter.pet;

import com.udacity.jdnd.course3.critter.entity.CustomerEntity;
import com.udacity.jdnd.course3.critter.entity.PetEntity;
import com.udacity.jdnd.course3.critter.service.CustomerService;
import com.udacity.jdnd.course3.critter.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Handles web requests related to Pets.
 */
@RestController
@RequestMapping("/pet")
public class PetController {

    @Autowired
    private PetService petService;

    @Autowired
    private CustomerService customerService;

    @PostMapping
    public PetDTO savePet(@RequestBody PetDTO petDTO) {
        PetEntity entity = this.transformFromPetDTO(petDTO);

        Long id = this.petService.add(entity);
        PetEntity pet = this.petService.getById(id).orElse(null);

        if(pet != null) {
            pet.getOwner().get(0).getPets().add(pet);
            return this.transformToPetDTO(pet);
        }

        return null;
    }

    @GetMapping("/{petId}")
    public PetDTO getPet(@PathVariable long petId) {
        return this.transformToPetDTO(this.petService.getById(petId).orElse(null));
    }

    @GetMapping
    public List<PetDTO> getPets(){
        Iterable<PetEntity> pets = this.petService.get();
        List<PetDTO> dtos = new ArrayList<>();

        for(PetEntity pet: pets) {
            dtos.add(this.transformToPetDTO(pet));
        }

        return dtos;
    }

    @GetMapping("/owner/{ownerId}")
    public List<PetDTO> getPetsByOwner(@PathVariable long ownerId) {
        Optional<CustomerEntity> customer = this.customerService.getById(ownerId);

        if(customer.isPresent()) {
            List<PetDTO> dtos = new ArrayList<>();

            for(PetEntity pet: customer.get().getPets()) {
                dtos.add(this.transformToPetDTO(pet));
            }

            return(dtos);
        }

        return new ArrayList<>();
    }

    protected PetEntity transformFromPetDTO(PetDTO dto) {
        PetEntity entity = new PetEntity();
        entity.setName(dto.getName());
        entity.setNotes(dto.getNotes());
        entity.setId(dto.getId());
        entity.setType(dto.getType());
        List<CustomerEntity> customers = entity.getOwner();

        Optional<CustomerEntity> customer = this.customerService.getById(dto.getOwnerId());

        if (customer.isPresent()) {
            customers.add(customer.get());
        }

        if (dto.getBirthDate() != null) {
            entity.setBirthDate(java.util.Date.from(dto.getBirthDate().atStartOfDay()
                    .atZone(ZoneId.systemDefault())
                    .toInstant()));
        }

        return entity;
    }

    protected PetDTO transformToPetDTO(PetEntity entity) {
        PetDTO dto = new PetDTO();
        if(entity.getBirthDate() != null) {
            dto.setBirthDate(entity.getBirthDate().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate());
        }
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setNotes(entity.getNotes());
        dto.setOwnerId(entity.getOwner().get(0).getId());
        dto.setType(entity.getType());

        return dto;
    }
}
