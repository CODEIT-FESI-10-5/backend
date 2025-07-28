//package com.codeit.project.slid_todo;
//
//import com.codeit.project.slid_todo.domain.study.persistent.entity.Study;
//import com.codeit.project.slid_todo.domain.studyUser.persistent.entity.StudyUser;
//import com.codeit.project.slid_todo.domain.studyUser.persistent.entity.enums.UserRole;
//import com.codeit.project.slid_todo.domain.user.persistent.entity.User;
//import com.codeit.project.slid_todo.domain.user.persistent.repository.DomainUserRepository;
//import com.codeit.project.slid_todo.domain.study.persistent.repository.DomainStudyRepository;
//import com.codeit.project.slid_todo.domain.studyUser.persistent.repository.DomainStudyUserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Profile;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//
//@Component
//@Profile("test")
//public class TestDataSetup implements CommandLineRunner {
//
//    @Autowired
//    private DomainUserRepository userRepository;
//    @Autowired
//    private DomainStudyRepository studyRepository;
//    @Autowired
//    private DomainStudyUserRepository studyUserRepository;
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    @Override
//    public void run(String... args) throws Exception {
//        // 테스트용 사용자 생성
//        User testUser = User.builder()
//                .nickname("테스트 사용자")
//                .email("test@example.com")
//                .password(passwordEncoder.encode("password123"))
//                .build();
//        userRepository.save(testUser);
//
//        // 테스트용 스터디 생성
//        Study testStudy = Study.builder()
//                .title("테스트 스터디")
//                .description("테스트용 스터디입니다.")
//                .inviteCode("TEST123")
//                .build();
//        studyRepository.save(testStudy);
//
//        // 스터디장으로 사용자 등록
//        StudyUser studyUser = StudyUser.builder()
//                .study(testStudy)
//                .user(testUser)
//                .userRole(UserRole.LEADER)
//                .build();
//        studyUserRepository.save(studyUser);
//    }
//}