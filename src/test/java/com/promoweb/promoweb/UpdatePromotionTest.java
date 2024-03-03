package com.promoweb.promoweb;

import com.promoweb.promoweb.controller.PromotionController;
import com.promoweb.promoweb.model.Product;
import com.promoweb.promoweb.model.Promotion;
import com.promoweb.promoweb.repository.PromotionRepository;
import com.promoweb.promoweb.service.ProductService;
import com.promoweb.promoweb.service.PromotionService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

@SpringBootTest
public class UpdatePromotionTest {

    @Mock
    private PromotionRepository promotionRepository;

    @Mock
    private ProductService productService;

    @InjectMocks
    private PromotionService promotionService;

    @InjectMocks
    private PromotionController promotionController;

    @Test
    public void testUpdatePromotion() {
        // Créer un objet promotion avec des valeurs
        Product product = new Product();
        product.setId(1L);
        Promotion promotion = new Promotion(product, 20, LocalDate.now(), LocalDate.now().plusDays(7));

        // Simuler les méthodes promotionRepository et productService
        when(promotionRepository.save(promotion)).thenReturn(promotion);

        // Appeler la méthode updatePromotion avec les objets simulés
        promotionService.updatePromotion(1L, promotion);

        // Vérifier la mise à jour
        verify(promotionRepository).save(promotion);
    }
}
