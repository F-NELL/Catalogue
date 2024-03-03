package com.promoweb.promoweb.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
@Tag(name = "Mercadona-admin", description = "L'accueil admin Mercadona")
public class AdminController {

    // Accéder à la page d'accueil admin
    @Operation(operationId = "adminHome", summary = "adminHome (Accéder à la page d'accueil admin)")
   // @ResponseBody
    @GetMapping("/admin")
    public String adminHome() {
        return "adminHome";
    }
}




