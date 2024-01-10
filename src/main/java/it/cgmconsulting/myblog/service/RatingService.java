package it.cgmconsulting.myblog.service;

import it.cgmconsulting.myblog.entity.Post;
import it.cgmconsulting.myblog.entity.Rating;
import it.cgmconsulting.myblog.entity.RatingId;
import it.cgmconsulting.myblog.entity.User;
import it.cgmconsulting.myblog.repository.RatingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RatingService
{
    private final RatingRepository ratingRepository;
    private final PostService postService;

    public ResponseEntity<?> ratePost(int postId, byte rate, UserDetails userDetails)
    {
        Post p = postService.getPostById(postId);
        postService.isPostVisible(p);
        Rating r = new Rating(new RatingId(p, (User) userDetails), rate);
        ratingRepository.save(r);
        return new ResponseEntity<>("Rating successfully saved", HttpStatus.OK);
    }
}
