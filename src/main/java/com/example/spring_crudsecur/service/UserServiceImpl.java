package com.example.spring_crudsecur.service;

import com.example.spring_crudsecur.model.Role;
import com.example.spring_crudsecur.model.User;
import com.example.spring_crudsecur.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleService roleService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public void addUser(User user) {

        // 1 Вариант сохранение пользователя со всеми ролями сразу

        Role USR = roleService.findByName("ROLE_USER");
        Role ADM = roleService.findByName("ROLE_ADMIN");


        if (userRepository.findByEmail(user.getEmail()) == null) {

            Set<Role> roleForSet = new HashSet<>();
            roleForSet.add(USR);
            if (user.getRoles().contains(ADM)) {
                roleForSet.add(ADM);
            }
            ;
            user.setRoles(roleForSet);

            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            userRepository.save(user);
        }

        //2 Вариант сохранение пользователя с одной ролью и обновление пользователя для получения второй роли

//        if (userRepository.findByEmail(user.getEmail()) == null) {
//            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
//            userRepository.save(user);
//        }
//
//        updateUser(user.getId(), user); //добавит админу в set роль юзера
    }

    @Override
    @Transactional
    public void deleteUserById(Long id, Principal principal) {
        if (!principal.getName().equals(getById(id).getEmail())) {
            userRepository.deleteById(id);
        }
    }


    @Transactional
    public void updateUser(Long id, User user) {
        User userForUpdate = getById(id);

        final String USR = "ROLE_USER";
        final String ADM = "ROLE_ADMIN";

        if (user.getRoles().contains(roleService.findByName(ADM))) {
            user.getRoles().add(roleService.findByName(USR));
        }
        if (user.getPassword() == null || user.getPassword().isBlank()) {
            user.setPassword(userForUpdate.getPassword());
        } else {
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        }

        userRepository.save(user);
    }

    @Override
    public User getById(Long id) {
        return userRepository.getById(id);
    }

    @Override
    public User getByEmail(String email) {
        return userRepository.findByEmail(email);
    }

}
