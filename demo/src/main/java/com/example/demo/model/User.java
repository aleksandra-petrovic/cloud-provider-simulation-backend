package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String surname;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @ManyToMany
    @JoinTable(
            name = "USERS_PERMISSIONS",
            joinColumns = @JoinColumn(name = "USER_ID", referencedColumnName = "USERID"),
            inverseJoinColumns = @JoinColumn(name = "PERMISSION_ID", referencedColumnName = "PERMISSIONID")
    )
    @JsonIgnore
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private List<Permission> permissions = new ArrayList<>();


//    @Column
//    private boolean can_read_users;
//
//    @Column
//    private boolean can_update_users;
//
//    @Column
//    private boolean can_delete_users;
//
//    @Column
//    private boolean can_create_users;


}
