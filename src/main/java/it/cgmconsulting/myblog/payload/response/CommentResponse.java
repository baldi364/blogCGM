package it.cgmconsulting.myblog.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponse
{
    private int commentId;
    private String comment;
    private String username;
    private LocalDateTime createdAt;
}
