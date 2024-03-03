package com.promoweb.promoweb.service;

import com.promoweb.promoweb.model.Product;
import com.promoweb.promoweb.model.Promotion;
import com.promoweb.promoweb.repository.PromotionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
@Service
public class PromotionService {
    @Autowired
    private PromotionRepository promotionRepository;

    @Autowired
    private ProductService productService;


    public Optional<Promotion>getPromotionById(Long promoId) {
        return promotionRepository.findById(promoId);
    }

    public void updatePromotion(Long promoId, Promotion promotion) {
        this.deletePromotion(promoId);
        promotionRepository.save(promotion);
    }

    public void addPromotion(Promotion promotion) {
        promotionRepository.save(promotion);
    }

    public void deletePromotion(Long promoId) {
        promotionRepository.deleteById(promoId);
    }

    public void removePromotion(Promotion promotion) {
        promotionRepository.delete(promotion);
    }

    public Optional<Promotion> getPromotionByProduct(Product product) { return promotionRepository.findByProduct(product);}

    public Optional<Promotion> getLastPromotionByProduct(Product product) {
        List<Promotion> promotions = promotionRepository.findByProductOrderByEndPromoDesc(product);
        return promotions.isEmpty() ? Optional.empty() : Optional.of(promotions.get(0));
    }
}
