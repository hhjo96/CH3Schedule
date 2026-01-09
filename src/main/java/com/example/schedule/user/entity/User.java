package com.example.schedule.user.entity;

import com.example.schedule.schedule.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

import java.time.LocalDateTime;

@Getter
@SQLDelete(sql = "UPDATE users SET deleted = true WHERE id = ?")
@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false, unique = true)
    private String email;
    private boolean deleted = false;
    @Column(nullable = false)
    private String password;


    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public void update(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public String displayDeletedUserName() {
        return deleted? "탈퇴한 사용자" : name;
    }

    public void delete() {
        deleted = true;
        email = "deleted_"+ LocalDateTime.now().toString().substring(0, 19) + email;
    }

}
