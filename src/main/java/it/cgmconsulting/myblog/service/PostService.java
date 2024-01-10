package it.cgmconsulting.myblog.service;

import it.cgmconsulting.myblog.entity.Category;
import it.cgmconsulting.myblog.entity.Content;
import it.cgmconsulting.myblog.entity.Post;
import it.cgmconsulting.myblog.entity.User;
import it.cgmconsulting.myblog.exception.OwnerException;
import it.cgmconsulting.myblog.exception.ResourceNotFoundException;
import it.cgmconsulting.myblog.payload.response.ContentResponse;
import it.cgmconsulting.myblog.payload.response.PostBoxResponse;
import it.cgmconsulting.myblog.payload.response.PostDetail;
import it.cgmconsulting.myblog.payload.response.PostHomePageResponse;
import it.cgmconsulting.myblog.repository.ContentRepository;
import it.cgmconsulting.myblog.repository.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService
{
    @Value("${application.contentImage.path}")
    private String imagePath;

 private final PostRepository postRepository;
 private final CategoryService categoryService;
    private final ContentRepository contentRepository;

    //METODO PER AGGIUNGERE UN POST
 public ResponseEntity<?> addPost(UserDetails userDetails, byte categoryId)
 {
     //ricavo i dettagi dell'utente tramite un cast di UserDetails su User
     User u = (User) userDetails;

     //ricavo la categoria per id e con visible a true
     Category cat = categoryService.getCategoryByIdAndVisibleTrue(categoryId);

     /*
        Il costruttore del post necessitava di autore e categoria
        dunque, una volta che li ho ricavati entrambi, li passo a new Post(author, category)
      */
     Post p = new Post(u, cat);

     //salvo il tutto
     postRepository.save(p);
     return new ResponseEntity("New post created", HttpStatus.CREATED);
 }

 // METODO PER AGGIORNARE LA CATEGORIA DI UN POST
    @Transactional
    public ResponseEntity<?> updatePostCategory(int postId, byte categoryId, UserDetails userDetails)
    {
        //Mi ricavo l'id del post con la categoria da aggiornare
        Post p = getPostById(postId);

        /*
            Tramite il metodo isOwner(), verifico che l'utente che vuole aggiornare
            la categoria del post sia lo stesso che l'ha pubblicato
         */

        isOwner(userDetails, p.getAuthor());

        //ricavo la nuova categoria che poi setto al post
        Category cat = categoryService.getCategoryByIdAndVisibleTrue(categoryId);
        p.setCategory(cat);
        return new ResponseEntity("Category updated", HttpStatus.OK);
    }


    //METODO PER MODIFICARE LA DATA DI PUBBLICAZIONE
    @Transactional
    public ResponseEntity<?> setPublicationDate(UserDetails userDetails, int postId, LocalDateTime publicationDate)
    {
        Post p = getPostById(postId);
        isOwner(userDetails, p.getAuthor());
        p.setPublicationDate(publicationDate);
        return new ResponseEntity("Publication date has been updated", HttpStatus.OK);
    }

    public ResponseEntity<?> getPostHomePageList()
    {
        List<PostHomePageResponse> list = postRepository.getPostHomePageList(LocalDateTime.now(), imagePath);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    public ResponseEntity<?> getPostBoxList()
    {
        List<PostBoxResponse> list = postRepository.getPostBoxList(LocalDateTime.now(), imagePath);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    /*
    questo metodo si occuperà di recuperare i dettagli di un post
    (titolo, autore, categoria, ecc.) e tutti i suoi contenuti
    (contenuto principale di tipo 'F' e contenuti aggiuntivi di tipo 'C')
     */
    public ResponseEntity<?> getPostDetail(int postId)
    {
        //Ottengo i dettagli del post attraverso la query JPQL in PostRepository
        //I parametri della query sono l'id del post, la data di pubblicazione e il path
        PostDetail p = postRepository.getPostDetail(postId, LocalDateTime.now(), imagePath);

        //Mi ricavo tutti i contenuti di tipo C del post attraverso un'altra query JPQL in ContentRepository
        List<Content> contents = contentRepository.getPostDetailContents(postId);

        //Mappo gli oggetti Content ottenuti precedentemente, in oggetti ContentResponse
        //Questi oggetti ContentResponse contengono i dettagli del titolo, del contenuto
        // e delle immagini associate a ciascun oggetto Content
        List<ContentResponse> contentList = contents.stream().map( c ->{
            return  new ContentResponse(c.getTitle(), c.getContent(), c.getImages());
        }).collect(Collectors.toList());

        //Ricavo il footer attraverso il metodo findByTypeAndPostId
        Content content = contentRepository.findByTypeAndPostId('F', postId);

        //Ricavo il footer mappandolo come oggetto ContentResponse
        ContentResponse contentf = new ContentResponse(content.getTitle(), content.getContent(), content.getImages());

        //Setto il tutto ai dettagli del post
        p.setContents(contentList);
        p.setContentF(contentf);
        return new ResponseEntity<>(p, HttpStatus.OK);
    }

    public ResponseEntity<?> getPostsByCategory(String categoryName, int pageNumber, int pageSize, String sortBy, String direction)
    {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.Direction.valueOf(direction), sortBy);
        Page<PostBoxResponse> result = postRepository.getPostsByCategory(pageable, categoryName, LocalDateTime.now(), imagePath);
        if(result.hasContent())
        {
            return new ResponseEntity(result.getContent(), HttpStatus.OK);
        }
        return new ResponseEntity("No posts found with category " + categoryName, HttpStatus.NOT_FOUND);
    }

    protected Post getPostById(int postId)
    {
        return postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));
    }

    /*
    UserDetails -> utente autenticato nel sistema
    User -> utente associato alla risorsa che si sta cercando di accedere o modificare
    Questo metodo li confonta e, se gli utenti non sono gli stessi, viene lanciata un'eccezione
     */
    protected void isOwner(UserDetails userDetails, User user)
    {
        if (!user.equals((User) userDetails))
        {
            throw new OwnerException();
        }
    }

    protected void isPostVisible(Post post)
    {
        if (LocalDateTime.now().isBefore(post.getPublicationDate()))
        {
            throw new ResourceNotFoundException("Post", "id", post.getId());
        }
}



}
