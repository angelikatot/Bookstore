package angie.projekti.bookstore;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import angie.projekti.bookstore.web.UserDetailServiceImpl;

@Configuration
@EnableMethodSecurity(securedEnabled = true)
public class WebSecurityConfig {

        @Autowired
        private UserDetailServiceImpl userDetailsService;

        private static final AntPathRequestMatcher[] WHITE_LIST_URLS = {
                        new AntPathRequestMatcher("/h2-console/**"),
                        new AntPathRequestMatcher("/"),
                        new AntPathRequestMatcher("/home"),
                        new AntPathRequestMatcher("/booklist")
        };

        @Bean
        public SecurityFilterChain configure(HttpSecurity http) throws Exception {
                http
                                .authorizeHttpRequests(authorize -> authorize
                                                .requestMatchers(antMatcher("/css/**")).permitAll()
                                                .requestMatchers(WHITE_LIST_URLS).permitAll()
                                                .requestMatchers(antMatcher("/add")).hasAuthority("ADMIN")
                                                .requestMatchers(antMatcher("/save")).hasAuthority("ADMIN")
                                                .requestMatchers(antMatcher("/edit/**")).hasAuthority("ADMIN")
                                                .requestMatchers(antMatcher("/update")).hasAuthority("ADMIN")
                                                .requestMatchers(antMatcher("/delete/**")).hasAuthority("ADMIN")
                                                .anyRequest().authenticated())
                                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions
                                                .disable())) // for h2-console
                                .formLogin(formlogin -> formlogin
                                                .loginPage("/login")
                                                .defaultSuccessUrl("/booklist", true)
                                                .permitAll())
                                .logout(logout -> logout
                                                .permitAll())
                                .csrf(csrf -> csrf.disable()); // not for production, just for development

                return http.build();
        }

        @Autowired
        public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
                auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
        }
}
