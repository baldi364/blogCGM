package it.cgmconsulting.myblog.repository;

import it.cgmconsulting.myblog.entity.Comment;
import it.cgmconsulting.myblog.payload.response.CommentResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer>
{
    @Query(value="SELECT new it.cgmconsulting.myblog.payload.response.CommentResponse(" +
            "c.id, " +
            "c.comment, " +
            "c.author.username, " +
            "c.createdAt" +
            ") " +
            "FROM Comment c " +
            "WHERE c.post.id = :postId " +
            "AND censored = false " +
            "ORDER BY c.createdAt DESC"
    )
    List<CommentResponse> getComments(int postId);
}
