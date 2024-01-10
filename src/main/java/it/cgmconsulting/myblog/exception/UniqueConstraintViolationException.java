package it.cgmconsulting.myblog.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
@Getter

/*
    L'obiettivo di questa classe è gestire eccezioni quando
    si violano vincoli di unicità nel database.
    Ad esempio, se sto cercando di inserire un record in una tabella del database
    e uno dei campi deve essere unico, ma il valore che sto cercando di inserire
    è già presente nel database,
    un'eccezione di violazione del vincolo di unicità viene sollevata.
 */

public class UniqueConstraintViolationException extends RuntimeException
{
    //nome della risorsa
    private final String resourceName;

    //nome del campo che deve essere unico
    private final String fieldName;

    //valore del campo che è già presente nel database
    private final Object fieldValue;

    public UniqueConstraintViolationException(String resourceName, String fieldName, Object fieldValue)
    {
        super(String.format("%s already present with %s: %s", resourceName, fieldName, fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }
}
