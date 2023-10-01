package com.flapkap.vendingmachine.service;

import com.flapkap.vendingmachine.dto.ProductDTO;
import com.flapkap.vendingmachine.exception.CustomException;
import com.flapkap.vendingmachine.exception.ErrorCode;
import com.flapkap.vendingmachine.mapper.ProductMapper;
import com.flapkap.vendingmachine.model.Product;
import com.flapkap.vendingmachine.model.User;
import com.flapkap.vendingmachine.repositoy.ProductRepository;
import com.flapkap.vendingmachine.repositoy.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private UserService userService;

    public ProductDTO createProductForSeller(String username, ProductDTO productDto) {
        User seller = userService.findByUsername(username);
        userService.verifySellerOrAdminRole(seller.getId());

        Product product = new Product(productDto.getProductName(), productDto.getCost(), productDto.getAmountAvailable(), seller);
        Product savedProduct = productRepository.save(product);
        return productMapper.toDTO(savedProduct);
    }

    public Product findById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));
    }

    public ProductDTO updateProductForSeller(String username, Long productId, ProductDTO productDto) {
        User seller = userService.findByUsername(username);
        userService.verifySellerOrAdminRole(seller.getId());

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));

        product.setProductName(productDto.getProductName());
        product.setCost(productDto.getCost());
        product.setAmountAvailable(productDto.getAmountAvailable());

        Product updatedProduct = productRepository.save(product);
        return productMapper.toDTO(updatedProduct);
    }

    public ProductDTO updateProduct(Long sellerId, Long productId, ProductDTO productDto) {
        User seller = userService.findByIdEntity(sellerId);
        if (seller == null) {
            throw new CustomException(ErrorCode.OPERATION_NOT_ALLOWED);
        }

        userService.verifySellerOrAdminRole(seller.getId());

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));

        if (!seller.getId().equals(productDto.getSellerId())) {
            throw new CustomException(ErrorCode.OPERATION_NOT_ALLOWED);
        }

        product.setProductName(productDto.getProductName());
        product.setCost(productDto.getCost());
        product.setAmountAvailable(productDto.getAmountAvailable());

        Product updatedProduct = productRepository.save(product);
        return productMapper.toDTO(updatedProduct);
    }


    public void deleteProductForSeller(String username, Long productId) {
        User seller = userService.findByUsername(username);
        userService.verifySellerOrAdminRole(seller.getId());

        if (!productRepository.existsById(productId)) {
            throw new CustomException(ErrorCode.RESOURCE_NOT_FOUND);
        }
        productRepository.deleteById(productId);
    }

    public List<ProductDTO> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(productMapper::toDTO).collect(Collectors.toList());
    }

    public List<ProductDTO> getProductsBySeller(String username) {
        User seller = userService.findByUsername(username);
        userService.verifySellerOrAdminRole(seller.getId());

        List<Product> products = productRepository.findBySeller(seller);
        return products.stream().map(productMapper::toDTO).collect(Collectors.toList());
    }
}
