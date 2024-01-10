package it.cgmconsulting.myblog.controller;

import it.cgmconsulting.myblog.service.AuthorityService;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("ath")
@Validated
@RequiredArgsConstructor
public class AuthorityController {

    private final AuthorityService authorityService;
    // aggiungere ruolo
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<?> addAuthority(@RequestParam @Size(max = 30, min = 7) @NotEmpty String newAuthority) {
        return authorityService.addAuthority(newAuthority);
    }

    // togliere visibilita' al ruolo
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> switchVisibility(@PathVariable @Min(1) byte id)
    {
        return authorityService.switchVisibility(id);
    }


    // cambiare il ruolo di default
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/default/{id}")
    public ResponseEntity<?> setNewDefault(@PathVariable @Min(1) byte id)
    {
        return authorityService.setNewDefault(id);
    }
}
