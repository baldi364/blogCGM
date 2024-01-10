package it.cgmconsulting.myblog.service;

import it.cgmconsulting.myblog.entity.Authority;
import it.cgmconsulting.myblog.exception.DefaultAuthorityException;
import it.cgmconsulting.myblog.exception.ResourceNotFoundException;
import it.cgmconsulting.myblog.repository.AuthorityRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorityService {

    private final AuthorityRepository authorityRepository;

    public ResponseEntity<?> addAuthority(String newAuthority) {
        if(authorityRepository.existsByAuthorityName(newAuthority))
            return new ResponseEntity<>("Authority already present", HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(authorityRepository.save(new Authority(newAuthority)), HttpStatus.CREATED);
    }

    @Transactional
    public ResponseEntity<?> switchVisibility(byte id)
    {
        Authority a = authorityRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Authority", "id", id));

        a.setVisible(!a.isVisible());
        return new ResponseEntity("Authority updated", HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> setNewDefault(byte id)
    {
        // trovare il ruolo di default
        Authority oldDefault = authorityRepository.findByDefaultAuthorityTrue();

        // settarlo a false
        oldDefault.setDefaultAuthority(false);

        // trovare il ruolo che voglio diventi quello di default
        Authority newDefault = authorityRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Authority", "id", id));

        // settare se quest'ultimo default a true
        newDefault.setDefaultAuthority(true);

        // prova del 9: contare i flag default = true, il conteggio DEVE essere uguale a 1
        long count = authorityRepository.countByDefaultAuthorityTrue();
        if (count != 1)
        {
            throw new DefaultAuthorityException(count);
        }

        return new ResponseEntity(newDefault.getAuthorityName() + " is the new default authority", HttpStatus.OK);
    }
}
