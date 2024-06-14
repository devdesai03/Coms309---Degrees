package org.springframework.samples.petclinic.pet;

import javax.persistence.*;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.core.style.ToStringCreator;
import org.springframework.samples.petclinic.owner.Owners;

@Entity
@Table(name = "pet")
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @NotFound(action = NotFoundAction.IGNORE)
    private Integer id;

    @Column(name = "name")
    @NotFound(action = NotFoundAction.IGNORE)
    private String name;

    @Column(name = "species")
    @NotFound(action = NotFoundAction.IGNORE)
    private String species;

    @ManyToOne
    @JoinColumn(name = "owner")
    @NotFound(action = NotFoundAction.IGNORE)
    private Owners owner;

    public Pet() {

    }

    public Pet(int id, String name, String species, Owners owner){
        this.id = id;
        this.name = name;
        this.species = species;
        this.owner = owner;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public boolean isNew() {
        return this.id == null;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecies() {
        return this.species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public Owners getOwner() {
        return this.owner;
    }

    public void setOwner(Owners owner) {
        this.owner = owner;
    }


    @Override
    public String toString() {
        return new ToStringCreator(this)

                .append("id", this.getId())
                .append("new", this.isNew())
                .append("name", this.getName())
                .append("species", this.getSpecies())
                .append("owner", this.getOwner()).toString();
    }
}
