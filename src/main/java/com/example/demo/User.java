package com.example.demo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import java.util.Objects;

@SuppressWarnings("JpaDataSourceORMInspection")
@Entity
@Table(name="users", schema="public")
public class User {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @Column(length=50, nullable=false)
    private String username;

    @Column(length=50, nullable=false)
    private String password;

    @Column(length=50, nullable=false)
    @Enumerated(EnumType.STRING)
    private UserType userType;

    public User(String username, String password, UserType userType) {
        this.username = username;
        this.password = password;
        this.userType = userType;
    }

    public User(CreateUserRequestDto userRequestDto) {
        this.username = userRequestDto.getUsername();
        this.password = userRequestDto.getPassword();
        this.userType = userRequestDto.getUserType();
    }

    public User() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return Objects.equals(id, user.id) && username.equals(user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, userType);
    }

    static final class CreateUserRequestDto {
        String username;
        String password;
        UserType userType;

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }

        public UserType getUserType() {
            return userType;
        }

        @Override
        public String toString() {
            return "CreateUserRequestDto{" +
                    "userName='" + username + '\'' +
                    ", password='" + password + '\'' +
                    ", userType=" + userType +
                    '}';
        }
    }
}
