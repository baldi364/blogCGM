package it.cgmconsulting.myblog.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
@Getter
public class OwnerException extends RuntimeException
{
    /*
    Questo messaggio verrà visualizzato nel caso in cui
    l'utente non sia autorizzato come proprietario della risorsa.
     */
    public OwnerException()
    {
        super("You are not the owner of this resource");
    }
}
