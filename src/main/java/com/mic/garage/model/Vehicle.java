package com.mic.garage.model;


import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "Vehicles")
public class Vehicle implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String brand;
    private int carYear;
    private int carEngine;


    public Vehicle() {
    }

    public Vehicle(String brand, int carYear, int carEngine) {
        this.carYear = carYear;
        this.carEngine = carEngine;
        this.brand = brand;
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getCarYear() {
        return carYear;
    }

    public void setCarYear(int carYear) {
        this.carYear = carYear;
    }

    public int getCarEngine() {
        return carEngine;
    }

    public void setCarEngine(int carEngine) {
        this.carEngine = carEngine;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }


    @Override
    public String toString() {
        return "Vehicle{" +
                "id=" + id +
                ", carYear=" + carYear +
                ", carEngine=" + carEngine +
                ", brand='" + brand + '\'' +
                '}';
    }
}
