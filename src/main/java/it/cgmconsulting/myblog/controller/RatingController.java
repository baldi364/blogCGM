package it.cgmconsulting.myblog.controller;

import it.cgmconsulting.myblog.service.RatingService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("rate")
@RequiredArgsConstructor
@Validated
public class RatingController
{
    private final RatingService ratingService;

    @PreAuthorize("hasRole('ROLE_MEMBER')")
    @PostMapping("/{postId}/{rate}")
    public ResponseEntity<?> ratePost(@PathVariable @Min(1) int postId,
                                      @PathVariable @Min(1) @Max(5) byte rate,
                                      @AuthenticationPrincipal UserDetails userDetails)
    {
        return ratingService.ratePost(postId, rate, userDetails);
    }
}
