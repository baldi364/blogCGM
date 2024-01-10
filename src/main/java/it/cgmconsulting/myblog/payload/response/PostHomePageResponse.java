package it.cgmconsulting.myblog.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostHomePageResponse
{
    private int postId;
    private String image;
    private String title;
    private double average;
    private long comments;
}
