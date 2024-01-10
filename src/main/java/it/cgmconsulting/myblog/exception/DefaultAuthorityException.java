package it.cgmconsulting.myblog.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
@Getter
public class DefaultAuthorityException extends RuntimeException
{
    private final long countDefaultAuthority;

    public DefaultAuthorityException(long countDefaultAuthority)
    {
        super(String.format("Count default authority = %d instead 1",countDefaultAuthority));
        this.countDefaultAuthority = countDefaultAuthority;
    }
}
