package it.cgmconsulting.myblog.controller;

import it.cgmconsulting.myblog.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/avatar")
@RequiredArgsConstructor
public class AvatarController
{
    private final ImageService imageService;


    @Value("${application.avatar.size}")
    private long size;

    @Value("${application.avatar.width}")
    private int width;

    @Value("${application.avatar.height}")
    private int height;

    @Value("${application.avatar.extensions}")
    private String[] extensions;

    /*
    MultipartFile MultipartFile è una classe fornita da Spring Framework
    che rappresenta un qualsiasi tipo di file caricato tramite una richiesta HTTP multipart.
    Questo è spesso utilizzato in applicazioni web per consentire agli utenti
    di caricare file, ad esempio immagini, documenti o altri tipi di dati.
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addAvatar(@AuthenticationPrincipal UserDetails userDetails, @RequestParam MultipartFile file) throws IOException
    {
        return imageService.addAvatar(size, width, height, extensions, userDetails, file);
    }

    @DeleteMapping
    public ResponseEntity<?> removeAvatar(@AuthenticationPrincipal UserDetails userDetails)
    {
        return imageService.removeAvatar(userDetails);
    }
}
