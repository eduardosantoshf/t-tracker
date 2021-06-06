package deliveries_engine.security;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;

import deliveries_engine.repository.UserRepository;

import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{

    private PasswordEncoder passwordEncoder;
    private UserDetailsService userDetailsService;

    private UserRepository userRepository;

    public SecurityConfig(PasswordEncoder passwordEncoder, UserDetailsService userDetailsService, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    @Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable().cors().and()
            .authorizeRequests()
            .antMatchers(HttpMethod.POST, "/rider/signup").permitAll()
            .antMatchers(HttpMethod.GET, "/rider/location/**").permitAll();
			//.anyRequest().authenticated()
			//.and()
			//.addFilter(new JwtAuthenticationFilter(authenticationManager(),userRepository))
            //.addFilter(new JWTAuthorizationFilter(authenticationManager()));

	}

    
}
