package com.promoweb.promoweb.repository;

import com.promoweb.promoweb.model.Product;
import com.promoweb.promoweb.model.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Long> {

    Optional<Promotion> findByProduct(Product product);

    List<Promotion> findByProductOrderByEndPromoDesc(Product product);
}
