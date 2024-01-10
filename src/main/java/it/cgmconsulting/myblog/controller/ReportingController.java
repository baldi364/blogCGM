package it.cgmconsulting.myblog.controller;

import it.cgmconsulting.myblog.payload.request.ReportingCloseRequest;
import it.cgmconsulting.myblog.payload.request.ReportingRequest;
import it.cgmconsulting.myblog.payload.request.ReportingUpdateRequest;
import it.cgmconsulting.myblog.service.ReportingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("controller")
@RequiredArgsConstructor
public class ReportingController
{
    private final ReportingService reportingService;

    @PreAuthorize("hasRole('ROLE_MEMBER')")
    @PostMapping
    public ResponseEntity<?> createReporting(@RequestBody @Valid ReportingRequest request,
                                             @AuthenticationPrincipal UserDetails userDetails)
    {
        return reportingService.createReporting(request, userDetails);
    }

    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    @PutMapping
    public ResponseEntity<?> updateReporting (@RequestBody @Valid ReportingUpdateRequest request)
    {
        return reportingService.updateReporting(request);
    }

    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    @PutMapping("/close")
    public ResponseEntity<?> closeReporting (@RequestBody @Valid ReportingCloseRequest request)
    {
        return reportingService.closeReporting(request);
    }
}
