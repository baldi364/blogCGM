package it.cgmconsulting.myblog.service;
/*
Questo package contiene le classi dei servizi che gestiscono:
- elaborazione dati
- operazioni di business
- validazione
- etc..
 */

import it.cgmconsulting.myblog.entity.Category;
import it.cgmconsulting.myblog.exception.ResourceNotFoundException;
import it.cgmconsulting.myblog.exception.UniqueConstraintViolationException;
import it.cgmconsulting.myblog.payload.response.CategoryVisibleResponse;
import it.cgmconsulting.myblog.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

//In Service vengono implementate le logiche di business
//All'avvio Spring sa che questa classe dovrà essere istanziata ed iniettati i repository.
//Questa classe potrà essere iniettata all'interno del controller per utilizzarne i metodi
@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryService
{

    private final CategoryRepository categoryRepository;
    private final UtilsService utilsService;

    //Metodo per salvare una nuova categoria
    public ResponseEntity <?> save(String categoryName)
    {
        log.info("Category persistence start");

        //controllo se la categoria che sto creando esiste
        existsByCategoryName(categoryName);

        /*
        istanzio l'oggetto Category passando come parametro
        il nome della categoria
         */
        Category cat = new Category(categoryName);

        //salvo l'oggetto category
        categoryRepository.save(cat);
        log.info("Category persisted: " + cat.toString());

        return new ResponseEntity(cat, HttpStatus.CREATED);
    }

    //metodo per visualizzare tutte le categorie con visible a true
    public ResponseEntity<?> getAllVisibleCategories()
    {
        log.info("Get all visible categories ordered by category name");

        /*
        - utilizzo la query in CategoryRepository
        - creo una classe CategoryVisibleResponse per ottenere solo i dati che mi servono
        - CategoryVisibleResponse conterrà in questo caso byte id e String categoryName
         */

        List<CategoryVisibleResponse> list = categoryRepository.getAllVisibleCategories();

        //controllo se la lista è vuota
        utilsService.isEmptyCollection(list, "categories");

        //ritorno solo la lista
        return new ResponseEntity(list, HttpStatus.OK);
    }

    //Metodo per trovare tutte le categorie
    public ResponseEntity<?> findAll()
    {
        log.info("Get all categories");

        /*
        creo una lista di categorie
        utilizzo i metodi derivati di JpaRepository per trovare tutte le categorie
         */
        List<Category> list = categoryRepository.findAll();

        //controllo sempre che la lista non sia vuota
        utilsService.isEmptyCollection(list, "categories");
        return new ResponseEntity(list, HttpStatus.OK);
    }

    //Metodo per aggiornare un valore
    public ResponseEntity<?> update(byte id, String newCategory)
    {
        //Verifico se la categoria esiste
        existsByCategoryName(newCategory);

        //Mi ricavo l'id della categoria da aggiornare
        Category cat = getCategoryById(id);

        //Setto la nuova categoria nel nome
        cat.setCategoryName(newCategory);

        //salvo il nuovo oggetto
        categoryRepository.save(cat);

        return new ResponseEntity("Category has been updated with name: " + newCategory, HttpStatus.OK);
    }

    //Metodo per settare la visibilità della categoria
    @Transactional //salva in automatico
    public ResponseEntity<?> switchVisibility(byte id)
    {
        //cerco prima la categoria
        Category cat = getCategoryById(id);
        //la setto
        cat.setVisible(!cat.isVisible());

        //e infine la salvo
        //categoryRepository.save(cat);
        return new ResponseEntity(null, HttpStatus.OK);
    }

    protected void existsByCategoryName(String categoryName)
    {
        if (categoryRepository.existsByCategoryName(categoryName))
        {
            throw new UniqueConstraintViolationException("Category", "categoryName", categoryName);
        }
    }

    protected Category getCategoryByIdAndVisibleTrue(byte categoryId){
        return categoryRepository.findByIdAndVisibleTrue(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));
    }

    protected Category getCategoryById(byte categoryId)
    {
        return categoryRepository.findById(categoryId)
                .orElseThrow( (() -> new ResourceNotFoundException("Category","id", categoryId)));
    }

}
