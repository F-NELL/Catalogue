package com.promoweb.promoweb.controller;

import com.promoweb.promoweb.model.Product;
import com.promoweb.promoweb.model.Promotion;
import com.promoweb.promoweb.repository.ProductRepository;
import com.promoweb.promoweb.repository.PromotionRepository;
import com.promoweb.promoweb.service.ProductService;
import com.promoweb.promoweb.service.PromotionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequestMapping("/")
@Tag(name = "Mercadona-promotion", description = "Les promotions Mercadona")
public class PromotionController {
    @Autowired
    private PromotionService promotionService;
    @Autowired
    ProductService productService;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    PromotionRepository promotionRepository;

    public PromotionController(PromotionService promotionService) {
        this.promotionService = promotionService;
    }

    // Afficher le formulaire et les données de promotions si elles existent pour ce produit
    @Operation(operationId = "showPromotionForm", summary = "showPromotionForm (Afficher le formulaire et les données de promotions si elles existent pour ce produit)")
    @GetMapping("/admin/products/promotion/{promoId}")
    public String showPromotionForm(@PathVariable("promoId") Long promoId, Model model) {
        Optional<Product> productOptional = productService.getProductById(promoId);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            Optional<Promotion> promotionOptional = promotionService.getPromotionByProduct(product);

            if (promotionOptional.isPresent()) {
                Promotion promotion = promotionOptional.get();
                model.addAttribute("product", product);
                model.addAttribute("promotion", promotion);
            } else {
                model.addAttribute("product", product);
                model.addAttribute("promotion", new Promotion());
            }

            return "promotion";
        } else {
            return "redirect:/admin/products";
        }
    }

    @Operation(operationId = "addPromotion", summary = "addPromotion (Ajouter une promotion)")
    @PostMapping("/admin/products/promotion/{promoId}")
    public String addPromotion(@PathVariable("promoId") Long promoId,
                               @ModelAttribute("promotion") Promotion promotion) {
        Optional<Product> optionalProduct = productService.getProductById(promoId);
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();


            // vérifier si la promotion est valide
            LocalDate today = LocalDate.now();
            LocalDate startPromo = promotion.getStartPromo();
            LocalDate endPromo = promotion.getEndPromo();
            if (startPromo.isBefore(today) || endPromo.isBefore(today) || startPromo.isAfter(endPromo)) {
                // Si la promotion est périmée, elle est supprimée
                product.setPromoPrice(null);
                promotionService.removePromotion(promotion);
                productService.updateProduct(product);
                return "redirect:/admin/products";
            }

            Promotion existingPromotion = promotionService.getPromotionByProduct(product)
                    .orElseGet(Promotion::new);

            // intégrer la promotion
            existingPromotion.setProduct(product);
            existingPromotion.setPerPromo(promotion.getPerPromo());
            existingPromotion.setStartPromo(promotion.getStartPromo());
            existingPromotion.setEndPromo(promotion.getEndPromo());
            double promoPrice = product.getPrice() * (100 - promotion.getPerPromo()) / 100;
            product.setPromoPrice(promoPrice);

            // sauvegarder la promotion et le produit
            productService.updateProduct(product);
            promotionService.addPromotion(existingPromotion);

            return "redirect:/admin/products";
        } else {
            return "redirect:/admin/products";
        }
    }

    @Operation(operationId = "updatePromotion", summary = "updatePromotion (Mettre à jour une promotion)")
    @PutMapping("/admin/products/promotion/{promoId}")
    public void updatePromotion(@PathVariable("promoId") Long promoId,
                                @ModelAttribute Promotion updatedPromotion) {
        promotionService.updatePromotion(promoId, updatedPromotion);
    }
}



