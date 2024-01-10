package it.cgmconsulting.myblog.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostBoxResponse extends PostHomePageResponse
{
    private String content;

    public PostBoxResponse(int postId, String image, String title, double average, long comments, String content)
    {
        super(postId, image, title, average, comments);
        this.content = content;
    }
}
