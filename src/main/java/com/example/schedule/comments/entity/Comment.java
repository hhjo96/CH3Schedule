package com.example.schedule.comments.entity;

import com.example.schedule.schedule.entity.Schedule;
import com.example.schedule.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

@Getter
@SQLDelete(sql = "UPDATE comments SET deleted = true WHERE id = ?")
@Entity
@Table(name = "comments")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //작성한 유저가 null일 수 없으므로 optional과 nullable false
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "FK_COMMENT_USER", value = ConstraintMode.CONSTRAINT))
    private User user;

    //일정이 null일 수 없으므로 optional과 nullable false
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "schedule_id", nullable = false, foreignKey = @ForeignKey(name = "FK_COMMENT_SCHEDULE", value = ConstraintMode.CONSTRAINT))
    private Schedule schedule;

    private String content;

    @Column(nullable = false)
    private boolean deleted = false; // soft delete

    public Comment(User user, Schedule schedule, String content) {
        this.user = user;
        this.schedule = schedule;
        this.content = content;
    }

    public void update( String content) {
        this.content = content;
    }

    public void delete() {
        this.deleted = true;
    }
}
