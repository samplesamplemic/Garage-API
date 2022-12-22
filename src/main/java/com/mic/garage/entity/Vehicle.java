package com.mic.garage.entity;


import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

import java.util.Objects;

//@Entity
//@Table(name = "Vehicles")
//@Getter
//@Setter
//@ToString
//@EqualsAndHashCode
//@Builder
//@AllArgsConstructor
//@NoArgsConstructor

//Inheritance solutions:
//1)
@MappedSuperclass

//2)
//@Entity
//@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
//@DiscriminatorColumn(name = "vehicle_type", discriminatorType = DiscriminatorType.INTEGER)
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String brand;
    private int vehicleYear;
    private int engine;

    public Vehicle() {
    }

    public Vehicle(String brand, int vehicleYear, int engine) {
        this.vehicleYear = vehicleYear;
        this.engine = engine;
        this.brand = brand;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getVehicleYear() {
        return vehicleYear;
    }

    public void setVehicleYear(int vehicleYear) {
        this.vehicleYear = vehicleYear;
    }

    public int getEngine() {
        return engine;
    }

    public void setEngine(int engine) {
        this.engine = engine;
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
                ", year=" + vehicleYear +
                ", engine capacity=" + engine +
                ", brand='" + brand + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vehicle vehicle = (Vehicle) o;
        if (vehicle.getId().equals(this.id)){
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, brand, vehicleYear, engine);
    }
}
