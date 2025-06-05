package com.example.identity_service.Configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.spec.SecretKeySpec;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // security tren cac method
public class SecurityConfig {

        private  String[] ACCESS_ENDPOINT = {"/users","/auth/log-in","/auth/introspect","/auth/logout"}; // endpoint dc access

//        @Value("${jwt.SignKey}")
//        private  String signKey;

        @Autowired
        private CustomJwtDecoder customJwtDecoder;

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {


            // cho phep enpoint nao dc access ma khong can authatication
            http.authorizeHttpRequests(request ->
                    //cho phep cac endpoint trong access endpoint truy cap
                    request.requestMatchers(HttpMethod.POST, ACCESS_ENDPOINT)
                            .permitAll()

    //                        // cho phep user co role "admin" truy cap vao endpoint /users
    //                        //co the convert thanh role_admin hoac bat ki gi dung converter ....(research)
    //                        .requestMatchers(HttpMethod.GET,"/users" )
//                            .hasAuthority("SCOPE_ADMIN")

                            .anyRequest()
                            .authenticated()
            );


            // cho phep endpoint nao co token hop le thi se dc access khi co token ( bearer token )
            // day la 1 manager provider
            http.oauth2ResourceServer(oauth2 ->
                    oauth2.jwt(jwtConfigurer -> jwtConfigurer.decoder(customJwtDecoder)
                                    .jwtAuthenticationConverter(jwtAuthenticationConverter())
                            )

                            .authenticationEntryPoint(new JwtAuthenticationEntryPoint()) // dieu huong khi ko authenticated dc
            );
            //jwtDecoder la 1 interface -> implement no

            http.csrf(AbstractHttpConfigurer::disable); // tat csrf
            return http.build();
        }

//    @Bean
//    JwtDecoder jwtDecoder(){
//        SecretKeySpec secretKeySpec = new SecretKeySpec(signKey.getBytes(),"HS512");
//        return NimbusJwtDecoder
//                .withSecretKey(secretKeySpec)
//                .macAlgorithm(MacAlgorithm.HS512)
//                .build();
//    }

        @Bean
        PasswordEncoder passwordEncoder(){
            return new BCryptPasswordEncoder(10);
        }

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);

        return jwtAuthenticationConverter;
    }

}
