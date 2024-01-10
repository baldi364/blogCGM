package it.cgmconsulting.myblog.payload.request;

import jakarta.validation.constraints.Min;
import lombok.Getter;

@Getter
public class ReportingUpdateRequest
{
    @Min(1)
    private int comment;
    private String reason;
    private String note;
}
