package com.codeit.project.slid_todo.domain.goal.persistent.entity;

import com.codeit.project.slid_todo.common.audting.BaseDateTime;
import com.codeit.project.slid_todo.domain.study.persistent.entity.Study;
import com.codeit.project.slid_todo.domain.todo.persistent.entity.Todo;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Goal extends BaseDateTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_id")
    private Study study;

    @Column(nullable = true)
    private String title;

    @Column(name = "priority_order")
    private String priorityOrder; // "1,2,5,4,3,6,7,8,9,10" 형태로 저장

    private boolean isDeleted = false;

    @OneToMany(mappedBy = "goal")
    private List<Todo> todos = new ArrayList<>();

    @Builder
    public Goal(Study study, String title, String priorityOrder) {
        addStudy(study);
        this.title = title;
        this.priorityOrder = priorityOrder;
    }

    private void addStudy(Study study) {
        if (study != null) {
            this.study = study;
            study.getGoals().add(this);
        }
    }

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updatePriorityOrder(String priorityOrder) {
        this.priorityOrder = priorityOrder;
    }

    public void delete() {
        this.isDeleted = true;
    }
} 