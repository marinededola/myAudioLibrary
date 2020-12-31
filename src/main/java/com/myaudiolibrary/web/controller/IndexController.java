package com.myaudiolibrary.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class IndexController {

    /**
     * Accueil Librairie audio
     * @param model
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "")
    public String index(final ModelMap model){
        return "accueil";
    }

}