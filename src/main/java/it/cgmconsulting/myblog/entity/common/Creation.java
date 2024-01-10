package it.cgmconsulting.myblog.entity.common;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
//non la definisco come entità ma, se un'entità la estende,
// gli attirbuti presenti qui diventeranno colonne della classe che la estende
//Serializable può essere serializzata -> trasformata in dato fisico, genera un JSON
public class Creation implements Serializable
{
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
}
