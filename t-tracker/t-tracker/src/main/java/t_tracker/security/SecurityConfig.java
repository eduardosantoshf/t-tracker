package t_tracker.security;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

import t_tracker.repository.ClientRepository;

import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{

    private PasswordEncoder passwordEncoder;
    private UserDetailsService userDetailsService;
    private ClientRepository userRepository;

    public SecurityConfig(PasswordEncoder passwordEncoder, @Qualifier("userDetailsServiceImpl") UserDetailsService userDetailsService, ClientRepository userRepository) {
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
            .antMatchers(HttpMethod.GET, "/client/login").permitAll()
            .antMatchers(HttpMethod.GET, "/swagger-ui.html").permitAll()
            .antMatchers(HttpMethod.GET, "/v2/api-docs").permitAll()
			.antMatchers(HttpMethod.GET, "/webjars/**").permitAll()
			.antMatchers(HttpMethod.GET, "/swagger-resources/**").permitAll()
            .antMatchers(HttpMethod.POST, "/client/signup").permitAll()
            .antMatchers(HttpMethod.GET, "/client/verify").permitAll()
            .antMatchers(HttpMethod.GET, "/product/all").permitAll()
            .antMatchers(HttpMethod.GET, "/product/{\\d+}").permitAll()
            .antMatchers(HttpMethod.GET, "/stock").permitAll()
			.anyRequest().authenticated()
			.and()
			.addFilter(new JwtAuthenticationFilter(authenticationManager(),userRepository))
            .addFilter(new JWTAuthorizationFilter(authenticationManager()));
	}
}
