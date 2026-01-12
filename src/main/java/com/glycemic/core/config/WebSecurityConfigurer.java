package com.glycemic.core.config;


import com.glycemic.core.filter.AuthTokenFilter;
import com.glycemic.core.jwt.AuthenticationEntryPointHandler;
import com.glycemic.core.security.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
		securedEnabled = true,
		jsr250Enabled = true,
		prePostEnabled = true)
@RequiredArgsConstructor
public class WebSecurityConfigurer {

	private final UserDetailsServiceImpl userDetailsService;

	private final AuthenticationEntryPointHandler authenticationEntryPointImpl;
	
	private static final String[] AUTH_URLS = {
			"/user/**", 
		    "/auth/logout",
		    "/food/insert",
		    "/food/delete",
		    "/food/update",
		    "/food/list/user",
		    "/search/search/user",
		    "/category/insert",
		    "/category/update",
		    "/category/delete",
		    "/nutritional/insert",
		    "/nutritional/update",
		    "/nutritional/delete",
		    "/mail/test",
	};
	
	@Bean
	AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter(AUTH_URLS);
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }
	
	@Bean
	 protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.sessionManagement(configure-> configure.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		http.exceptionHandling(configure -> configure.authenticationEntryPoint(authenticationEntryPointImpl));
		http.authorizeHttpRequests(configure -> configure.requestMatchers(AUTH_URLS).authenticated().anyRequest().permitAll());
		//http.authorizeHttpRequests(configure -> configure.requestMatchers("\\/food\\/get\\?name=[\\s\\S]\\w{1,}\\w[&]{1}status=[\\s\\S]\\w{1,}").hasAnyRole("USER","ADMIN"));
		http.addFilterBefore(authenticationJwtTokenFilter(),UsernamePasswordAuthenticationFilter.class);

		http.cors(Customizer.withDefaults());
		http.formLogin(AbstractHttpConfigurer::disable);
		http.logout(AbstractHttpConfigurer::disable);
		http.httpBasic(AbstractHttpConfigurer::disable);
		http.csrf(AbstractHttpConfigurer::disable);

        return http.build();
	 }
}
