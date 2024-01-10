package it.cgmconsulting.myblog.repository;
/*
Questo è il package in cui risiedono le classi dei repository,
che sono responsabili di interagire con il DB per eseguire operazioni CRUD (Create, Read, Update, Delete) sulle entità.
Le classi dei repository spesso estendono l'interfaccia JpaRepository o altre interfacce Spring Data.
 */

import it.cgmconsulting.myblog.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

//User è il tipo di entità con cui JPARepository lavorerà e da cui gestirà l'accesso ai dati
//Integer è il tipo dell'ID dell'entità
@Repository
public interface UserRepository extends JpaRepository<User,Integer>
{
    /*
    - Optional è un contenitore che può o non può contenere un valore non nullo.
      È utilizzato per rappresentare l'assenza di un valore e ridurre le eccezioni
      di tipo NullPointerException.

    - Si usa Optional quando un metodo può restituire un risultato vuoto.
      Questo è utile per i metodi di ricerca come findByUsername,
      dove potrebbe non esistere alcun utente con il nome specificato.
     */
    Optional<User> findByUsername(String username);

    boolean existsByEmailOrUsername(String email, String username);
}
