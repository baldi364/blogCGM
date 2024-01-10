package it.cgmconsulting.myblog.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import it.cgmconsulting.myblog.payload.request.SigninRequest;
import it.cgmconsulting.myblog.payload.request.SignupRequest;
import it.cgmconsulting.myblog.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController
{
    private final AuthenticationService authenticationService;

    @Operation(
            description = "POST ENDPOINT FOR USER SIGNUP",
            summary = "Questo è il sommario",
            responses = {
                    @ApiResponse(description = "User successfully created",
                                 responseCode = "201"),
                    @ApiResponse(description = "Bad Request",
                                 responseCode = "400"),
                    @ApiResponse(description = "E' andato tutto a casino, sorry",
                                 responseCode = "500")
            }
    )

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody @Valid SignupRequest signupRequest)
    {
        return authenticationService.signup(signupRequest);
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@RequestBody @Valid SigninRequest signinRequest)
    {
        return authenticationService.signin(signinRequest);
    }

}
