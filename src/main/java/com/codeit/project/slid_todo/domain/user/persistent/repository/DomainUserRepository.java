package com.codeit.project.slid_todo.domain.user.persistent.repository;

import com.codeit.project.slid_todo.common.exception.BaseException;
import com.codeit.project.slid_todo.domain.user.errorCode.UserErrorCode;
import com.codeit.project.slid_todo.domain.user.persistent.entity.User;
import com.codeit.project.slid_todo.domain.user.persistent.repository.jpaRepository.JpaUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class DomainUserRepository {

    private final JpaUserRepository jpaUserRepository;

    public void assertEmailNotExists(String email) {
        jpaUserRepository.findByEmailAndIsDeletedFalse(email)
                .ifPresent(user -> {
                    throw new BaseException(UserErrorCode.EMAIL_ALREADY_EXISTS);
                });
    }

    public void assertNickNameNotExists(String nickname) {
        jpaUserRepository.findByNicknameAndIsDeletedFalse(nickname)
                .ifPresent(user -> {
                    throw new BaseException(UserErrorCode.NICKNAME_ALREADY_EXISTS);
                });
    }

    public User getByEmailOrThrow(String email) {
        return jpaUserRepository.findByEmailAndIsDeletedFalse(email)
                .orElseThrow(() -> new BaseException(UserErrorCode.NOT_EXIST_USER));
    }

    public User getByNicknameOrThrow(String nickname) {
        return jpaUserRepository.findByNicknameAndIsDeletedFalse(nickname)
                .orElse(null);
    }

    public User findByEmail(String email) {
        return jpaUserRepository.findByEmailAndIsDeletedFalse(email).orElse(null);
    }

    public User getByIdOrThrow(Long id) {
        return jpaUserRepository.findById(id)
                .orElseThrow(() -> new BaseException(UserErrorCode.NOT_EXIST_USER));
    }

    public void save(User user) {
        jpaUserRepository.save(user);
    }

}
