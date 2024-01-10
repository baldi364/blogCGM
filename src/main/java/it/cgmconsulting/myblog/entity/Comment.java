package it.cgmconsulting.myblog.entity;

import it.cgmconsulting.myblog.entity.common.Creation;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Comment extends Creation
{
    /*
    CREATE TABLE IF NOT EXISTS `comment`(
id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
author_id INT NOT NULL,
post_id INT NOT NULL,
created_at DATETIME NOT NULL,
`comment` VARCHAR(255) NOT NULL DEFAULT '',
censored BIT(1) NOT NULL DEFAULT b'0',
FOREIGN KEY(author_id) REFERENCES  `user`(id),
FOREIGN KEY (post_id) REFERENCES post(id)
);
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author", nullable = false)
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Column(nullable = false)
    private String comment;

    private boolean censored = false;

    public Comment(User author, Post post, String comment)
    {
        this.author = author;
        this.post = post;
        this.comment = comment;
    }
}
