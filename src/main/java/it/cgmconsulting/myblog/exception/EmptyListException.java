package it.cgmconsulting.myblog.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
@Getter
public class EmptyListException extends RuntimeException
{
    private final String elementNotFound;

    public EmptyListException(String elementNotFound)
    {
        /*
        Con String.format() posso inserire variabili in determinate posizioni
        "%s", in questo caso, verrà sostituito da "elementNotFound" passato come parametro

        Altri tipi di variabili:
        %d: Numero intero (int o Integer)
        %f: Numero in virgola mobile (float o double)
        %b: Valore booleano
        %c: Carattere
        %t: Data/ora
         */
        super(String.format("No %s found", elementNotFound));
        this.elementNotFound = elementNotFound;
    }
}
