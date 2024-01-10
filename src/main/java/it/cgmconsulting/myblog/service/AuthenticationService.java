package it.cgmconsulting.myblog.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Collections;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import it.cgmconsulting.myblog.entity.Authority;
import it.cgmconsulting.myblog.entity.User;
import it.cgmconsulting.myblog.payload.request.SigninRequest;
import it.cgmconsulting.myblog.payload.request.SignupRequest;
import it.cgmconsulting.myblog.payload.response.AuthenticationResponse;
import it.cgmconsulting.myblog.repository.AuthorityRepository;
import it.cgmconsulting.myblog.repository.UserRepository;
import it.cgmconsulting.myblog.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {
    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final ReportingService reportingService;

    public ResponseEntity<?> signup(SignupRequest request)
    {
        if (userRepository.existsByEmailOrUsername(request.getEmail(), request.getUsername()))
        {
            return new ResponseEntity("Username or Email already in use", HttpStatus.BAD_REQUEST);
        }

        Authority a = authorityRepository.findByDefaultAuthorityTrue();

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .authorities(Collections.singleton(a))
                .build();

        userRepository.save(user);

        log.info("### "+ user.toString());
        return new ResponseEntity<String>("User successfully registered",HttpStatus.CREATED);
    }

    public ResponseEntity<?> signin(SigninRequest request)
    {
        //cerco l'utente
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(()-> new BadCredentialsException("Bad credentials"));

        //confronto le password
        if(!passwordEncoder.matches(request.getPassword(), user.getPassword()))
        {
            throw new BadCredentialsException("Bad credentials");
        }

        boolean isGuest = ArrayUtils.contains(authorities(user.getAuthorities()), "ROLE_GUEST");

        if(!user.isEnabled() && isGuest)
        {
            return new ResponseEntity<>("Please check your email for activate your account", HttpStatus.BAD_REQUEST);
        }

        if(!user.isEnabled() && !isGuest) // SE USER DISABILITATO E TRA I RUOLI NON HA QUELLO DI GUEST ALLORA E' STATO BANNATO
        {
            //se l'utente non è abilitato, facciamo verifica su reporting
            LocalDateTime bannedUntil = reportingService.checkUser(user);
            if(bannedUntil != null)
            {
                return new ResponseEntity<>("You are banned until " + bannedUntil.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), HttpStatus.UNAUTHORIZED);
            }
            else
            {
                // riabilita l'utente e proseguo
                user.setEnabled(true);
            }
        }
        String jwtToken = jwtService.generateToken(user, user.getId());

        return new ResponseEntity(AuthenticationResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .authorities(authorities(user.getAuthorities()))
                .token(jwtToken)
                .build(),
                HttpStatus.OK);
    }

    private String[] authorities(Collection<? extends GrantedAuthority> auths)
    {
        return auths.stream().map(a -> a.getAuthority())
                .toArray(String[]::new);
    }
}
