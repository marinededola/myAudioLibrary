package com.myaudiolibrary.web.controller;

import com.myaudiolibrary.web.model.Album;
import com.myaudiolibrary.web.model.Artist;
import com.myaudiolibrary.web.repository.AlbumRepository;
import com.myaudiolibrary.web.repository.ArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("/albums")
public class AlbumController {

    @Autowired
    AlbumRepository albumRepository;

    /**
     * Creation d'un album
     * @param album
     * @param artist
     * @return
     */
    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public RedirectView createOrSaveAlbum(Album album, Artist artist){
        return saveAlbum(album, artist);
    }

    /**
     * Enregistrement d'un nouvel album
     * @param album
     * @param artist
     * @return
     */
    private RedirectView saveAlbum(Album album, Artist artist){
        albumRepository.save(album);
        return new RedirectView("/artists/" + artist.getId());
    }

    /**
     * Suppression d'un album
     * @param id
     * @return
     */
    @GetMapping(value = "/{id}")
    public RedirectView deleteAlbum(@PathVariable(name = "id") Integer id){
        Optional<Album> albumId = albumRepository.findById(id);
        Integer artistId = albumId.get().getArtist().getId();

        albumRepository.deleteById(id);
        return new RedirectView("/artists/" + artistId);
    }

}
