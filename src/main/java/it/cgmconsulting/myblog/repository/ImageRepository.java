package it.cgmconsulting.myblog.repository;

import it.cgmconsulting.myblog.entity.Image;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image, Integer>
{
    //le query di aggiornamento e delete devono avere le annotation @Modifying e @Transactional
    @Modifying
    @Transactional
    @Query (value = "DELETE FROM content_images WHERE image_id = :imageId", nativeQuery = true)
    void deleteImageFromContent(int imageId);
}
