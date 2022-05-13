package HomeDecor.security;

import HomeDecor.login.JWT.JWTAuthenticationFilter;
import HomeDecor.login.JWT.token.JWTTokenHelper;
import HomeDecor.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final JWTTokenHelper jwtTokenHelper;
    @Autowired
    public SecurityConfig(PasswordEncoder passwordEncoder,
                          UserService userService,
                          AuthenticationEntryPoint authenticationEntryPoint,
                          JWTTokenHelper jwtTokenHelper) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.jwtTokenHelper = jwtTokenHelper;
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                    .exceptionHandling()
                    .authenticationEntryPoint(authenticationEntryPoint)
                .and()
                .authorizeRequests(request ->
                        request.antMatchers("/", "index",
                                "/css/*", "/js/*").permitAll()
                        .antMatchers("/api/v*/registration/**",
                                "/api/v*/adminRegistration/**",
                                "/api/v1/auth/login",
                                "/api/v1/auth/adminLogin",
                                "/api/v1/auth/superAdminLogin",
                                "/api/v1/auth/userinfo",
                                "/api/v1/login/**", "/api/v*/confirm/**",
                                "/api/product/getProduct",
                                "/api/product/getProductByName/**",
                                "/api/product/getProductById/**",
                                "/api/v*/superAdminRegistration/**").permitAll()
                        .anyRequest()
                        .authenticated()
                )
                .addFilterBefore(new JWTAuthenticationFilter(userService, jwtTokenHelper),
                        UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(userService);
        return provider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
