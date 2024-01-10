package it.cgmconsulting.myblog.repository;

import it.cgmconsulting.myblog.entity.Post;
import it.cgmconsulting.myblog.payload.response.ContentResponse;
import it.cgmconsulting.myblog.payload.response.PostBoxResponse;
import it.cgmconsulting.myblog.payload.response.PostDetail;
import it.cgmconsulting.myblog.payload.response.PostHomePageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer>
{
    @Query(value = "SELECT new it.cgmconsulting.myblog.payload.response.PostHomePageResponse(" +
                    "co.post.id, " +
                    "CONCAT(:imagePath,' ',i.image), " +
                    "co.title, " +
                    "(SELECT COALESCE(AVG(r.rate),0.0) FROM Rating r WHERE r.ratingId.post.id = co.post.id) as average, " +
                    "(SELECT COUNT(c.id) FROM Comment c WHERE c.post.id = co.post.id AND c.censored = false) as comments" +
                    ") FROM Content co " +
                    "LEFT JOIN co.images i " +
                    "WHERE co.type='H' " +
                    "AND co.post.publicationDate <= :now " +
                    "ORDER BY co.post.publicationDate DESC")
    List<PostHomePageResponse> getPostHomePageList(LocalDateTime now, String imagePath);

    @Query(value = "SELECT new it.cgmconsulting.myblog.payload.response.PostBoxResponse(" +
            "co.post.id, " +
            "CONCAT(:imagePath,i.image), " +
            "co.title, " +
            "(SELECT COALESCE(AVG(r.rate),0.0) FROM Rating r WHERE r.ratingId.post.id = co.post.id) as average, " +
            "(SELECT COUNT(c.id) FROM Comment c WHERE c.post.id = co.post.id AND c.censored = false) as comments, " +
            "co.content" +
            ") FROM Content co " +
            "LEFT JOIN co.images i " +
            "WHERE co.type='H' " +
            "AND co.post.publicationDate <= :now ")
    List<PostBoxResponse> getPostBoxList(LocalDateTime now, String imagePath);

    @Query(value = "SELECT new it.cgmconsulting.myblog.payload.response.PostDetail(" +
                    "p.id, " +
                    "cat.categoryName, " +
                    "p.author.username, " +
                    "co.title, " +
                    "CONCAT(:imagePath,i.image), " +
                    "(SELECT COALESCE(AVG(r.rate),0.0) FROM Rating r WHERE r.ratingId.post.id = p.id) as average, " +
                    "(SELECT COUNT(c.id) FROM Comment c WHERE c.post.id = p.id AND c.censored = false) as comments" +
                    ") FROM Content co " +
                    "INNER JOIN Post p ON p.id = co.post.id " +
                    "LEFT JOIN co.images i " +
                    "LEFT JOIN Category cat ON (cat.id = co.post.category.id AND cat.visible = true) " +
                    "WHERE co.post.id = :postId " +
                    "AND co.type='H' " +
                    "AND p.publicationDate <= :now")
    PostDetail getPostDetail(int postId, LocalDateTime now, String imagePath);

    @Query(value="SELECT new it.cgmconsulting.myblog.payload.response.PostBoxResponse(" +
            "co.post.id, " +
            "CONCAT(:imagePath,i.image), " +
            "co.title, " +
            "(SELECT COALESCE(AVG(r.rate),0.0) FROM Rating r WHERE r.ratingId.post.id = co.post.id) as average, " +
            "(SELECT COUNT(c.id) FROM Comment c WHERE c.post.id = co.post.id AND c.censored = false) as comments, " +
            "co.content" +
            ") FROM Content co " +
            "LEFT JOIN co.images i " +
            "WHERE co.type='H' " +
            "AND co.post.publicationDate <= :now " +
            "AND co.post.category.categoryName = :categoryName " +
            "AND co.post.category.visible = true")
    Page<PostBoxResponse> getPostsByCategory(Pageable pageable, String categoryName, LocalDateTime now, String imagePath);
}
