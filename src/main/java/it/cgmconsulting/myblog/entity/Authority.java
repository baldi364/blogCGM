package it.cgmconsulting.myblog.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
//@Table(name="authority")
@Getter @Setter @NoArgsConstructor @ToString @EqualsAndHashCode
public class Authority
{

    /*
    -- CREAZIONE TABELLA AUTHORITY
CREATE TABLE IF NOT EXISTS authority(
id TINYINT PRIMARY KEY AUTO_INCREMENT NOT NULL,
authority_name VARCHAR(30) UNIQUE NOT NULL DEFAULT '0',
`visible` BIT(1) NOT NULL DEFAULT b'0'
);

     */
    @Id //identifica la primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private byte id;

    @Column(length = 30, nullable = false, unique = true)
    private String authorityName; //authority_name sul DB

    private boolean visible = true;

    private boolean defaultAuthority = false;

    public Authority(String authorityName)
    {
        this.authorityName = authorityName;
    }
}
