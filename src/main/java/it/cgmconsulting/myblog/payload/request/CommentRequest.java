package it.cgmconsulting.myblog.payload.request;

import it.cgmconsulting.myblog.entity.Comment;
import it.cgmconsulting.myblog.entity.Post;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class CommentRequest
{
    @Min(1)
    private int postId;

    @NotBlank @Size(min = 2, max = 255)
    private String comment;
}
