package com.myaudiolibrary.web.repository;

import com.myaudiolibrary.web.model.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ArtistRepository extends JpaRepository<Artist, Integer> {

    /**
     * Requête de recherche de nom d'artistes contenant le nom entré en paramètre
     * @param name
     * @return
     */
    @Query("SELECT a FROM #{#entityName} a WHERE a.name LIKE %:name%")
    List<Artist> findByName(@Param("name")String name);

    /**
     * Booléen qui détermine l'existence, ou nom, d'un artiste possèdant un nom identique à celui rentré en paramètre
     * @param name
     * @return
     */
    boolean existsByName(String name);
}