package com.codeit.project.slid_todo.domain.user.business.service;

import com.codeit.project.slid_todo.domain.user.persistent.entity.User;
import com.codeit.project.slid_todo.domain.user.persistent.entity.enums.UserRole;
import com.codeit.project.slid_todo.domain.user.persistent.repository.DomainUserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional(rollbackOn = Exception.class)
@RequiredArgsConstructor
public class UserService {

    private final DomainUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void registerUser(String name, String email, String password) {
        validateDuplicateEmail(email);

        String encodedPassword = passwordEncoder.encode(password);
        User user = User.builder()
                .name(name)
                .email(email)
                .password(encodedPassword)
                .userRole(UserRole.USER)
                .is_deleted(false)
                .build();

        userRepository.save(user);
    }

    private void validateDuplicateEmail(String email) {
        userRepository.assertEmailNotExists(email);
    }

}
