package com.example.schedule.schedule.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Getter
@SQLDelete(sql = "UPDATE schedules SET deleted = true WHERE id = ?")
@Entity
@Table(name = "schedules")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Schedule extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userName;
//    @ManyToOne(fetch = FetchType.LAZY, optional = false)
//    @JoinColumn(name = "user_name", nullable = false)
//    private User user;
////    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "FK_SCHEDULE_USER", value = ConstraintMode.CONSTRAINT))
////    private String userName;
    private String title;
    private String content;
    private boolean deleted = false;

    public Schedule(String userName, String title, String content) {
        this.userName = userName;
        this.title = title;
        this.content = content;
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
