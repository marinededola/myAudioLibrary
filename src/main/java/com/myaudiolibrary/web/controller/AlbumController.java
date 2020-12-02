package com.myaudiolibrary.web.controller;

import com.myaudiolibrary.web.exception.ArtistException;
import com.myaudiolibrary.web.exception.ConflictException;
import com.myaudiolibrary.web.model.Album;
import com.myaudiolibrary.web.service.AlbumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/albums")
public class AlbumController {

    @Autowired
    private AlbumService albumService;

    /**
     * Permet de gérer la méthode POST de création d'un album
     * @param album
     * @return la création d'un album
     * @throws ConflictException
     */
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, value = "")
    public Album createAlbum(@RequestBody Album album) throws ConflictException {
        return this.albumService.createAlbum(album);
    }

    /**
     * Permet de gérer la méthode de suppression d'un album selon son id
     * @param id
     * @throws ArtistException
     * Genère la suppression d'un album ou une erreur 404 si aucun album ne possède cet id
     */
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteAlbum(@PathVariable("id") Integer id) throws ArtistException {
        this.albumService.deleteAlbum(id);
    }

}
