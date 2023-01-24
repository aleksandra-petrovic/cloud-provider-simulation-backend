package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
public class Machine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long machineId;
    @Column
    private String name;
    @Column(nullable = false)
    private Status status;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "CREATED_BY", referencedColumnName = "USERID")
    private User user;
    @Column
    private boolean active;
    @Column
    private Date dateCreated;
    @Column
    @Version
    private Integer version;
}
