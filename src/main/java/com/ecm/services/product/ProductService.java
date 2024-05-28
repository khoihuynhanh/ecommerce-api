package com.ecm.services.product;

import com.ecm.dtos.ProductDTO;
import com.ecm.dtos.ProductImageDTO;
import com.ecm.models.Category;
import com.ecm.models.Product;
import com.ecm.models.ProductImage;
import com.ecm.repositories.CategoryRepository;
import com.ecm.repositories.ProductImageRepository;
import com.ecm.repositories.ProductRepository;
import com.ecm.responses.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductImageRepository productImageRepository;

    @Override
    public Product createProduct(ProductDTO productDTO) throws Exception {
        Category existingCategory = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new Exception("not found category"));
        Product product = Product.builder()
                .name(productDTO.getName())
                .price(productDTO.getPrice())
                .description(productDTO.getDescription())
                .category(existingCategory)
                .build();
        return productRepository.save(product);
    }

    @Override
    public Page<ProductResponse> getAllProducts(PageRequest pageRequest) {
        return productRepository.findAll(pageRequest).map(
                product -> ProductResponse.formProduct(product)
        );
    }

    @Override
    public Product getProductById(Long id) throws Exception {
        return productRepository.findById(id)
                .orElseThrow(() -> new Exception("not found id"));
    }

    @Override
    public ProductResponse updateProduct(Long id, ProductDTO productDTO) throws Exception {
        Product existingProduct = getProductById(id);
        Category existingCategory = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new Exception("not found category"));
        if (existingProduct != null) {
            existingProduct.setName(productDTO.getName());
            existingProduct.setPrice(productDTO.getPrice());
            existingProduct.setDescription(productDTO.getDescription());
            existingProduct.setCategory(existingCategory);
            existingProduct.setUpdatedAt(LocalDateTime.now());
            Product updateProduct = productRepository.save(existingProduct);
            return ProductResponse.formProduct(updateProduct);
        }
        return null;
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public boolean existsByName(String name) {
        return productRepository.existsByName(name);
    }

    @Override
    public ProductImage createProductImage(Long id, ProductImageDTO productImageDTO) throws Exception {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new Exception("not found"));
        ProductImage images = ProductImage.builder()
                .product(existingProduct)
                .imageUrl(productImageDTO.getImageUrl())
                .build();
        int size = productImageRepository.findByProductId(id).size();
        if (size >= 5) {
            throw new IOException("size must be <= 5");
        }
        return productImageRepository.save(images);
    }
}
