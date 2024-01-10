package it.cgmconsulting.myblog.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Category
{
    /*
    CREATE TABLE IF NOT EXISTS category (
id TINYINT PRIMARY KEY AUTO_INCREMENT,
category_name VARCHAR(30) UNIQUE NOT NULL,
`visible` BIT(1) NOT NULL DEFAULT b'0'
);
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private byte id;

    @Column(length = 30, unique = true, nullable = false)
    private String categoryName;

    private boolean visible = true;

    public Category(String categoryName)
    {
        this.categoryName = categoryName;
    }
}
