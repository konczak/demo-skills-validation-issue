package com.example.demo.entity;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Person {
    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    private String name;

    @ElementCollection(fetch = FetchType.EAGER)
    @Size(min = 1, max = 5)
    private Set<String> skills = new HashSet<>();

    protected Person() {
    }

    public Person(final String name, final Collection<String> skills) {
        this.name = name;
        this.skills.addAll(skills);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Set<String> getSkills() {
        return skills;
    }

    public void updateSkills(Collection<String> skills) {
        this.skills.clear();
        this.skills.addAll(skills);
    }

    public void updateName(final String name) {
        this.name = name;
    }
}
