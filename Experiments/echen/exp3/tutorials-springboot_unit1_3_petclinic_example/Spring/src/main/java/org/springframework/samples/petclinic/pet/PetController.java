/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.pet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.samples.petclinic.owner.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
class PetController {

    @Autowired
    PetRepository petRepository;

    @Autowired
    OwnerRepository ownerRepository;

    @PostMapping(path = "/pets/addDummy")
    public void addDummyPets() {
        try {
            petRepository.save(new Pet(1, "Alice", "Dog", ownerRepository.findById(3).get()));
            petRepository.save(new Pet(2, "Bob", "Cat", ownerRepository.findById(2).get()));
            petRepository.save(new Pet(3, "Charlie", "Hamster", ownerRepository.findById(1).get()));
            petRepository.save(new Pet(4, "Douglas", "Cat", ownerRepository.findById(4).get()));
            petRepository.save(new Pet(5, "Eden", "Hamster", ownerRepository.findById(1).get()));
            petRepository.save(new Pet(6, "Florence", "Dog", ownerRepository.findById(2).get()));
            petRepository.save(new Pet(7, "Gabriel", "Cat", ownerRepository.findById(4).get()));
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(500,
                    "Null pointer found! Remember to do /owner/createDummy first",
                    null);
        }
    }

    @PostMapping(path = "/pets/add")
    public Pet addPet(@RequestBody Pet pet) {
        pet.setId(null);
        return petRepository.save(pet);
    }
    @GetMapping(path = "/pets")
    public List<Pet> getPets(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "species", required = false) String species,
            @RequestParam(name = "ownerId", required = false) Integer ownerId
            ) {
        List<Pet> petList = petRepository.findAll();
        if (name != null)
            petList.removeIf(pet -> !name.equals(pet.getName()));
        if (species != null)
            petList.removeIf(pet -> !species.equals(pet.getSpecies()));
        if (ownerId != null)
            petList.removeIf(pet -> !ownerId.equals(pet.getOwner().getId()));
        return petList;
    }

    @GetMapping(path = "/pets/{id}")
    public Pet getPetById(@PathVariable(name = "id") int id) {
        return petRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(404, "No such pet :(", null));
    }

    @Transactional
    @PutMapping(path = "/pets/{id}")
    public void putPetById(@PathVariable(name = "id") int id, @RequestBody Pet newPet) {
        Pet pet = getPetById(id);
        pet.setName(newPet.getName());
        pet.setSpecies(newPet.getSpecies());
        pet.setOwner(newPet.getOwner());

    }

    @Transactional
    @PatchMapping(path = "/pets/{id}")
    public Pet patchPetById(@PathVariable(name = "id") int id, @RequestBody Pet newPet) {
        Pet pet = getPetById(id);
        if (newPet.getName() != null)
            pet.setName(newPet.getName());
        if (newPet.getSpecies() != null)
            pet.setSpecies(newPet.getSpecies());
        if (newPet.getOwner() != null)
            pet.setOwner(newPet.getOwner());
        return pet;
    }
}
