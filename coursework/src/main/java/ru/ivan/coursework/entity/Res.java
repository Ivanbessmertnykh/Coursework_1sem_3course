package ru.ivan.coursework.entity;


import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Res {
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    private int id;
    private int numberOfTime;

    public Res() {

    }

    public Res(int id, int numberOfTime){
        this.id = id;
        this.numberOfTime = numberOfTime;
    }

    public int getId() {
        return id;
    }

    public Res(int numberOfTime){
        this.numberOfTime = numberOfTime;
    }

    public void setId(int id) {
        this.id = id;
    }
}
