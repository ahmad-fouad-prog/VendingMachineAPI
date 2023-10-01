package com.flapkap.vendingmachine.controller;

import com.flapkap.vendingmachine.dto.ProductDTO;
import com.flapkap.vendingmachine.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/seller")
public class SellerController {

    @Autowired
    private ProductService productService;

    // Add a product
    @PostMapping("/products")
    public ProductDTO addProduct(@RequestBody ProductDTO productDto, Authentication authentication) {
        return productService.createProductForSeller(authentication.getName(), productDto);
    }

    // Update a product
    @PutMapping("/products/{productId}")
    public ProductDTO updateProduct(@PathVariable Long productId, @RequestBody ProductDTO productDto, Authentication authentication) {
        return productService.updateProductForSeller(authentication.getName(), productId, productDto);
    }

    // Delete a product
    @DeleteMapping("/products/{productId}")
    public void deleteProduct(@PathVariable Long productId, Authentication authentication) {
        productService.deleteProductForSeller(authentication.getName(), productId);
    }

    // Get all products of a seller
    @GetMapping("/products")
    public List<ProductDTO> getSellerProducts(Authentication authentication) {
        return productService.getProductsBySeller(authentication.getName());
    }
}
