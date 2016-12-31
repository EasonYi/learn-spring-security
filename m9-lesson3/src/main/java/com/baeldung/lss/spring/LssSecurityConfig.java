package com.baeldung.lss.spring;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.access.method.MapBasedMethodSecurityMetadataSource;
import org.springframework.security.access.method.MethodSecurityMetadataSource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import com.baeldung.lss.security.MyUserDetailsService;

@EnableWebSecurity
@ComponentScan({ "org.baeldung.lss.security" })
public class LssSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private MyUserDetailsService userDetailsService;

    public LssSecurityConfig() {
        super();
    }

    //

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {// @formatter:off
        http
        .authorizeRequests()

            .anyRequest().authenticated()

        .and()
        .formLogin().
            loginPage("/login").permitAll().
            loginProcessingUrl("/doLogin")

        .and()
        .logout().permitAll().logoutUrl("/logout")

        .and()
        .csrf().disable()
        ;
    }

    @EnableGlobalMethodSecurity(prePostEnabled = true)
    public static class MethodSecurityConfig extends GlobalMethodSecurityConfiguration {

        @Override
        public MethodSecurityMetadataSource customMethodSecurityMetadataSource() {
            final Map<String, List<ConfigAttribute>> methodMap = new HashMap<>();
            methodMap.put("com.baeldung.lss.web.controller.UserController.createForm*", SecurityConfig.createList("ROLE_ADMIN"));
            return new MapBasedMethodSecurityMetadataSource(methodMap);
        }

    }

}
