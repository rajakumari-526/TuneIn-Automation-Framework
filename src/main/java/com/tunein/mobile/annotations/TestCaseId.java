package com.tunein.mobile.annotations;

import java.lang.annotation.*;

@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Repeatable(TestCaseIds.class)
public @interface TestCaseId {

    String value();

}
