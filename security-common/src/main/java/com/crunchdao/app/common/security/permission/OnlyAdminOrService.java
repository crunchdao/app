package com.crunchdao.app.common.security.permission;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.springframework.security.access.prepost.PreAuthorize;

import com.crunchdao.app.common.security.role.CommonRoles;

@Retention(RUNTIME)
@Target({ TYPE, METHOD })
@Inherited
@PreAuthorize("hasRole('" + CommonRoles.Name.ADMIN + "') or hasRole('" + CommonRoles.Name.SERVICE + "')")
public @interface OnlyAdminOrService {
}