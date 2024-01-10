package it.cgmconsulting.myblog.payload.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class ReportingRequest
{
   @Min(1)
   private int comment;

   @NotBlank @Size(max=50)
   private String reason;
}
