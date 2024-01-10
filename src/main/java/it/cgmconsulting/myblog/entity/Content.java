package it.cgmconsulting.myblog.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Check;

import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Content
{
    /*
         CREATE TABLE IF NOT EXISTS content(
        id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
        title VARCHAR(50),
        content TEXT NOT NULL,
        `type` ENUM('H','C','F') NOT NULL,
        prg TINYINT NOT NULL,
        post_id INT NOT NULL,
        FOREIGN KEY (post_id) REFERENCES post(id)
        );
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;

    @Column(length = 50)
    private String title;

    @Column(nullable = false, length = 10000)
    private String content;

    @Check(constraints = "type = 'H' or type = 'C' or type = 'F'")
    @Column(nullable = false)
    private char type;

    private byte prg; //progressivo per ordinamento di content di tipo 'C'

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    //  un contenuto può avere più immmagini
    //  ma un immagine può essere presente in più contenuti
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="content_images",
            joinColumns = {@JoinColumn(name = "content_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "image_id", referencedColumnName = "id")})
    private Set<Image> images;

    public Content(String title, String content, char type, Post post)
    {
        this.title = title;
        this.content = content;
        this.type = type;
        this.post = post;
    }


}
