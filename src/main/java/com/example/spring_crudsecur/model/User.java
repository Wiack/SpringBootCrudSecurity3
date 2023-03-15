package com.example.spring_crudsecur.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.internal.util.collections.ConcurrentReferenceHashMap;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @NotBlank
    @Column(name = "name")
    private String firstName;
    @NotBlank
    @Column(name = "lastname")
    private String lastName;
    @NotNull
    @Column(name = "age")
    private int age;
    @NotBlank
    @Email
    @Column(name = "username")
    private String email;
    @Column(name = "password")
    private String password;
    @ManyToMany(cascade = //{
            // CascadeType.PERSIST,
            CascadeType.MERGE,//},
            fetch = FetchType.LAZY)
    @JoinTable(name = "users_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;


    public User(String firstName, String lastName, int age, String email, String password, Set<Role> roles) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }


    public String toStringAllRoles() {
        String result = "";

        for (Role role : roles) {
            if (role.getId() == 1) {
                String res = role.getShortName() + " " + result;
                result = res;
            }
            if (role.getId() == 2) {
                result += role.getShortName() + " ";
            }
        }
        return result;
    }


    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
