package com.promoweb.promoweb;


import com.promoweb.promoweb.controller.ProductController;
import com.promoweb.promoweb.model.Product;
import com.promoweb.promoweb.repository.ProductRepository;
import com.promoweb.promoweb.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.verification.VerificationMode;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ProductListTest {
    @InjectMocks
    private ProductController productController;

    @Mock
    private ProductService productService;

    @Mock
    private Model model;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testListProducts() {
        // Créer une liste de produits factice pour simuler le résultat
        List<Product> fakeProducts = new ArrayList<>();
        fakeProducts.add(new Product()); // Utiliser le constructeur sans arguments
        fakeProducts.add(new Product()); // Utiliser le constructeur sans arguments

        // Définir les valeurs des propriétés des produits factices
        fakeProducts.get(0).setName("Produit 1");
        fakeProducts.get(1).setName("Produit 2");

        // Définir le comportement attendu
        when(productService.getAllProduct()).thenReturn(fakeProducts);

        // Appeler la méthode
        String result = productController.listProducts(model);

        // Vérifier que la méthode a été appelée (une fois)
        verify(productService, times(1)).getAllProduct();

        // Vérifier que le modèle a été mis à jour avec la liste de produits
        verify(model, times(1)).addAttribute("products", fakeProducts);

        // Vérifier que la vue retournée est ok
        assertEquals("products", result);
    }
}