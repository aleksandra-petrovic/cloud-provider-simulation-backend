package com.example.demo.services;

import com.example.demo.model.Permission;
import com.example.demo.repositories.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public List<Permission> findPermissions(List<String> permisions){
        List<Permission> permissionList = new ArrayList<>();
        for(String p : permisions) permissionList.add(this.permissionRepository.findByPermission(p));
        return permissionList;
    }

}
