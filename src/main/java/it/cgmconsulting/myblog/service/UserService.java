package it.cgmconsulting.myblog.service;

import it.cgmconsulting.myblog.entity.Authority;
import it.cgmconsulting.myblog.entity.User;
import it.cgmconsulting.myblog.exception.ResourceNotFoundException;
import it.cgmconsulting.myblog.payload.response.UserResponse;
import it.cgmconsulting.myblog.repository.AuthorityRepository;
import it.cgmconsulting.myblog.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService
{
    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    /*
      - CAMBIO RUOLO
      - CAMBIO PASSWORD
      - GET MYPROFILE
     */

    //CAMBIO RUOLO
    public ResponseEntity<?> updateAuthorities(int id, Set<String> authorities)
    {
        // Verifichiamo l'esistenza dell'utente
        User u = userRepository.findById(id)
                .orElseThrow((() -> new ResourceNotFoundException("User", "id", id)));

        //Trasformiamo Set<String> in Set<Authority>
        Set<Authority> auths = authorityRepository.findByVisibleTrueAndAuthorityNameIn(authorities);
        if(auths.isEmpty())
        {
            return new ResponseEntity("Authorities not found", HttpStatus.NOT_FOUND);
        }

        //Setto il Set<Authority> su user e salvare
        u.setAuthorities(auths);
        userRepository.save(u);
        return new ResponseEntity("Authorities updated for user " + u.getUsername(), HttpStatus.OK);
    }

    // GET MYPROFILE
    public ResponseEntity<?> getMe(UserDetails userDetails)
    {

        UserResponse u = UserResponse.fromUserDetailsToUserResponse((User) userDetails);
        return new ResponseEntity(u,HttpStatus.OK);
    }
}
