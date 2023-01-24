package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
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
//    @JsonIgnore
//    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private List<Permission> permissions = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    //@JsonIgnore
    //@ToString.Exclude
    private List<Machine> machines;

}
