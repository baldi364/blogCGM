package it.cgmconsulting.myblog.payload.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryVisibleResponse
{
    /*
        utilizzo questa classe per passarmi in risposta soltanto gli attributi
        che effettivamente mi servono
     */
    private byte id;
    private String categoryName;
}

