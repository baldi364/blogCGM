package it.cgmconsulting.myblog.payload.response;

import it.cgmconsulting.myblog.entity.Image;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ContentResponse
{
    private String title;
    private String content;
    private Set<Image> images;
}

