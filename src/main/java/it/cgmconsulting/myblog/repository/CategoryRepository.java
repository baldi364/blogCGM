package it.cgmconsulting.myblog.repository;

import it.cgmconsulting.myblog.entity.Category;
import it.cgmconsulting.myblog.payload.response.CategoryVisibleResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Byte>
{
    boolean existsByCategoryName(String categoryName);

    Optional<Category> findByIdAndVisibleTrue(byte id);

    List<Category> findAllByVisibleTrue();

    /*
        - List<CategoryVisibleResponse> -> lista delle categorie visibili
        La query viene scritta in JPQL (Java Persistance Query Language)
        E' un mix tra Java e MySQL come sintassi
        Es. SELECT new (percorso e nome entità) (alias.attributi come scritti in Java)
            FROM nomeEntità"
     */
    @Query(value = "SELECT new it.cgmconsulting.myblog.payload.response.CategoryVisibleResponse(" +
                    "cat.id, " +
                    "cat.categoryName" +
                    ") FROM Category cat " +
                    "WHERE cat.visible = true ORDER BY cat.categoryName")
    List<CategoryVisibleResponse> getAllVisibleCategories();
}
