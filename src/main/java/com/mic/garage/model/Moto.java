package com.mic.garage.model;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
public class Moto implements Serializable {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private int times;

    public Moto() {
    }

    public Moto(int times) {
        this.times = times;
    }

    public int getTimes() {
        return this.times;
    }
    public void setTimes(int times){
        this.times = times;
    }

    @Override
    public String toString() {
        return "Moto{" +
                "id=" + id +
                ", times=" + times +
                '}';
    }
}