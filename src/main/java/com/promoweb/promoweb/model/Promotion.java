package com.promoweb.promoweb.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Table(name = "promotion")
@Getter
@Setter
public class Promotion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "promo_id", updatable = false)
    private Long promoId;
    @OneToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;

    @Positive
    @Column(name = "per_promo")
    private int perPromo;

    @Column(name = "start_promo")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate startPromo;

    @Column(name = "end_promo")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate endPromo;

    public Promotion() {
    }

    public Promotion(Product product, int perPromo, LocalDate startPromo, LocalDate endPromo) {
        this.product = product;
        this.perPromo = perPromo;
        this.startPromo = startPromo;
        this.endPromo = endPromo;
    }
}


