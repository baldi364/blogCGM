package it.cgmconsulting.myblog.service;

import it.cgmconsulting.myblog.exception.EmptyListException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UtilsService
{
    /*
        Metodo generico per lista vuota
        Parametri: lista, tipo dell'elemento non trovato
     */

    protected void isEmptyCollection(List<?> list, String elementNotFound)
    {
        if(list.isEmpty())
        {
            throw new EmptyListException(elementNotFound);
        }
    }
}
