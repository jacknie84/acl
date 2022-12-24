package com.jacknie.examples.acl.config.security;

import com.jacknie.examples.acl.config.security.acess.expression.CustomMethodSecurityExpressionHandler;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.util.FieldUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@RequiredArgsConstructor
public class GlobalMethodSecurityCustomConfiguration extends GlobalMethodSecurityConfiguration {

    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        CustomMethodSecurityExpressionHandler handler = new CustomMethodSecurityExpressionHandler();
        setDefaultMethodSecurityExpressionHandler(handler);
        return handler;
    }

    @SneakyThrows
    private void setDefaultMethodSecurityExpressionHandler(DefaultMethodSecurityExpressionHandler handler) {
        Field field = FieldUtils.getField(GlobalMethodSecurityConfiguration.class, "defaultMethodExpressionHandler");
        ReflectionUtils.makeAccessible(field);
        field.set(this, handler);
    }
}
