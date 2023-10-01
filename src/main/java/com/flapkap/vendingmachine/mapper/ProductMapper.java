package com.flapkap.vendingmachine.mapper;

import com.flapkap.vendingmachine.dto.ProductDTO;
import com.flapkap.vendingmachine.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {


    ProductDTO toDTO(Product product);
    Product toEntity(ProductDTO productDTO);
}
