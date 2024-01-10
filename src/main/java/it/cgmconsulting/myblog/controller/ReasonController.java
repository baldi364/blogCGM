package it.cgmconsulting.myblog.controller;

import it.cgmconsulting.myblog.payload.request.ReasonRequest;
import it.cgmconsulting.myblog.service.ReasonService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("reason")
@RequiredArgsConstructor
@Validated
public class ReasonController
{
    private final ReasonService reasonService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<?> addReason(@RequestBody @Valid ReasonRequest request)
    {
        return reasonService.addReason(request);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping
    public ResponseEntity<?> updateReason(@RequestBody @Valid ReasonRequest request)
    {
        return reasonService.updateReason(request);
    }

    @PreAuthorize("hasRole('ROLE_MEMBER')")
    @GetMapping
    public ResponseEntity<?> getValidReasons()
    {
      return reasonService.getValidReasons();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/remove/{reason}")
    public ResponseEntity<?> removeReason(@PathVariable @Size(max=50) @NotBlank String reason)
    {
        return reasonService.removeReason(reason);
    }
}
