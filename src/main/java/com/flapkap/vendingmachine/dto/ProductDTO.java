package com.flapkap.vendingmachine.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    private String productName;
    private Double cost;
    private Double amountAvailable;
    @JsonIgnore
    private Long sellerId;

    public ProductDTO(String productName, Double cost, double amountAvailable, Long sellerId) {
        this.productName = productName;
        this.cost = cost;
        this.amountAvailable = amountAvailable;
        this.sellerId = sellerId;
    }
}
