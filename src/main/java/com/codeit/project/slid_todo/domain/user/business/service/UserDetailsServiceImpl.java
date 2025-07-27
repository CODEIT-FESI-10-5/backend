package com.codeit.project.slid_todo.domain.user.business.service;

import com.codeit.project.slid_todo.common.security.vo.CustomUserDetails;
import com.codeit.project.slid_todo.domain.user.persistent.entity.User;
import com.codeit.project.slid_todo.domain.user.persistent.repository.DomainUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final DomainUserRepository domainUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User findUser = domainUserRepository.getByEmailOrThrow(username);
        return CustomUserDetails.builder()
                .id(findUser.getId())
                .email(findUser.getEmail())
                .password(findUser.getPassword())
                .nickname(findUser.getNickname())
                .imgDir(
                        findUser.getImg() != null ?
                                findUser.getImg().getStoreImgDir()
                                : ""
                )
                .build();
    }
}
