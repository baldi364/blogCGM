package it.cgmconsulting.myblog.entity.common;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.context.annotation.Scope;

import java.io.Serializable;
import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
//non la definisco come entità ma, se un'entità la estende,
// gli attirbuti presenti qui diventeranno colonne della classe che la estende
//Serializable può essere serializzata -> trasformata in dato fisico, genera un JSON
public class CreationUpdate extends Creation implements Serializable
{
    /*
    Questa annotazione è utilizzata per indicare che il campo updatedAt
    dovrebbe essere automaticamente popolato con il timestamp dell'ultima modifica.
    Quando si aggiorna un record nell'entità figlio,
    il campo updatedAt verrà automaticamente impostato sulla data e l'ora correnti.
     */
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
