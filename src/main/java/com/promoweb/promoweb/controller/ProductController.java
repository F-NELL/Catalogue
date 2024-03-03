package com.promoweb.promoweb.controller;

import com.promoweb.promoweb.model.Product;
import com.promoweb.promoweb.service.CategoryService;
import com.promoweb.promoweb.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;
@Controller
//@RequestMapping("/promoweb")
public class ProductController {
    @Autowired
    CategoryService categoryService;
    @Autowired
    ProductService productService;

    // uploadDir = chemin vers le dossier d'images téléchargées
    @Value("${upload.dir}")
    private String uploadDir;

    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }

    // Affichage de la liste des produits
    @GetMapping("/admin/products")
    public String listProducts(Model model) {
        model.addAttribute("products", productService.getAllProduct());
        return "products";
    }

    // Affichage de la page d'ajout ou de modification de produits
    @GetMapping("/admin/products/add")
    public String addProductGet(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.getAllCategory());
        return "productsAdd";
    }


    // Ajout de produit
    @PostMapping("/admin/products/add")
    public String addProductPost(@ModelAttribute Product product,
                                 @RequestParam("uploadedImage") MultipartFile file,
                                 Model model) throws IOException {

        // Sauvegarde du produit en base de donnée
        product.setCategory(categoryService.getCategoryById(product.getCategory().getId()).get());

        // Enregistrement du nouveau fichier d'image
        if (!file.isEmpty()) {
            // Obtenir un UUID unique pour les images
            String imageUUID = UUID.randomUUID().toString();
            Path fileNameAndPath = Paths.get(uploadDir, imageUUID);
            Files.write(fileNameAndPath, file.getBytes());
            product.setImage(imageUUID);
            model.addAttribute("imageUrl", "/uploadedImages/" + imageUUID);
        }

        productService.addProduct(product);

        return "redirect:/admin/products";
    }

    // Afficher les éléments du produit à mettre à jour
    @GetMapping("/admin/products/update/{id}")
    public String updateProductGet(@PathVariable long id, Model model) {
        // Récupération du produit existant
        Optional<Product> optionalProduct = productService.getProductById(id);
        if (optionalProduct.isEmpty()) {
            return "redirect:/admin/products";
        }
        Product product = optionalProduct.get();

        // Ajout de l'objet Product au modèle
        model.addAttribute("product", product);
        // Ajout de la catégorie
        model.addAttribute("categories", categoryService.getAllCategory());
        // Ajout de l'image
        String imageUrl = "/uploadedImages/" + product.getImage();
        model.addAttribute("imageUrl", imageUrl);
        // Affichage de la vue de mise à jour
        return "productsAdd";
    }

    // Mettre à jour le produit
    @PutMapping("/admin/products/update")
    public String updateProductPut(@ModelAttribute Product product,
                                   @RequestParam(value = "uploadedImage", required = false) MultipartFile file,
                                   Model model) throws IOException {
        // Récupération du produit à partir de la base de données
        Optional<Product> optionalProduct = productService.getProductById(product.getId());
        if (optionalProduct.isEmpty()) {
            return "redirect:/admin/products";
        }
        Product currentProduct = optionalProduct.get();

        // Vérification si l'image doit être mise à jour
        if (file != null && !file.isEmpty()) {
            String oldImageUUID = currentProduct.getImage();
            if (oldImageUUID != null) {
                Path oldFileNameAndPath = Paths.get(uploadDir, oldImageUUID);
                if (Files.exists(oldFileNameAndPath)) {
                    try {
                        Files.delete(oldFileNameAndPath);
                    } catch (IOException e) { // Gérer l'exception
                        throw new IOException("Oups erreur lors de la suppression de l'ancienne photo");
                    }
                }
            }

            // Enregistrement de la nouvelle image
            String imageUUID = UUID.randomUUID().toString();
            Path fileNameAndPath = Paths.get(uploadDir, imageUUID);
            Files.write(fileNameAndPath, file.getBytes());
            currentProduct.setImage(imageUUID);

            // Mise à jour de l'URL de l'image
            model.addAttribute("imageUrl", "/uploadedImages/" + imageUUID);

        } else {

            // Aucune nouvelle image n'a été téléchargée
            // Conserver la valeur actuelle de l'image dans le produit
            String imageUUID = currentProduct.getImage();

            // Mise à jour de l'URL de l'image
            model.addAttribute("imageUrl", "/uploadedImages/" + imageUUID);
        }

        // Mise à jour des autres valeurs du produit
        currentProduct.setName(product.getName());
        currentProduct.setCategory(categoryService.getCategoryById(product.getCategory().getId()).get());
        currentProduct.setPrice(product.getPrice());
        currentProduct.setDescription(product.getDescription());


        // Récupération du produit mis à jour
        Optional<Product> updatedProduct = productService.getProductById(currentProduct.getId());
        if (updatedProduct.isPresent()) {
            // Ajout de l'objet Product mis à jour au modèle
            model.addAttribute("product", updatedProduct.get());
        }
        productService.updateProduct(currentProduct);

        return "productsAdd";
    }

    // Suppression du produit
    @GetMapping("/admin/products/delete/{id}")
    public String deleteProduct(@PathVariable long id,  Model model) {
        Product product = productService.getProductById(id).get();
        String imageUUID = product.getImage();
        productService.removeProductById(id);

        // Suppression de l'image associée au produit à supprimer
        if (imageUUID != null) {
            Path imagePath = Paths.get(uploadDir, imageUUID);
            try {
                Files.delete(imagePath);
            } catch (IOException e) {
                model.addAttribute("error", "Une erreur s'est produite lors de la suppression de la photo du produit.");
            }
        }
        return "redirect:/admin/products";
    }
}



