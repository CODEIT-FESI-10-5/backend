package com.codeit.project.slid_todo.domain.todo.business.service;

import com.codeit.project.slid_todo.common.exception.BaseException;
import com.codeit.project.slid_todo.domain.goal.persistent.entity.Goal;
import com.codeit.project.slid_todo.domain.goal.persistent.repository.DomainGoalRepository;
import com.codeit.project.slid_todo.domain.studyUser.persistent.entity.StudyUser;
import com.codeit.project.slid_todo.domain.studyUser.persistent.repository.DomainStudyUserRepository;
import com.codeit.project.slid_todo.domain.todo.errorCode.TodoErrorCode;
import com.codeit.project.slid_todo.domain.todo.persistent.entity.Todo;
import com.codeit.project.slid_todo.domain.todo.persistent.repository.DomainTodoRepository;
import com.codeit.project.slid_todo.domain.note.persistent.entity.Note;
import com.codeit.project.slid_todo.domain.note.persistent.repository.DomainNoteRepository;
import com.codeit.project.slid_todo.domain.note.business.service.NoteService;
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
    private final DomainNoteRepository noteRepository;
    private final NoteService noteService;

    private static final int MAX_TODOS_PER_GOAL = 10; // 투두 최대 개수 제한

    public Todo createTodo(Long goalId, Long assignedUserId, String content, boolean shared) {
        Goal goal = goalRepository.getByIdOrThrow(goalId);
        StudyUser assignedUser = studyUserRepository.getByIdOrThrow(assignedUserId);
        
        // 사용자별 투두 개수 제한 확인
        long currentTodoCount = todoRepository.countByGoalIdAndUserIdAndNotDeleted(goalId, assignedUser.getUser().getId());
        if (currentTodoCount >= MAX_TODOS_PER_GOAL) {
            throw new BaseException(TodoErrorCode.TODO_LIMIT_EXCEEDED);
        }

        // 새로운 우선순위 계산 (사용자별 최대값 + 1)
        Integer newPriorityOrder = calculateNewPriorityOrderByUserId(goalId, assignedUser.getUser().getId());

        Todo todo = Todo.builder()
                .goal(goal)
                .assignedUser(assignedUser)
                .content(content)
                .shared(shared)
                .priorityOrder(newPriorityOrder)
                .build();

        todoRepository.save(todo);
        
        // 투두 생성 시 빈 노트도 함께 생성
        Note note = Note.builder()
                .todo(todo)
                .content("")
                .build();
        noteRepository.save(note);
        
        return todo;
    }

    public List<Todo> createSharedTodosForAllMembers(Long goalId, String content) {
        Goal goal = goalRepository.getByIdOrThrow(goalId);
        List<StudyUser> studyUsers = studyUserRepository.findByStudyId(goal.getStudy().getId());
        
        List<Todo> createdTodos = new ArrayList<>();
        
        for (StudyUser studyUser : studyUsers) {
            // 각 사용자별로 개수 제한 확인
            long currentTodoCount = todoRepository.countByGoalIdAndUserIdAndNotDeleted(goalId, studyUser.getUser().getId());
            if (currentTodoCount >= MAX_TODOS_PER_GOAL) {
                throw new BaseException(TodoErrorCode.TODO_LIMIT_EXCEEDED);
            }
            
            // 각 사용자별로 새로운 우선순위 계산
            Integer newPriorityOrder = calculateNewPriorityOrderByUserId(goalId, studyUser.getUser().getId());
            
            Todo todo = Todo.builder()
                    .goal(goal)
                    .assignedUser(studyUser)
                    .content(content)
                    .shared(true)
                    .priorityOrder(newPriorityOrder)
                    .build();
            
            todoRepository.save(todo);
            
            // 공통 투두 생성 시에도 빈 노트 함께 생성
            Note note = Note.builder()
                    .todo(todo)
                    .content("")
                    .build();
            noteRepository.save(note);
            
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

    public void updateTodoContentAndCompleted(Long todoId, String content, Boolean completed) {
        Todo todo = todoRepository.getByIdOrThrow(todoId);
        todo.updateContent(content);
        
        // completed가 null이 아닌 경우에만 처리
        if (completed != null && completed != todo.isCompleted()) {
            boolean wasCompleted = todo.isCompleted();
            todo.toggleComplete();
            
            // 완료 취소 처리 (completed: false)인 경우에만 우선순위 조정
            if (wasCompleted && !completed) {
                adjustPriorityWhenUncomplete(todo);
            }
            // 완료 처리 (completed: true)인 경우는 우선순위 건드리지 않음
        }
        
        todoRepository.save(todo);
    }

    public long countCompletedByGoalIdAndUserId(Long goalId, Long userId) {
        return todoRepository.countCompletedByGoalIdAndUserId(goalId, userId);
    }

    public long countByGoalIdAndUserId(Long goalId, Long userId) {
        return todoRepository.countByGoalIdAndUserId(goalId, userId);
    }

    public long countCompletedByStudyId(Long studyId) {
        return todoRepository.countCompletedByStudyId(studyId);
    }

    public long countByStudy(Long studyId) {
        return todoRepository.countByStudy(studyId);
    }

    public List<Todo> getRecentCompletedTodosByGoalIdAndUserId(Long goalId, Long userId) {
        return todoRepository.findRecentCompletedTodosByGoalIdAndUserId(goalId, userId);
    }

    public List<Todo> getInProgressTodosByGoalIdAndUserId(Long goalId, Long userId) {
        return todoRepository.findInProgressTodosByGoalIdAndUserIdOrderByPriority(goalId, userId);
    }

    public List<Todo> getTodosByGoalId(Long goalId) {
        return todoRepository.findByGoalId(goalId);
    }

    public void save(Todo todo) {
        todoRepository.save(todo);
    }

    public void updateTodoPriority(Long todoId, Integer newPriorityOrder) {
        Todo todo = todoRepository.getByIdOrThrow(todoId);
        Long goalId = todo.getGoal().getId();
        Long userId = todo.getAssignedUser().getUser().getId();
        
        // 우선순위 변경 시 같은 사용자의 다른 투두들의 우선순위 조정
        adjustPriorityOrdersByUserId(goalId, userId, todo.getPriorityOrder(), newPriorityOrder);
        
        // 해당 투두의 우선순위 업데이트
        todo.updatePriorityOrder(newPriorityOrder);
        todoRepository.save(todo);
    }

    private Integer calculateNewPriorityOrder(Long goalId) {
        Integer maxPriorityOrder = todoRepository.findMaxPriorityOrderByGoalId(goalId);
        return maxPriorityOrder == null ? 1 : maxPriorityOrder + 1;
    }

    private Integer calculateNewPriorityOrderByUserId(Long goalId, Long userId) {
        Integer maxPriorityOrder = todoRepository.findMaxPriorityOrderByGoalIdAndUserId(goalId, userId);
        return maxPriorityOrder == null ? 1 : maxPriorityOrder + 1;
    }

    private void adjustPriorityOrders(Long goalId, Integer newPriorityOrder) {
        // 새로운 우선순위 이상인 투두들을 조회
        List<Todo> todosToAdjust = todoRepository.findByGoalIdAndPriorityOrderGreaterThanEqual(goalId, newPriorityOrder);
        
        // 우선순위를 하나씩 증가
        for (Todo todo : todosToAdjust) {
            todo.updatePriorityOrder(todo.getPriorityOrder() + 1);
            todoRepository.save(todo);
        }
    }

    private void adjustPriorityOrdersByUserId(
            Long goalId, Long userId,Integer asIsPriorityOrder, Integer newPriorityOrder
    ) {
        if(asIsPriorityOrder < newPriorityOrder) {
            // 예시
            // asIsPriorityOrder = 2
            // newPriorityOrder = 4
            // 2->4 이동시 3번보다 크고 4번보다 같거나 작은 모든 목록 조회
            List<Todo> todosToAdjust = todoRepository.findByGoalIdAndUserIdAndPriorityOrderBetween(goalId, userId, asIsPriorityOrder + 1, newPriorityOrder);

            // 우선순위를 하나씩 감소
            for (Todo todo : todosToAdjust) {
                todo.updatePriorityOrder(todo.getPriorityOrder() - 1);
                todoRepository.save(todo);
            }
        }else if(asIsPriorityOrder > newPriorityOrder){
            // 예시
            // asIsPriorityOrder = 4
            // newPriorityOrder = 2
            // 4->2 이동시 2번보다 크거나 같고 4번보다 작은 목록 조회
            List<Todo> todosToAdjust = todoRepository.findByGoalIdAndUserIdAndPriorityOrderBetween(goalId, userId, newPriorityOrder, asIsPriorityOrder - 1);
        
        // 우선순위를 하나씩 증가
        for (Todo todo : todosToAdjust) {
            todo.updatePriorityOrder(todo.getPriorityOrder() + 1);
            todoRepository.save(todo);
            }
        }
    }

    private void adjustPriorityWhenUncomplete(Todo todo) {
        Long goalId = todo.getGoal().getId();
        
        // 목표 전체에서 통합된 우선순위 계산 (사용자 구분 없음)
        Integer maxPriorityOrder = todoRepository.findMaxPriorityOrderByGoalIdExcludingTodo(goalId, todo.getId());
        
        // 취소할 투두가 이미 가장 뒤에 있으면 아무것도 하지 않음
        if (maxPriorityOrder == null || todo.getPriorityOrder() > maxPriorityOrder) {
            return;
        }
        
        // 취소할 투두의 우선순위를 목표 전체 미완료 투두 중 가장 낮은 우선순위로 설정
        Integer newPriorityOrder = maxPriorityOrder;
        
        // 원래 우선순위 저장
        Integer originalPriorityOrder = todo.getPriorityOrder();
        
        // 목표 전체에서 취소할 투두보다 높은 우선순위를 가진 투두들의 우선순위를 -1씩 조정
        List<Todo> todosToAdjust = todoRepository.findByGoalIdAndPriorityOrderGreaterThan(goalId, originalPriorityOrder);
        
        for (Todo todoToAdjust : todosToAdjust) {
            todoToAdjust.updatePriorityOrder(todoToAdjust.getPriorityOrder() - 1);
            todoRepository.save(todoToAdjust);
        }
        
        // 마지막에 취소할 투두의 우선순위를 목표 전체 미완료 투두 중 가장 낮은 우선순위로 설정
        todo.updatePriorityOrder(newPriorityOrder);
        todoRepository.save(todo);
    }

    public void deleteTodo(Long todoId, Long userId) {
        Todo todo = todoRepository.getByIdOrThrow(todoId);
        
        // 투두가 현재 사용자의 것인지 확인
        if (!todo.getAssignedUser().getUser().getId().equals(userId)) {
            throw new RuntimeException("자신의 투두만 삭제할 수 있습니다.");
        }
        
        // 삭제할 투두보다 높은 우선순위를 가진 투두들의 우선순위를 -1씩 조정
        adjustPriorityWhenDelete(todo);
        
        // 투두와 관련된 노트를 먼저 하드 삭제
        if (todo.getNote() != null) {
            noteRepository.delete(todo.getNote());
        }
        
        // 투두 하드 삭제
        todoRepository.delete(todo);
    }

    private void adjustPriorityWhenDelete(Todo todo) {
        Long goalId = todo.getGoal().getId();
        
        // 삭제할 투두보다 높은 우선순위를 가진 투두들의 우선순위를 -1씩 조정 (목표 전체에서 통합)
        List<Todo> todosToAdjust = todoRepository.findByGoalIdAndPriorityOrderGreaterThan(goalId, todo.getPriorityOrder());
        
        for (Todo todoToAdjust : todosToAdjust) {
            todoToAdjust.updatePriorityOrder(todoToAdjust.getPriorityOrder() - 1);
            todoRepository.save(todoToAdjust);
        }
    }
} 