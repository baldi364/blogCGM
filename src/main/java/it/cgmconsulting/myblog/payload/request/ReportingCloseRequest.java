package it.cgmconsulting.myblog.payload.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class ReportingCloseRequest
{
    @Min(1)
    private int commentId;

    @Size(min=15, max=18) @NotBlank
    private String status;

    private String note;
}
