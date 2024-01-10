package it.cgmconsulting.myblog.controller;

import it.cgmconsulting.myblog.payload.request.CommentRequest;
import it.cgmconsulting.myblog.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("comment")
@RequiredArgsConstructor
@Slf4j
public class CommentController
{

    private final CommentService commentService;


    @CacheEvict(value="tuttiICommenti", allEntries = true)
    @PreAuthorize("hasRole('ROLE_MEMBER')")
    @PostMapping
    public ResponseEntity<?> addComment(@AuthenticationPrincipal UserDetails userDetails,
                                        @RequestBody CommentRequest commentRequest)
    {
        return commentService.addComment(userDetails, commentRequest);
    }

    //LISTA COMMENTI DI UN POST
    @Cacheable("tuttiICommenti")
    @GetMapping("/public/get-comment-list/{postId}")
    public ResponseEntity<?> getCommentList(@PathVariable int postId)
    {
        log.info("######################## CIAO ######################");
        return commentService.getCommentList(postId);
    }

    //CONTEGGIO DEI COMMENTI DI UN POST DA AGGIUNGERE ALLE VARIE GET DEI POST
}
