package com.myaudiolibrary.web.controller;

import com.myaudiolibrary.web.model.Artist;
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

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("/artists")
public class ArtistController {

    @Autowired
    private ArtistRepository artistRepository;


    /**
     *
     * @param id
     * @param model
     * @return les informations de l'artiste de l'id selectionné
     */
    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public String findArtistById(@PathVariable Integer id, final ModelMap model) {
        Optional<Artist> artistOptionnal = artistRepository.findById(id);

        if (artistOptionnal.isEmpty()) {
            throw new EntityNotFoundException("L'artiste avec l'identifiant " + id + " n'a pas été trouvé");
        }

        model.put("artist", artistOptionnal.get());

        return "detailArtist";
    }

    /**
     * Liste des artistes contenant le nom recherché
     * @param name
     * @param model
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "", params = "name")
    public String findArtistByName(@RequestParam("name") String name, final ModelMap model){
        List<Artist> artists = artistRepository.findByName(name);
        model.put("artists", artists);
        Integer nbArtists = artists.size();
        model.put("nbArtists", nbArtists);
        return "listeArtists";
    }

    /**
     * Permet de récupérer la liste de tous les artistes de manière paginée et triée (par défaut, par ordre ascendant)
     * @param model
     * @param page
     * @param size
     * @param sortDirection
     * @param sortProperty
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "")
    public String listArtists(final ModelMap model,
                               @RequestParam(defaultValue = "0") Integer page,
                               @RequestParam(defaultValue = "10") Integer size,
                               @RequestParam(defaultValue = "ASC") String sortDirection,
                               @RequestParam(defaultValue = "name") String sortProperty){
        PageRequest pageRequest = PageRequest.of(page, size,
                Sort.Direction.fromString(sortDirection), sortProperty);
        Page<Artist> pageArtists = artistRepository.findAll(pageRequest);
        model.put("artists", pageArtists);
        model.put("pageNumber", page + 1);
        model.put("previousPage", page - 1);
        model.put("nextPage", page + 1);
        return "listeArtists";
    }
}
