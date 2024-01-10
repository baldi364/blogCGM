package it.cgmconsulting.myblog.service;

import it.cgmconsulting.myblog.entity.*;
import it.cgmconsulting.myblog.exception.ResourceNotFoundException;
import it.cgmconsulting.myblog.repository.ImageRepository;
import it.cgmconsulting.myblog.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.UUID;

@Service
public class ImageService
{
    @Value("$application.contentImage.path")
    private String imagePath;

    private final UserRepository userRepository;
    private final ImageRepository imageRepository;
    private final ContentService contentService;

    public ImageService(UserRepository userRepository,
                        ImageRepository imageRepository,
                        ContentService contentService)
    {
        this.userRepository = userRepository;
        this.imageRepository = imageRepository;
        this.contentService = contentService;
    }

    public ResponseEntity checkImage(long size,
                                     int width,
                                     int height,
                                     String [] extensions,
                                     UserDetails userDetails,
                                     MultipartFile file)
    {
        if(checkSize(file, size))
        {
            return new ResponseEntity("Size too large or file empty", HttpStatus.BAD_REQUEST);
        }

        if(checkDimensions(width, height, file))
        {
            return new ResponseEntity("Width or height too large", HttpStatus.BAD_REQUEST);
        }

        if(checkExtensions(file, extensions))
        {
            return new ResponseEntity("File type not allowed", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(true, HttpStatus.OK);
    }

    //Metodo per aggiungere un immagine al contenuto
    @Transactional
    public ResponseEntity<?> addImageToContent(long size, int width, int height, String[] extensions, UserDetails userDetails, MultipartFile file, int contentId) throws IOException
    {

    //Trovo il contenuto per ID
    Content co = contentService.getContentById(contentId);

    //Se il contenuto ha già un immagine caricata nell'Header, stampa un messaggio di errore
    if(co.getImages().size() > 0 && co.getType() == 'H')
        return new ResponseEntity<>("The content has already an image(content type H)", HttpStatus.BAD_REQUEST);

    /*
    Controllo se il file va bene tramite il metodo precedente checkImage()
    ricavandomi l'HttpStatus della ResponseEntity
    tramite il metodo .getStatusCode().equals(HttpStatus.OK))
     */
        if(checkImage(size, width, height, extensions, userDetails, file).getStatusCode().equals(HttpStatus.OK))
        {
            //Ottengo il nome originale del file
            String fileName = file.getOriginalFilename();

            //Estraggo l'estensione del file attraverso il substring( es .jpg etc..)
            //Questi passaggi vengono utilizzate per dare un nome univoco al file
            String ext = fileName.substring(fileName.lastIndexOf("."));

            //Creo un path utilizzando un UUID(identificativo univoco universale) per prevenire sovrascritture accidentali
            //con Path creo  il percorso completo del file
            Path path = Paths.get(imagePath + UUID.randomUUID().toString() + ext); //path = /Users/Public/myblog/images/pippo.jpg (con UUID.randomUUID().toString() al posto di "pippo" ci sarà un UUID random

            //Scrivo i byte dell'img nel percorso specificato
            //Questo passaggio salva fisicamente il file sul server
            Files.write(path, file.getBytes());

            //Creo un oggetto Image passandogli il nome originale dell'img
            Image i = new Image(fileName);

            //Salvo l'immagine tramite il metodo delle JpaRepository
            imageRepository.save(i);

            //Aggiungo l'img al content trovato per id
            co.getImages().add(i);
            return new ResponseEntity<>("Image added to content", HttpStatus.CREATED);
        }
        return new ResponseEntity<>("Something went wrong adding image to content", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    //METODO PER CANCELLARE UN'IMMAGINE DA UN CONTENUTO
    public ResponseEntity<?> removeImageFromContent(int imageId) throws IOException
    {
        //Ottengo l'immagine tramite id
        Image i = getImageById(imageId);
        String fileName = i.getImage();

        //richiamo il metodo con la mia query di delete
        imageRepository.deleteImageFromContent(imageId);

        //richiamo anche il metodo delle Jpa Repository
        imageRepository.delete(i);

        //devo eliminare il file anche dal percorso
        Path path = Paths.get(imagePath + fileName); //path = /Users/Public/myblog/images/pippo.jpg
        Files.delete(path);
        return new ResponseEntity<>("Image has been removed from content", HttpStatus.OK);
    }

    //METODO PER AGGIUNGERE UN AVATAR AD UN UTENTE
    public ResponseEntity<?> addAvatar(long size,
                                       int width,
                                       int height,
                                       String [] extensions,
                                       UserDetails userDetails,
                                       MultipartFile file) throws IOException
    {
        //Mi ricavo l'utente tramite findByUsername()
        User u = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", userDetails.getUsername()));

       if(checkImage(size, width, height, extensions, userDetails, file).getStatusCode().equals(HttpStatus.OK))
       {
           //creo l'oggetto avatar passsandogli ciò che chiede il suo costruttore (nome, tipo e data)
           Avatar av = new Avatar(file.getOriginalFilename(), file.getContentType(), file.getBytes());

           //setto l'avatar all'utente
           u.setAvatar(av);

           //salvo per garantire la persistenza dei dati
           userRepository.save(u);
           return new ResponseEntity("Avatar successfully updated", HttpStatus.OK);
       }
       return new ResponseEntity("Avatar not uploaded", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    //METODO PER ELIMINARE L'AVATAR
    public ResponseEntity<?> removeAvatar(UserDetails userDetails)
    {
        //ricavo l'utente
        User u = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", userDetails.getUsername()));

        //setto l'avatar a null, molto semplicemente
        u.setAvatar(null);
        userRepository.save(u);
        return new ResponseEntity("Avatar successfully removed", HttpStatus.OK);
    }

    //METODO PER CONTROLLARE SE L'UTENTE HA GIA' LO STESSO AVATAR
    public boolean doesUserHasSameAvatar(User user, MultipartFile file)
    {
        //Mi prendo l'avatar dell'utente
        Avatar avatarEsistente = user.getAvatar();
        if (avatarEsistente != null)
        {
            // Confronto il nome del file, il tipo di contenuto e il contenuto dell'avatar esistente con il nuovo file
            String fileNameEsistente = avatarEsistente.getFilename();
            String newFilename = file.getOriginalFilename();
            String contentTypeEsistente = avatarEsistente.getFiletype();
            String newContentType = file.getContentType();
            byte[] dataEsistente = avatarEsistente.getData();
            byte[] newData;

            try
            {
                newData = file.getBytes();
            }
            catch (IOException e)
            {
                e.printStackTrace();
                return false;
            }

            // Confronto i dettagli dell'avatar esistente con quelli del nuovo file
            if(fileNameEsistente.equals(newFilename) &&
               contentTypeEsistente.equals(newContentType) && Arrays.equals(dataEsistente, newData))
            {
                return true; //l'utente ha lo stesso avatar
            }
        }
        return false; //l'utente non ha lo stesso avatar
    }


    //CONTROLLO SIZE
    private boolean checkSize(MultipartFile file, long size)
    {
        /*
            se la grandezza del file è maggiore di quella consentita in application.yaml
            o
            il file è vuoto
            allora il metodo ritorna true, dunque qualcosa non va bene
         */
        if(file.getSize() > size || file.isEmpty())
        {
            return true;
        }
        return false;
    }

    /*
        Questo metodo converte un file MultipartFile in un'immagine BufferedImage
     */
    private BufferedImage fromMultipartFileToBufferedImage(MultipartFile file)
    {
        BufferedImage bf = null;
        try
        {
            /*
                file.getInputStream() restituisce un oggetto InputStream
                che rappresenta il contenuto del file MultipartFile.
             */
            bf = ImageIO.read(file.getInputStream());
            return bf;
        }
        catch(IOException e)
        {
            return null;
        }
    }

    //CONTROLLO ALTEZZA E LARGHEZZA
    private boolean checkDimensions(int width, int height, MultipartFile file)
    {
        //
        BufferedImage bf = fromMultipartFileToBufferedImage(file);
        if(bf != null)
        {
            /*
                se l'immagine è più larga o alta delle dimensioni consentite,
                il metodo restituisce true e dunque l'immagine non va bene
             */
            if(bf.getWidth() > width || bf.getHeight() > height)
            {
                return true;
            }
        }
        return false;
    }

    //CONTROLLO ESTENSIONI
    private boolean checkExtensions(MultipartFile file, String [] extensions)
    {
        // ottengo il nome originale del file
        String fileName = file.getOriginalFilename();
        String ext = null;

        try
        {
            // otteniamo l'estensione del file con le substring
            ext = fileName.substring(fileName.lastIndexOf(".") + 1); //otteniamo cos' jpg, png etc..

            /*
               Qui, stiamo creando uno stream degli elementi nell'array extensions.
               Per ogni elemento stiamo verificando se ext (l'estensione del file) è uguale,
               ignorando maiuscole e minuscole utilizzando equalsIgnoreCase.
             */
            if(Arrays.stream(extensions).anyMatch(ext::equalsIgnoreCase))
            {
                /*
                .anyMatch(...) restituirà true se almeno uno degli elementi nell'array
                delle estensioni coincide con ext.
                Se c'è una corrispondenza, l'if restituirà false, indicando che l'estensione è valida.
                Se nessuna delle estensioni consentite corrisponde a ext, .anyMatch(...)
                restituirà false, e il metodo restituirà true
                indicando un'estensione non valida.
                 */
                return false;
            }
        }
        catch(NullPointerException e)
        {
            return true;
        }
        return true;
    }

    protected Image getImageById(int imageId)
    {
        return imageRepository.findById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException("Image", "id", imageId));
    }


}

