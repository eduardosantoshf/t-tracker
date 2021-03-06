package deliveries_engine.security;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

import deliveries_engine.repository.UserRepository;

import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{

    private PasswordEncoder passwordEncoder;
    private UserDetailsService userDetailsService;
    private UserRepository userRepository;

    public SecurityConfig(PasswordEncoder passwordEncoder, @Qualifier("userDetailsServiceImpl") UserDetailsService userDetailsService, UserRepository userRepository) {
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
            .antMatchers(HttpMethod.GET, "/login").permitAll()
            .antMatchers(HttpMethod.GET, "/swagger-ui.html").permitAll()
            .antMatchers(HttpMethod.GET, "/v2/api-docs").permitAll()
			.antMatchers(HttpMethod.GET, "/webjars/**").permitAll()
			.antMatchers(HttpMethod.GET, "/swagger-resources/**").permitAll()
            .antMatchers(HttpMethod.POST, "/rider/signup").permitAll()
            .antMatchers(HttpMethod.GET, "/rider/verify").permitAll()
            .antMatchers(HttpMethod.POST, "/store").permitAll()
                .antMatchers(HttpMethod.POST, "/store/order/{storeId}").permitAll()
                .antMatchers("/chat/**").permitAll()
                .antMatchers("/chat/info/*").permitAll()
                .antMatchers(HttpMethod.POST, "/store/driver/rating/{storeId}/{riderId}").permitAll()
                .antMatchers(HttpMethod.POST, "/store/driver/comment/{storeId}/{riderId}").permitAll()
                .antMatchers(HttpMethod.GET, "/store/driver/rating/{storeId}/{riderId}").permitAll()
                .antMatchers(HttpMethod.GET, "/store/driver/comment/{storeId}/{riderId}").permitAll()
            .antMatchers(HttpMethod.POST, "/store/order/{storeId}").permitAll()
            .antMatchers(HttpMethod.POST, "/location/{latitude}/{longitude}").permitAll()
                .antMatchers(HttpMethod.GET, "/store/rider/{riderId}/{storeId}").permitAll()
			.anyRequest().authenticated()
			.and()
			.addFilter(new JwtAuthenticationFilter(authenticationManager(),userRepository))
            .addFilter(new JWTAuthorizationFilter(authenticationManager()));
	}

}
