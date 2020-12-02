package com.myaudiolibrary.web.service;

import com.myaudiolibrary.web.exception.ArtistException;
import com.myaudiolibrary.web.exception.ConflictException;
import com.myaudiolibrary.web.model.*;
import com.myaudiolibrary.web.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Validated
public class ArtistService {

    public static final int PAGE_SIZE_MIN = 10;
    public static final int PAGE_SIZE_MAX = 100;
    public static final int PAGE_MIN = 0;
    private static final String PAGE_VALID_MESSAGE = "La taille de la page doit être comprise entre 10 et 100";

    @Autowired
    private ArtistRepository artistRepository;

    public Artist findById(Integer id){
        Optional<Artist> artist = artistRepository.findById(id);
        if(!artist.isPresent()){
            throw new EntityNotFoundException("L'artiste avec l'identifiant " + id + " n'a pas été trouvé.");
        }
        return artist.get();
    }

    public List<Artist> findByName(String name) {
        List<Artist> artist =  this.artistRepository.findByName(name);
        return artist;
    }

    public Page<Artist> findAllArtists(
            @Min(message = "Le numéro de page ne peut pas être inférieur à 0", value = PAGE_MIN)
                    Integer page,
            @Min(value = PAGE_SIZE_MIN, message = PAGE_VALID_MESSAGE)
            @Max(value = PAGE_SIZE_MAX, message = PAGE_VALID_MESSAGE)
                    Integer size,
            String sortProperty,
            Sort.Direction sortDirection
    ) {
        //Vérification de sortProperty
        if(Arrays.stream(Artist.class.getDeclaredFields()).
                map(Field::getName).
                filter(s -> s.equals(sortProperty)).count() != 1){
            throw new IllegalArgumentException("La propriété " + sortProperty + " n'existe pas");
        };

        Pageable pageable = PageRequest.of(page,size,sortDirection, sortProperty);
        Page<Artist> artists = artistRepository.findAll(pageable);
        if(page >= artists.getTotalPages()){
            throw new IllegalArgumentException("Le numéro de page ne peut pas être supérieur à " + artists.getTotalPages());
        } else if(artists.getTotalElements() == 0){
            throw new EntityNotFoundException("Il n'y a aucun artistes dans la base de données");
        }
        return artists;
    }

    public <T extends Artist> T createArtist(@Valid T e) throws ConflictException {
        if(artistRepository.existsByName(e.getName())) {
            throw new ConflictException("L'artiste nommé:" + e.getName() + " existe déjà");
        }
        return artistRepository.save(e);
    }

    public <T extends Artist> T updateArtist(Integer id, @Valid T artist) {
        if(!artistRepository.existsById(id)) {
            throw new EntityNotFoundException("L'artiste avec l'identifiant: " + id + " n'existe pas");
        }
        if(!id.equals(artist.getId())) {
            throw new IllegalArgumentException("Requête invalide");
        }
        return artistRepository.save(artist);
    }

    public void deleteArtist(Integer id) throws ArtistException {
        artistRepository.deleteById(id);
    }

}
