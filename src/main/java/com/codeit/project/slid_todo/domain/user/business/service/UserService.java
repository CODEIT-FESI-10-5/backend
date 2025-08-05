package com.codeit.project.slid_todo.domain.user.business.service;

import com.codeit.project.slid_todo.common.exception.BaseException;
import com.codeit.project.slid_todo.common.vo.UploadImg;
import com.codeit.project.slid_todo.domain.user.errorCode.UserErrorCode;
import com.codeit.project.slid_todo.domain.user.persistent.entity.User;
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

    public void registerUser(String nickname, String email, String password) {
        validateDuplicateEmail(email);

        validateDuplicateNickname(nickname);

        String encodedPassword = passwordEncoder.encode(password);
        User user = User.builder()
                .nickname(nickname)
                .email(email)
                .password(encodedPassword)
                .build();

        userRepository.save(user);
    }

    private void validateDuplicateEmail(String email) {
        userRepository.assertEmailNotExists(email);
    }

    private void validateDuplicateNickname(String nickname) {
        userRepository.assertNickNameNotExists(nickname);
    }

    public void changePassword(User user, String currentPassword, String newPassword) {
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new BaseException(UserErrorCode.PASSWORD_NOT_MATCH);
        }
        String encodedNewPassword = passwordEncoder.encode(newPassword);
        user.updatePassword(encodedNewPassword);
    }

    public User updateProfileImg(User user, UploadImg UploadImg) {
        user.updateProfileImg(UploadImg);
        return user;
    }

     public User updateNickname(User user, String nickname) {
        validateDuplicateNickname(user.getId(), nickname);
        user.updateNickname(nickname);
        return user;
    }

    private boolean validateDuplicateNickname(Long userId, String nickname) {
        User user = userRepository.getByNicknameOrThrow(nickname);
        return user == null || user.getId().equals(userId);
    }

    public User findUserById(Long userId) {
        return userRepository.getByIdOrThrow(userId);
    }

    public User findByEmail(String email) {
        return userRepository.getByEmailOrThrow(email);
    }
}
