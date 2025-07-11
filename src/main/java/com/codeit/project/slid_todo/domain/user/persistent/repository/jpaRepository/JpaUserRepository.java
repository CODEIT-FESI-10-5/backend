package com.codeit.project.slid_todo.domain.user.persistent.repository.jpaRepository;

import com.codeit.project.slid_todo.domain.user.persistent.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaUserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
