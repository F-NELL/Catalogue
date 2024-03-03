package com.promoweb.promoweb.controller;

import com.promoweb.promoweb.model.Category;
import com.promoweb.promoweb.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/")
@Tag(name = "Mercadona-category", description = "Les catégories de produits Mercadona")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    // Afficher les catégories
    @Operation(operationId = "getCat", summary = "getCat (Afficher les catégories)")
    @GetMapping("/admin/categories")
    public String getCat(Model model) {
        model.addAttribute("categories", categoryService.getAllCategory());
        return "categories";
    }

    //Afficher la page d'ajout de catégorie
    @Operation(operationId = "getCatAdd", summary = "getCatAdd (Afficher la page d'ajout de catégorie)")
    @GetMapping("/admin/categories/add")
    public String getCatAdd(Model model) {
        model.addAttribute("category", new Category());
        return "categoriesAdd";
    }

    // Ajouter une catégorie
    @Operation(operationId = "postCatAdd", summary = "postCatAdd (Ajouter une catégorie)")
    @PostMapping("/admin/categories/add")
    public String postCatAdd(@ModelAttribute("category") Category category) {
        categoryService.addCategory(category);
        return "redirect:/admin/categories";
    }

    // Supprimer une catégorie
    @Operation(operationId = "deleteCat", summary = "deleteCat (Supprimer une catégorie)")
    @GetMapping("/admin/categories/delete/{id}")
    public String deleteCat(@PathVariable Long id) {
        categoryService.removeCategoryById(id);
        return "redirect:/admin/categories";
    }

    // Mettre à jour une catégorie
    @Operation(operationId = "updateCat", summary = "updateCat (Mettre à jour une catégorie)")
    @GetMapping("/admin/categories/update/{id}")
    public String updateCat(@PathVariable Long id, Model model) {
        Optional<Category> category = categoryService.getCategoryById(id);
        if (category.isPresent()) {
            model.addAttribute("category", category.get());
            return "categoriesAdd";
        } else
            model.addAttribute("errorMessage", "Oups la catégorie est cachée.");
        return "404";
    }
}
