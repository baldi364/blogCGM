package it.cgmconsulting.myblog.controller;
/*
Questo package contiene le classi dei controller Spring, che gestiscono le richieste HTTP dai client.
Le classi controller sono responsabili di:
 - instradare le richieste alle risorse appropriate
 - chiamare i servizi di business e restituire le risposte ai client.
 */

import it.cgmconsulting.myblog.service.CategoryService;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/*
L'annotation @RestController viene utilizzata in classi che restituiscono
dati in formati desiderati come JSON o XML, anzichè viste HTML.
Questi controller vengono utilizzati per creare servizi web RESTful
I dati vengono inviati in formato strutturato come JSON e
vengono elaborati da un app client o un app mobile
 */
@RestController
@RequestMapping("category")
@RequiredArgsConstructor
@Validated
public class CategoryController
{
    private final CategoryService categoryService;

    /*
    primo metodo per inviare una variabile
    con @PreAuthorize autorizzo solo utenti con determinate autorità
    ad effettuare modifiche (in questo caso solo gli admin)
     */

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/") // localhost:8081/category/?categoryName=antipasti Method .POST
    public ResponseEntity<?> save(@RequestParam
                                  @NotBlank
                                  @Length(min = 4, max = 30) String categoryName)
    {
       return categoryService.save(categoryName.trim());
    }

    //secondo metodo
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/{categoryName}") // localhost:8081/category/antipasti Method .POST
    public ResponseEntity<?> save2(@PathVariable
                                   @NotBlank
                                   @Length(min = 4, max = 30) String categoryName)
    {
        return categoryService.save(categoryName.trim());
    }

    //terzo metodo (url uguale al primo, andrà in errore per mapping ambigui)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/add") // localhost:8081/category/add Method .POST
    public ResponseEntity<?> save3(@RequestBody
                                   @NotBlank
                                   @Length(min = 4, max = 30) String categoryName)
    {
        return categoryService.save(categoryName.trim());
    }

    //recuperare una risorsa
    @PreAuthorize("hasRole('ROLE_WRITER')")
    @GetMapping //localhost:8081/category
    public ResponseEntity<?> getAllVisibleCategories()
    {
        return categoryService.getAllVisibleCategories();
    }

    @GetMapping("/all") //localhost:8081/category/all
    public ResponseEntity<?> findAll()
    {
        return categoryService.findAll();
    }

    //aggiornare una risorsa
    @PutMapping("/{id}") //localhost:8081/category/5?newCategory=carni
    public ResponseEntity<?> update(@PathVariable @Min(1) byte id,
                                    @RequestParam @NotBlank @Size(min = 4, max = 30) String newCategory)
    {
        return categoryService.update(id, newCategory.trim());
    }

    //settare la visibilità di una categoria
    @PutMapping("/switch-visibility/{id}") //localhost:8081/category/switch-visibility/3
    public ResponseEntity<?> switchVisibility(@PathVariable @Min(1) byte id)
    {
        return categoryService.switchVisibility(id);
    }
}
