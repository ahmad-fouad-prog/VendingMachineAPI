package com.flapkap.vendingmachine.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private Double cost;

    @Column(nullable = false)
    private Double amountAvailable;


    @ManyToOne
    @JoinColumn(name = "seller_id", referencedColumnName = "id")
    private User seller;

    public Product(String productName, Double cost, Double amountAvailable, User seller) {
        this.productName = productName;
        this.cost = cost;
        this.amountAvailable = amountAvailable;
        this.seller = seller;
    }
}
