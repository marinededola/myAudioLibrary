package com.myaudiolibrary.web.controller;

import com.myaudiolibrary.web.model.Artist;
import com.myaudiolibrary.web.repository.ArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

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
     * Liste des artistes contenant le nom recherche
     *
     */
    @RequestMapping(method = RequestMethod.GET, value = "", params = "name")
    public String findArtistByName(final ModelMap model,
                                   @RequestParam(defaultValue = "name") String name){
        List<Artist> artists = artistRepository.findByName(name);
        Boolean all = false;
        model.put("all", all);

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
    public String listeArtists(final ModelMap model,
                               @RequestParam(defaultValue = "0") Integer page,
                               @RequestParam(defaultValue = "10") Integer size,
                               @RequestParam(defaultValue = "ASC") String sortDirection,
                               @RequestParam(defaultValue = "name") String sortProperty){
        PageRequest pageRequest = PageRequest.of(page, size,
                Sort.Direction.fromString(sortDirection), sortProperty);
        Page<Artist> pageArtists = artistRepository.findAll(pageRequest);
        long nbArtists = pageArtists.getTotalElements();
        Boolean all = true;
        model.put("all", all);
        model.put("artists", pageArtists);
        model.put("nbArtists", nbArtists);
        model.put("pageNumber", page + 1);
        model.put("previousPage", page - 1);
        model.put("nextPage", page + 1);
        model.put("start", page * size + 1);
        model.put("end", (page) * size + pageArtists.getNumberOfElements());
        return "listeArtists";
    }

    /**
     * Ouverture de la page de détails pour la création de l'artiste
     * @param model
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/new/artist")
    public String newArtist(final ModelMap model){
        model.put("artist", new Artist());
        return "detailArtist";
    }

    /**
     * Enregistrement des informations de création ou modification d'un artiste
     * @param artist
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public RedirectView createOrSaveArtist(Artist artist){
        artist = artistRepository.save(artist);
        return new RedirectView("/artists/" + artist.getId());
    }
}
