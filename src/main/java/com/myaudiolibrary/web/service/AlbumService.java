package com.myaudiolibrary.web.service;

import com.myaudiolibrary.web.exception.ConflictException;
import com.myaudiolibrary.web.model.*;
import com.myaudiolibrary.web.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;

@Service
@Validated
public class AlbumService {

    @Autowired
    private AlbumRepository albumRepository;

    public <T extends Album> T createAlbum(@Valid T e) throws ConflictException {
        return albumRepository.save(e);
    }

    public void deleteAlbum(Integer id) {
        albumRepository.deleteById(id);
    }

}
