package com.ecm.services.product;

import com.ecm.dtos.ProductDTO;
import com.ecm.dtos.ProductImageDTO;
import com.ecm.models.Product;
import com.ecm.models.ProductImage;
import com.ecm.responses.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface IProductService {
    Product createProduct(ProductDTO productDTO) throws Exception;
    Page<ProductResponse> getAllProducts(PageRequest pageRequest);
    Product getProductById(Long id) throws Exception;
    ProductResponse updateProduct(Long id, ProductDTO productDTO) throws Exception;
    void deleteProduct(Long id);
    boolean existsByName(String name);
    ProductImage createProductImage(Long id, ProductImageDTO productImageDTO) throws Exception;
}
