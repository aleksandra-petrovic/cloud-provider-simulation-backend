package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
public class ErrorMsg {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long errorId;
    @Column
    private Date date;
    @Column
    private Long machineId;
    @Column
    private String operation;
    @Column
    private String description;
}
