package com.codeit.project.slid_todo.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckStudyLeader {
    String studyIdParam() default "studyId";
    String userIdParam() default "userId";
}
