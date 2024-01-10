package it.cgmconsulting.myblog.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ReadValueFromApplicationYaml
{
    public static String IMAGE_PATH;

    @Autowired
    public void getValues(@Value("${application.contentImage.path}") String imagePath)
    {
        IMAGE_PATH = imagePath; //path: /Users/Public/myblog/images/
    }
}
