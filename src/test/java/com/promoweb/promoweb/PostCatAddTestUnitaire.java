package com.promoweb.promoweb;

import com.promoweb.promoweb.controller.CategoryController;
import com.promoweb.promoweb.model.Category;
import com.promoweb.promoweb.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PostCatAddTestUnitaire {

    @InjectMocks
    private CategoryController categoryController;

    @Mock
    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testPostCatAdd() {
        // Créer une instance de Category à ajouter
        Category category = new Category();

        // Appeler la méthode postCatAdd
        String redirectUrl = categoryController.postCatAdd(category);

        // Vérifier que la méthode redirect renvoie vers "/admin/categories"
        assertEquals("redirect:/admin/categories", redirectUrl);
    }
}
