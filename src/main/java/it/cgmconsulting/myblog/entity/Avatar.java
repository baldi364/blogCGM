package it.cgmconsulting.myblog.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Avatar
{
    /*
    CREATE TABLE IF NOT EXISTS avatar(
    id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    filename VARCHAR(255) NOT NULL,
    filetype VARCHAR(50) NOT NULL,
    `data` BLOB NOT NULL
);
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;

    @Column(nullable = false)
    private String filename;

    @Column(length = 50, nullable = false)
    private String filetype; //mime type (es. image/png)

    /*
    L'annotazione @Lob (Large Object) in Java Persistence API (JPA) viene utilizzata per indicare che
    un campo di dati di un'entità rappresenta un oggetto di grandi dimensioni,
    come un array di byte, una stringa molto lunga o altri tipi di dati che superano
    le dimensioni dei tipi di dati standard di una tabella del database.
     */
    @Lob
    @Column(nullable = false, columnDefinition = "BLOB")
    private byte[] data;

    public Avatar(String filename, String filetype, byte[] data)
    {
        this.filename = filename;
        this.filetype = filetype;
        this.data = data;
    }
}
