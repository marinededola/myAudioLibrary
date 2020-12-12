package com.myaudiolibrary.web.controller;

import com.myaudiolibrary.web.exception.ArtistException;
import com.myaudiolibrary.web.exception.ConflictException;
import com.myaudiolibrary.web.model.*;

import com.myaudiolibrary.web.service.ArtistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/artists")
public class ArtistController {

        @Autowired
        private ArtistService artistService;

        /**
         * Permet de récupérer les informations d'un artiste à partir de son identifiant
         *
         * @param id Identifiant de l'artiste
         * @return l'artiste et ses informations, si l'identifiant est trouvé, ou une erreur 404 sinon.
         */
        @RequestMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
        public Artist findById(@PathVariable(value = "id") Integer id){
                return artistService.findById(id);
        }

        /**
         * Permet de récupérer les informations des artistes ayant un nom qui contient le nom rentré en paramètre
         * @param name Le nom, ou une partie du nom de l'artiste recherché
         * @return le ou les artistes contenant le nom rentré en paramètre ou une erreur 404 si aucun artiste ne correspond.
         */
        @RequestMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET, params = "name")
        public List<Artist> findByName(@RequestParam("name") String name){
                return artistService.findByName(name);
        }


        /**
         * Permet de récupérer la liste de tous les artistes de manière paginée et triée (par défaut, par ordre ascendant)
         *
         * @param page Numéro de la page en partant de 0
         * @param size Taille de la page
         * @param sortDirection Tri ascendant ASC ou descendant DESC
         * @param sortProperty Propriété utilisée par le tri
         * @return Une page contenant les employés
         */
        @RequestMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
        public Page<Artist> findAll(
                @RequestParam(value = "page", defaultValue = "0") Integer page,
                @RequestParam(value = "size", defaultValue = "10") Integer size,
                @RequestParam(value = "sortProperty", defaultValue = "name") String sortProperty,
                @RequestParam(value = "sortDirection", defaultValue = "ASC") Sort.Direction sortDirection){
                return artistService.findAllArtists(page, size, sortProperty, sortDirection);
        }

        /**
         * Permet de gérer la méthode POST de création d'un artiste
         * @param artist
         * @return la création d'un artiste ou une erreur 500 si le nom de l'artiste existe déjà dans la base.
         * @throws ConflictException
         */
        @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, value = "")
        public Artist createArtist(@RequestBody Artist artist) throws ConflictException {
                return this.artistService.createArtist(artist);
        }

        /**
         * Permet de gérer la méthode de modification d'un artiste selon son id
         * @param id
         * @param artist
         * @return la modification d'un artiste ou une erreur 404 si aucun artiste ne possède cet id
         */
        @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
        public Artist updateArtist(@PathVariable("id") Integer id, @RequestBody Artist artist){
                return this.artistService.updateArtist(id,artist);
        }

        /**
         * Permet de gérer la méthode de suppression d'un artiste selon son id
         * @param id
         * @throws ArtistException
         * Genère la suppression d'un artiste ou une erreur 404 si aucun artiste ne possède cet id
         */
        @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
        @ResponseStatus(value = HttpStatus.NO_CONTENT)
        public void deleteArtist(@PathVariable("id") Integer id) throws ArtistException {
                this.artistService.deleteArtist(id);
        }

}
