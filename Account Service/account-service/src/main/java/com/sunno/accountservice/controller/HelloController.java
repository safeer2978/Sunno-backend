package com.sunno.accountservice.controller;

import com.sunno.accountservice.models.RoleName;
import com.sunno.accountservice.models.persistence.JwtRefreshToken;
import com.sunno.accountservice.models.persistence.Role;
import com.sunno.accountservice.models.persistence.User;
import com.sunno.accountservice.models.persistence.repository.JwtRefreshTokenRepository;
import com.sunno.accountservice.models.persistence.repository.RoleRepository;
import com.sunno.accountservice.models.persistence.repository.UserRepository;
import com.sunno.accountservice.models.request.AuthenticationRequest;
import com.sunno.accountservice.models.request.LoginRequest;
import com.sunno.accountservice.models.request.RefreshTokenRequest;
import com.sunno.accountservice.models.request.SignUpRequest;
import com.sunno.accountservice.models.response.ApiResponse;
import com.sunno.accountservice.models.response.JwtAuthenticationResponse;
import com.sunno.accountservice.security.util.JwtUtil;
import com.sunno.accountservice.service.MyUserDetailsService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;

//TODO HANDLE JWT EXPIRE TOKEN EXCEPTION

@RestController()
class HelloController {
    @Value("${app.jwtExpirationInMs}")
    private long jwtExpirationInMs;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtTokenUtil;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    JwtRefreshTokenRepository jwtRefreshTokenRepository;


    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @GetMapping("/hello")
    public String firstPage() {
        return "Hello World";
    }

/*    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );
        }
        catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }


        long exp=System.currentTimeMillis()+30*60*1000; ///TODO: change this

        final String jwt = jwtTokenUtil.generateToken("PAID",exp);

        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt,"", exp));
    }
    @GetMapping("auth/custom-login")
    public String loadLoginPage(){
        return "login";
    }*/


    @SneakyThrows
    @PostMapping("/refreshToken")
    public ResponseEntity<?> refreshAccessToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        long jwtExpirationInMs = System.currentTimeMillis()+30*60*1000; //TODO CHANGE THESE NUMBERS

        return jwtRefreshTokenRepository.findByToken(refreshTokenRequest.getRefreshToken()).map(jwtRefreshToken -> {
            User user = jwtRefreshToken.getUser();
            String accessToken = jwtTokenUtil.generateToken(MyUserDetailsService.create(user), jwtExpirationInMs);
            return ResponseEntity.ok(new JwtAuthenticationResponse(accessToken, jwtRefreshToken.getToken(), jwtExpirationInMs));
        }).orElseThrow(() -> new BadRequestException("Invalid Refresh Token"));
    }

    private void saveRefreshToken(MyUserDetailsService userDetailsService, String refreshToken) {
        // Persist Refresh Token

        JwtRefreshToken jwtRefreshToken = new JwtRefreshToken(refreshToken);
        jwtRefreshToken.setUser(userRepository.findByEmail(userDetailsService.getUsername()));

        Instant expirationDateTime = Instant.now().plus(10, ChronoUnit.DAYS);  // Todo Add this in application.properties
        jwtRefreshToken.setExpirationDateTime(expirationDateTime);

        jwtRefreshTokenRepository.save(jwtRefreshToken);
    }


    private static class BadRequestException extends Exception {
        public BadRequestException(String invalid_refresh_token) {
            super(invalid_refresh_token);
        }
    }


    @SneakyThrows
    @PostMapping("/sign_up")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
   /*     if(userRepository.existsByUsername(signUpRequest.getUsername())) {
            return new ResponseEntity(new ApiResponse(false, "Username is already taken!"),
                    HttpStatus.BAD_REQUEST);
        }
*/
        if(userRepository.existsByEmail(signUpRequest.getEmail())) {
            return new ResponseEntity(new ApiResponse(false, "Email Address already in use!"),
                    HttpStatus.BAD_REQUEST);
        }

        // Creating user's account
        User user = new User(signUpRequest.getFirst_name(), signUpRequest.getFirst_name(),
                signUpRequest.getEmail(),signUpRequest.getPassword(), signUpRequest.getAge(),signUpRequest.getGender());

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role userRole = roleRepository.findByName(RoleName.ROLE_USER_FREE)
                .orElseThrow(() -> new Exception("User Role not set."));

        user.setRoles(Collections.singleton(userRole));

        User result = userRepository.save(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/users/{username}")
                .buildAndExpand(result.getId()).toUri();

        return ResponseEntity.created(location).body(new ApiResponse(true, "User registered successfully"));
    }


    @PostMapping("/sign_in")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        MyUserDetailsService userDetails = (MyUserDetailsService) authentication.getPrincipal();

        //UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        long exp_time= System.currentTimeMillis()+30*1000*60;

        String accessToken = jwtTokenUtil.generateToken(userDetails, exp_time);
        String refreshToken = jwtTokenUtil.generateRefreshToken();

        saveRefreshToken(userDetails, refreshToken);

        return ResponseEntity.ok(new JwtAuthenticationResponse(accessToken, refreshToken, exp_time));
    }


}
