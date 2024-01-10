package it.cgmconsulting.myblog.payload.response;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@NotBlank
@AllArgsConstructor
public class ReportingCheckBannedUserResponse
{
    private LocalDateTime createdAt;
    private int severity;
}
