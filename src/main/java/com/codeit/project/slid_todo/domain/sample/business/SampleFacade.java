package com.codeit.project.slid_todo.domain.sample.business;

import com.codeit.project.slid_todo.domain.sample.business.service.SampleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class SampleFacade {
    private final SampleService sampleService;

}
