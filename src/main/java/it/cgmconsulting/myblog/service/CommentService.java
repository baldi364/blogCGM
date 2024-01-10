package it.cgmconsulting.myblog.service;

import it.cgmconsulting.myblog.entity.Comment;
import it.cgmconsulting.myblog.entity.Post;
import it.cgmconsulting.myblog.entity.User;
import it.cgmconsulting.myblog.payload.request.CommentRequest;
import it.cgmconsulting.myblog.payload.response.CommentResponse;
import it.cgmconsulting.myblog.repository.CommentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService
{
    private final CommentRepository commentRepository;
    private final PostService postService;

    //METDOO PER AGGIUNGERE UN COMMENTO AD UN POST
    @Transactional
    public ResponseEntity<?> addComment(UserDetails userDetails, CommentRequest request)
    {
        Post p = postService.getPostById(request.getPostId());
        postService.isPostVisible(p);
        Comment comment = new Comment((User) userDetails, p, request.getComment());
        p.addComment(comment);
        return new ResponseEntity("Comment added to post", HttpStatus.OK);
    }

    //LISTA COMMENTI DI UN POST
    public ResponseEntity<?> getCommentList(int postId)
    {
        Post p = postService.getPostById(postId);
        postService.isPostVisible(p);
        List<CommentResponse> commentList = commentRepository.getComments(postId);
        return new ResponseEntity(commentList, HttpStatus.OK);
    }

    //CONTEGGIO DEI COMMENTI DI UN POST DA AGGIUNGERE ALLE VARIE GET DEI POST

}
