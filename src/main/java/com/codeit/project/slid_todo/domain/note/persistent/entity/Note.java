package com.codeit.project.slid_todo.domain.note.persistent.entity;

import com.codeit.project.slid_todo.common.audting.BaseDateTime;
import com.codeit.project.slid_todo.domain.todo.persistent.entity.Todo;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Note extends BaseDateTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "todo_id")
    private Todo todo;

    @Column(columnDefinition = "TEXT")
    private String content;

    private boolean isDeleted = false;

    @Builder
    public Note(Todo todo, String content) {
        addTodo(todo);
        this.content = content;
    }

    private void addTodo(Todo todo) {
        if (todo != null) {
            this.todo = todo;
            todo.setNote(this);
        }
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void delete() {
        this.isDeleted = true;
    }
} 