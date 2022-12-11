package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long permissionId;

    @Column
    private String permission;

    @ManyToMany
    @JoinTable(
            name = "USERS_PERMISSIONS",
            joinColumns = @JoinColumn(name = "PERMISSION_ID", referencedColumnName = "PERMISSIONID"),
            inverseJoinColumns = @JoinColumn(name = "USER_ID", referencedColumnName = "USERID")
    )
    private List<User> users = new ArrayList<>();

}
