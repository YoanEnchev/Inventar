package com.inventar.app.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "type_id", nullable = false)
    private ProductType type;

    private String name;
    private int year;
    private int amorthizationIndex;
    private String description;
    private boolean isDeleted;
    private boolean isAvailable;

    public Product(ProductType type, String name, int year, int amorthizationIndex, String description, boolean isDeleted, boolean isAvailable) {
        this.type = type;
        this.name = name;
        this.year = year;
        this.amorthizationIndex = amorthizationIndex;
        this.description = description;
        this.isDeleted = isDeleted;
        this.isAvailable = isAvailable;
    }

    public Product() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProductType getType() {
        return type;
    }

    public void setType(ProductType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getAmorthizationIndex() {
        return amorthizationIndex;
    }

    public void setAmorthizationIndex(int amorthizationIndex) {
        this.amorthizationIndex = amorthizationIndex;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean getIsDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public boolean equals(Object o) {

        Product prod = (Product) o;

        return this.getId().equals(prod.getId());
    }
}
