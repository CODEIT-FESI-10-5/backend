package com.codeit.project.slid_todo.domain.todo.persistent.entity;

import com.codeit.project.slid_todo.common.audting.BaseDateTime;
import com.codeit.project.slid_todo.domain.goal.persistent.entity.Goal;
import com.codeit.project.slid_todo.domain.note.persistent.entity.Note;
import com.codeit.project.slid_todo.domain.studyUser.persistent.entity.StudyUser;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Todo extends BaseDateTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goal_id")
    private Goal goal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_user_id")
    private StudyUser assignedUser;

    @Column(nullable = false)
    private String content;

    private boolean completed = false;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    private boolean shared = false;

    private boolean isDeleted = false;

    @OneToOne(mappedBy = "todo", cascade = CascadeType.ALL, orphanRemoval = true)
    private Note note;

    @Builder
    public Todo(Goal goal, StudyUser assignedUser, String content, boolean shared) {
        addGoal(goal);
        addAssignedUser(assignedUser);
        this.content = content;
        this.shared = shared;
    }

    private void addGoal(Goal goal) {
        if (goal != null) {
            this.goal = goal;
            goal.getTodos().add(this);
        }
    }

    private void addAssignedUser(StudyUser assignedUser) {
        if (assignedUser != null) {
            this.assignedUser = assignedUser;
        }
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void toggleComplete() {
        this.completed = !this.completed;
        this.completedAt = this.completed ? LocalDateTime.now() : null;
    }

    public void updateShared(boolean shared) {
        this.shared = shared;
    }

    public void delete() {
        this.isDeleted = true;
    }

    public void setNote(Note note) {
        this.note = note;
    }
} 