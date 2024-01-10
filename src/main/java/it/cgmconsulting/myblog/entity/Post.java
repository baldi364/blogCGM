package it.cgmconsulting.myblog.entity;

import it.cgmconsulting.myblog.entity.common.CreationUpdate;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Getter
@Setter
@NoArgsConstructor

public class Post extends CreationUpdate
{
    /*
    CREATE TABLE IF NOT EXISTS post(
id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
author_id INT NOT NULL,
category_id TINYINT NOT NULL,
created_at DATETIME NOT NULL,
updated_at DATETIME NOT NULL,
publication_date DATETIME DEFAULT NULL,
FOREIGN KEY (author_id) REFERENCES  `user`(id),
FOREIGN KEY (category_id) REFERENCES category(id)
);
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "author", nullable = false)
    private User author;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    /*
   "One to Many" -> ad un post possono essere associati più commenti

   "mappedBy" -> l'entità Comment contiene un riferimento all'entità Post,
                 e questo riferimento è identificato dall'attributo "post" nell'entità Comment.

   "cascade" -> concetto di persistenza. qualunque operazione vado a fare su un post,
                la sua persistenza si può ripercuotere sulla lista di commenti.
                In questo caso, avendo utilizzato .ALL, se elimino un post
                verranno cancellati anche i suoi commenti.

   "orphanRemoval" = se cancelli un post, si cancellano anche i suoi figli (in questo caso, i commenti)
    */

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Comment> comments = new HashSet<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Content> contents = new ArrayList<>();

    private LocalDateTime publicationDate; //se null o posteriore alla data attuale il post non sarà visibile

    public void addComment(Comment comment)
    {
        //aggiungo un commento
        comments.add(comment);

        //setto il post al commento
        comment.setPost(this);
    }

    public Post(User author, Category category)
    {
        this.author = author;
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return id == post.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
