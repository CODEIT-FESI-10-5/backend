package com.codeit.project.slid_todo.domain.todo.business.service;

import com.codeit.project.slid_todo.common.exception.BaseException;
import com.codeit.project.slid_todo.domain.goal.persistent.entity.Goal;
import com.codeit.project.slid_todo.domain.goal.persistent.repository.DomainGoalRepository;
import com.codeit.project.slid_todo.domain.studyUser.persistent.entity.StudyUser;
import com.codeit.project.slid_todo.domain.studyUser.persistent.repository.DomainStudyUserRepository;
import com.codeit.project.slid_todo.domain.todo.errorCode.TodoErrorCode;
import com.codeit.project.slid_todo.domain.todo.persistent.entity.Todo;
import com.codeit.project.slid_todo.domain.todo.persistent.repository.DomainTodoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(rollbackOn = Exception.class)
@RequiredArgsConstructor
public class TodoService {

    private final DomainTodoRepository todoRepository;
    private final DomainGoalRepository goalRepository;
    private final DomainStudyUserRepository studyUserRepository;

    private static final int MAX_TODOS_PER_GOAL = 10; // 투두 최대 개수 제한

    public Todo createTodo(Long goalId, Long assignedUserId, String content, boolean shared) {
        Goal goal = goalRepository.getByIdOrThrow(goalId);
        StudyUser assignedUser = studyUserRepository.getByIdOrThrow(assignedUserId);
        
        // 투두 개수 제한 확인
        long currentTodoCount = todoRepository.countByGoalId(goalId);
        if (currentTodoCount >= MAX_TODOS_PER_GOAL) {
            throw new BaseException(TodoErrorCode.TODO_LIMIT_EXCEEDED);
        }

        Todo todo = Todo.builder()
                .goal(goal)
                .assignedUser(assignedUser)
                .content(content)
                .shared(shared)
                .build();

        todoRepository.save(todo);
        return todo;
    }

    public List<Todo> createSharedTodosForAllMembers(Long goalId, String content) {
        Goal goal = goalRepository.getByIdOrThrow(goalId);
        List<StudyUser> studyUsers = studyUserRepository.findByStudyId(goal.getStudy().getId());
        
        List<Todo> createdTodos = new ArrayList<>();
        
        for (StudyUser studyUser : studyUsers) {
            Todo todo = Todo.builder()
                    .goal(goal)
                    .assignedUser(studyUser)
                    .content(content)
                    .shared(true)
                    .build();
            
            todoRepository.save(todo);
            createdTodos.add(todo);
        }
        
        return createdTodos;
    }

    public Todo getTodoById(Long todoId) {
        return todoRepository.getByIdOrThrow(todoId);
    }

    public List<Todo> getTodosByGoalIdAndUserId(Long goalId, Long userId) {
        return todoRepository.findByGoalIdAndUserId(goalId, userId);
    }

    public void updateTodoContentAndCompleted(Long todoId, String content, boolean completed) {
        Todo todo = todoRepository.getByIdOrThrow(todoId);
        todo.updateContent(content);
        
        if (completed != todo.isCompleted()) {
            todo.toggleComplete();
        }
        
        todoRepository.save(todo);
    }

    public long countCompletedByGoalIdAndUserId(Long goalId, Long userId) {
        return todoRepository.countCompletedByGoalIdAndUserId(goalId, userId);
    }

    public long countByGoalIdAndUserId(Long goalId, Long userId) {
        return todoRepository.countByGoalIdAndUserId(goalId, userId);
    }
} 