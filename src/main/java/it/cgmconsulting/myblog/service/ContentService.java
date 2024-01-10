package it.cgmconsulting.myblog.service;

import it.cgmconsulting.myblog.entity.Content;
import it.cgmconsulting.myblog.entity.Post;
import it.cgmconsulting.myblog.exception.ResourceNotFoundException;
import it.cgmconsulting.myblog.payload.request.ContentRequest;
import it.cgmconsulting.myblog.repository.ContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContentService {
    @Value("${application.contentTypes}")
    private char[] contentTypes;

    private final ContentRepository contentRepository;
    private final PostService postService;

    //METODO PER CREARE UN CONTENUTO
    public ResponseEntity<?> createContent(ContentRequest request, UserDetails userDetails)
    {
        //Itero sui tipi di contenuti (H,C e F)
        for (char c : contentTypes)
        {
            //Mi assicuro che il tipo di contenuto trovato è presente nel mio array di char
            if (request.getType() == c)
            {
                //Recupero il post e verifico che l'utente autenticato sia il proprietario del post
                Post p = postService.getPostById(request.getPostId());
                postService.isOwner(userDetails, p.getAuthor());

                //Creo un nuovo oggetto Content utilizzando i dati dalla richiesta e dal post.
                Content co = fromRequestToEntity(request, p);


                byte prg = 0;

                //Verifico se il tipo richiesto è H o F ed esiste già un post con quel tipo
                if (request.getType() == 'H'
                        &&
                    contentRepository.existsByTypeAndPost('H', p))
                {
                    return new ResponseEntity<>("Header for post " + p.getId() + " already created", HttpStatus.BAD_REQUEST);
                }

                if (request.getType() == 'F'
                        &&
                    contentRepository.existsByTypeAndPost('F', p))
                {
                    return new ResponseEntity<>("Footer for post " + p.getId() + " already created", HttpStatus.BAD_REQUEST);
                }

                //cercare il max progressivo, sommargli 3
                prg = (byte) (contentRepository.getMaxPrgByType(p, request.getType()) + 3);

                //settarlo all'oggetto Content e salvare
                co.setPrg(prg);
                contentRepository.save(co);
                return new ResponseEntity("Content added to post", HttpStatus.OK);
            }
        }
        return new ResponseEntity<>("ContentType not allowed", HttpStatus.OK);
    }


    /*
    Questo metodo svolge un'operazione di mappatura (mapping) da un oggetto di tipo ContentRequest
    a un oggetto di tipo Content. Questo tipo di metodo è comune nell'ambito dello sviluppo delle applicazioni Spring,
    in cui i dati inviati dal client (come richieste HTTP) spesso devono essere mappati
    agli oggetti delle entità persistenti nel database.
    Questa operazione è nota come "Data Transfer Object (DTO) to Entity mapping".
     */
    protected Content fromRequestToEntity(ContentRequest request, Post p)
    {
        return new Content(
                request.getTitle(),
                request.getContent(),
                request.getType(),
                p);
    }

    protected Content getContentById(int contentId)
    {
        return contentRepository.findById(contentId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", contentId));
    }
}
