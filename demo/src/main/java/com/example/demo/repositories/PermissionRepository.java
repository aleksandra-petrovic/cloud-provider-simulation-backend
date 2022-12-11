package com.example.demo.repositories;

import com.example.demo.model.Permission;
import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<Permission,Long> {
    public Permission findByPermission(String permission);
}
