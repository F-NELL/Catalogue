package com.promoweb.promoweb.controller;


import com.promoweb.promoweb.service.CategoryService;
import com.promoweb.promoweb.service.ProductService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequestMapping("/")
@Tag(name = "Mercadona-catalog", description = "Le catalogue Mercadona pour les visiteurs")

public class HomeController {
    @Autowired
    CategoryService categoryService;
    @Autowired
    ProductService productService;

    @GetMapping({"/", "/home"})
    public String Home(Model model) {
        return "catalog";
    }

    @GetMapping("/catalog")
    public String catalog(Model model) {
        model.addAttribute("categories", categoryService.getAllCategory());
        model.addAttribute("products", productService.getAllProduct());
        return "catalog";
    }

    @GetMapping("/catalog/category/{id}")
    public String catalogByCategory (Model model,@PathVariable Long id){
        model.addAttribute("categories", categoryService.getAllCategory());
        model.addAttribute("products", productService.getAllProductsByCategoryId(id));
        return "catalog";
            }
        }