package com.codeit.project.slid_todo.domain.sample.web.controller;

import com.codeit.project.slid_todo.domain.sample.business.SampleFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SampleController {
    private final SampleFacade sampleFacade;


}
