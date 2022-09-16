package com.crunchdao.app.service.model.permission;

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
@PreAuthorize("hasRole('" + CommonRoles.Name.USER + "') or hasAuthority('" + Scopes.WRITE + "')")
public @interface CanWrite {
}