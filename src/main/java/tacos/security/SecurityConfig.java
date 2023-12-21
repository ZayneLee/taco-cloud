package tacos.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder PasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        return new CustomClientRegistrationRepository();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder encoder) {
        List<UserDetails> usersList = new ArrayList<>();
        usersList.add(
                new User("buzz", encoder.encode("password"), Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"))));
        usersList.add(
                new User("woody", encoder.encode("password"), Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"))));
        return new InMemoryUserDetailsManager(usersList);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.authorizeRequests().mvcMatchers("/design", "/orders").hasRole("USER").antMatchers("/", "/**")
                .permitAll().and().formLogin().loginPage("/login").and().oauth2Login().loginPage("/login")
                .defaultSuccessUrl("/design", true).and().build();
    }

    private class CustomClientRegistrationRepository implements ClientRegistrationRepository {

        @Override
        public ClientRegistration findByRegistrationId(String registrationId) {
            if ("facebook".equals(registrationId)) {
                return ClientRegistration.withRegistrationId("facebook")
                        .clientId("329187793397695")
                        .clientSecret("ce1420447166e7486df7c7fb8109b5f2")
                        .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                        .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                        // .redirectUriTemplate("{baseUrl}/login/oauth2/code/{registrationId}")
                        .scope("email", "public_profile")
                        .build();
            }
            return null;
        }

        // @Bean
        // public UserDetailsService userDetailsService(UserRepository userRepo) {
        // return username -> {
        // tacos.User user = userRepo.findByUsername(username);
        // if (user != null)
        // return user;

        // throw new UsernameNotFoundException("User '" + username + "' not found");
        // };
        //
    }
}
