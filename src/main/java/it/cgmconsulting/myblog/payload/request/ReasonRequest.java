package it.cgmconsulting.myblog.payload.request;

import jakarta.validation.constraints.*;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class ReasonRequest
{
    @NotBlank
    @Size(max = 50)
    private String reason;

    @NotNull @FutureOrPresent
    private LocalDate startDate;

    @Min(1) @Max(36500)
    private int severity;
}
