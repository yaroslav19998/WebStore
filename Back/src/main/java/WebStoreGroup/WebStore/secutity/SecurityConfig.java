package WebStoreGroup.WebStore.secutity;

import WebStoreGroup.WebStore.secutity.filter.AuthFilter;
import WebStoreGroup.WebStore.secutity.filter.JWTFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserDetailsService userDetailsService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private final JWTFilter jwtFilter;
    private final AuthFilter authFilter;


    @Autowired
    public SecurityConfig(@Qualifier("userService") UserDetailsService userDetailsService, JWTFilter jwtFilter, AuthFilter authFilter){ //,// ) { //, CustomAuthFilter customAuthFilter
        this.userDetailsService = userDetailsService;
        this.jwtFilter=jwtFilter;
        this.authFilter = authFilter;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Collections.singletonList("*"));
        configuration.setAllowedMethods(Arrays.asList("HEAD",
                "GET", "POST", "PUT", "DELETE", "PATCH"));
        // setAllowCredentials(true) is important, otherwise:
        // The value of the 'Access-Control-Allow-Origin' header in the response must not be the wildcard '*' when the request's credentials mode is 'include'.
        configuration.setAllowCredentials(false);
        // setAllowedHeaders is important! Without it, OPTIONS preflight request
        // will fail with 403 Invalid CORS request
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type","ResponseResourceFormat"));
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
      auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        authFilter.setAuthenticationManager(authenticationManagerBean());
        authFilter.setFilterProcessesUrl("/login");
        http.csrf().disable();
        http.cors();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeRequests()
                .antMatchers("/login","/users/refresh_token","/users/registration").permitAll()

                .antMatchers(HttpMethod.POST,"/orders").permitAll();
        http.authorizeRequests().antMatchers(HttpMethod.GET).permitAll();
        http.authorizeRequests().antMatchers(HttpMethod.POST,"/products/{\\d+}/reviews").authenticated().and().exceptionHandling()
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));
        http.authorizeRequests().antMatchers(HttpMethod.PUT,"/products/reviews/{\\d+}").authenticated().and().exceptionHandling()
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));
        http.authorizeRequests().antMatchers(HttpMethod.PUT,"/users/{\\d+}").authenticated().and().exceptionHandling()
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));
        http.authorizeRequests().antMatchers(HttpMethod.DELETE,"/products/reviews/{\\d+}").authenticated().and().exceptionHandling()
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));


        http.authorizeRequests().antMatchers(HttpMethod.POST).hasRole("ADMIN").and().exceptionHandling().authenticationEntryPoint(
                new HttpStatusEntryPoint(HttpStatus.FORBIDDEN));
        http.authorizeRequests().antMatchers(HttpMethod.DELETE).hasRole("ADMIN").and().exceptionHandling().authenticationEntryPoint(
                new HttpStatusEntryPoint(HttpStatus.FORBIDDEN));
        http.authorizeRequests().antMatchers(HttpMethod.PUT).hasRole("ADMIN").and().exceptionHandling().authenticationEntryPoint(
                new HttpStatusEntryPoint(HttpStatus.FORBIDDEN));


        http.addFilter(authFilter);
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean()  throws Exception {
        return super.authenticationManagerBean();
    }
}
