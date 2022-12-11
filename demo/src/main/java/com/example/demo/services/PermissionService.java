package com.example.demo.services;

import com.example.demo.model.Permission;
import com.example.demo.repositories.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionService {

    private final PermissionRepository permissionRepository;

    @Autowired
    public PermissionService(PermissionRepository permissionRepository){
        this.permissionRepository = permissionRepository;
    }

    public Permission findPermission(String permissionName){
        return this.permissionRepository.findByPermission(permissionName);
    }
//
//    public Permission createPermission(String permission){
//        Permission p = new Permission();
//        p.setPermission(permission);
//        return this.permissionRepository.save(p);
//    }

}
