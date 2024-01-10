package it.cgmconsulting.myblog.payload.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class ContentRequest
{
    @Size(max = 50)
    private String title;

    @NotBlank
    @Size(max = 10000, min = 10)
    private String content;

    private char type;

    @Min(1)
    private int postId;

}
