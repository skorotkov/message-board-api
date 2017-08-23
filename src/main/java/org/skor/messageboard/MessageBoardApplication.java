package org.skor.messageboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@SpringBootApplication
//@EnableOAuth2Sso
public class MessageBoardApplication extends WebSecurityConfigurerAdapter {

    public static void main(String[] args) {
        SpringApplication.run(MessageBoardApplication.class, args);
    }

    @Bean
    public Executor executor() {
        return Executors.newFixedThreadPool(200);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.antMatcher("/**")
            .authorizeRequests()
                .antMatchers("/", "/github-login").permitAll()
                .anyRequest().authenticated()
            .and().cors()
            .and().csrf().disable();
    }
}
