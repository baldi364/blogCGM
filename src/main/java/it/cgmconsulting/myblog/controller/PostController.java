package it.cgmconsulting.myblog.controller;

import it.cgmconsulting.myblog.payload.request.ContentRequest;
import it.cgmconsulting.myblog.service.ContentService;
import it.cgmconsulting.myblog.service.ImageService;
import it.cgmconsulting.myblog.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController
{
    @Value("${application.contentImage.size}")
    private long size;

    @Value("${application.contentImage.width}")
    private int width;

    @Value("${application.contentImage.height}")
    private int height;

    @Value("${application.contentImage.extensions}")
    private String[] extensions;

    private final PostService postService;
    private final ContentService contentService;
    private final ImageService imageService;

    @PreAuthorize("hasRole('ROLE_WRITER')")
    @PostMapping
    public ResponseEntity<?> addPost(@AuthenticationPrincipal UserDetails userDetails,
                                     @RequestParam @Valid byte categoryId)
    {
        return postService.addPost(userDetails, categoryId);
    }

    @PreAuthorize("hasRole('ROLE_WRITER')")
    @PutMapping("/{postId}/{categoryId}")
    public ResponseEntity<?> updatePostCategory(@AuthenticationPrincipal UserDetails userDetails,
                                                @PathVariable byte categoryId,
                                                @PathVariable int postId)
    {
        return postService.updatePostCategory(postId, categoryId, userDetails);
    }

    @PreAuthorize("hasRole('ROLE_WRITER')")
    @PutMapping("/{postId}")
    public ResponseEntity<?> setPublicationDate(@AuthenticationPrincipal UserDetails userDetails,
                                                @PathVariable int postId,
                                                @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime publicationDate)
    {
        return postService.setPublicationDate(userDetails, postId, publicationDate);
    }

    @PreAuthorize("hasRole('ROLE_WRITER')")
    @PostMapping("/add-content")
    public ResponseEntity<?> createContent(@AuthenticationPrincipal UserDetails userDetails,
                                           @RequestBody @Valid ContentRequest request)
    {
        return contentService.createContent(request, userDetails);
    }

    @PreAuthorize("hasRole('ROLE_WRITER')")
    @PutMapping(value = "/add-content-image/{contentId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addImageToContent(@AuthenticationPrincipal UserDetails userDetails,
                                               @RequestParam MultipartFile file,
                                               @PathVariable int contentId) throws IOException
    {
        return imageService.addImageToContent(size, width, height, extensions, userDetails, file, contentId);
    }

    @PreAuthorize("hasRole('ROLE_WRITER')")
    @PutMapping("/remove-image-from-content/{imageId}")
    public ResponseEntity<?> removeImageFromContent(@PathVariable int imageId) throws IOException
    {
        return imageService.removeImageFromContent(imageId);
    }

    @GetMapping("/public/get-hp-boxes")
    public ResponseEntity<?> getPostHomePageList()
    {
        return postService.getPostHomePageList();
    }

    @GetMapping("/public/get-boxes")
    public ResponseEntity<?> getPostBoxList()
    {
        return postService.getPostBoxList();
    }

    @GetMapping("/public/get-post/{postId}")
    public ResponseEntity<?> getPostDetail(@PathVariable int postId)
    {
        return postService.getPostDetail(postId);
    }


    @GetMapping("/public/get-posts-by-category")
    public ResponseEntity<?> getPostsByCategory(@RequestParam String categoryName,
                                                @RequestParam(defaultValue = "0") int pageNumber,       //n di pagine da cui partire ad impaginare i risultati
                                                @RequestParam(defaultValue = "10") int pageSize,        //n di elementi per pagina
                                                @RequestParam(defaultValue = "title") String sortBy,    // ordinati per titolo
                                                @RequestParam(defaultValue = "ASC") String direction
    )
    {
        return postService.getPostsByCategory(categoryName, pageNumber, pageSize, sortBy, direction);
    }

}
