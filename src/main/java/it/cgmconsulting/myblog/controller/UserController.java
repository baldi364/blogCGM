package it.cgmconsulting.myblog.controller;

import it.cgmconsulting.myblog.service.UserService;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Validated
public class UserController
{
    private final UserService userService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/update_auths/{userId}")
    public ResponseEntity<?> updateAuths(@PathVariable @Min(1) int userId,
                                         @RequestBody @NotEmpty Set<String> authorities)
    {
        return userService.updateAuthorities(userId, authorities);
    }

    //authenticationPrincipal -> esegue il getPrincipal(), è propria di Spring Security
    @GetMapping("get-me")
    public ResponseEntity<?> getMe(@AuthenticationPrincipal UserDetails userDetails)
    {
        return userService.getMe(userDetails);
    }
}
