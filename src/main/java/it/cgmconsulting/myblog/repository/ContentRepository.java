package it.cgmconsulting.myblog.repository;

import it.cgmconsulting.myblog.entity.Content;
import it.cgmconsulting.myblog.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContentRepository extends JpaRepository<Content, Integer>
{
    @Query(value = "SELECT COALESCE(MAX(c.prg), 0) FROM Content c WHERE c.post = :p AND c.type = :type")
    byte getMaxPrgByType(Post p, char type);

    /*
    Il metodo existsByTypeAndPost verifica se esiste già un contenuto
    con un tipo specifico (type) associato
    a un determinato post (p).
     */
    boolean existsByTypeAndPost(char type, Post p);

    @Query(value="SELECT co FROM Content co WHERE co.post.id = :postId AND co.type='C'")
    List<Content> getPostDetailContents(int postId);

    Content findByTypeAndPostId(char type, int postId);
}
