package com.example.spring_crudsecur.service;

import com.example.spring_crudsecur.model.Role;

import java.util.List;

public interface RoleService {

    Role findByName(String name);

    List<Role> findAll();

    void saveRole(Role role);

}
