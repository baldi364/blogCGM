package it.cgmconsulting.myblog.entity;

import it.cgmconsulting.myblog.entity.common.CreationUpdate;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.*;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Rating extends CreationUpdate
{
    /*
    CREATE TABLE IF NOT EXISTS rating (
user_id INT NOT NULL,
post_id INT NOT NULL,
rate TINYINT NOT NULL,
created_at DATETIME NOT NULL,
updated_at DATETIME NOT NULL,
PRIMARY KEY (user_id, post_id),
FOREIGN KEY (user_id) REFERENCES `user`(id),
FOREIGN KEY(post_id) REFERENCES post(id)
);

     */
    @EmbeddedId
    private RatingId ratingId;

    //@Check(name = "validation_rate", constraints = "rate > 0 AND rate < 6")
    private byte rate; // i voti vanno da 1 a 5

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rating rating = (Rating) o;
        return Objects.equals(ratingId, rating.ratingId);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(ratingId);
    }
}