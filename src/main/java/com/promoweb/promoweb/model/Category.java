package com.promoweb.promoweb.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="category_id", updatable = false)
    //@OneToMany(mappedBy = "category_id")
    private Long id;
    @NotBlank
    @Column(name = "name")
    private String name;
 //   @OneToMany(mappedBy = "category_id")
  //  private Set<Product> products = new HashSet<>();

    public Category() {
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
