package com.ecm.controllers;

import com.ecm.dtos.ProductDTO;
import com.ecm.dtos.ProductImageDTO;
import com.ecm.models.Product;
import com.ecm.models.ProductImage;
import com.ecm.responses.ProductListResponse;
import com.ecm.responses.ProductResponse;
import com.ecm.services.product.IProductService;
import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("${api.prefix}/products")
@RequiredArgsConstructor
public class ProductController {
    private final IProductService productService;

    @PostMapping()
    public ResponseEntity<?> createProduct(
            @Validated @RequestBody ProductDTO productDTO,
            BindingResult result
    ) throws Exception {
        if (result.hasErrors()) {
            List<String> errorMessage = result.getFieldErrors()
                    .stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .toList();
            return ResponseEntity.badRequest().body(errorMessage);
        }
        Product product = productService.createProduct(productDTO);
        return ResponseEntity.ok(product);
    }

    @PostMapping(value = "/uploads/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadImages(
            @PathVariable("id") Long prooductId,
            @ModelAttribute("file") List<MultipartFile> files
    ) throws Exception {
        Product existingProduct = productService.getProductById(prooductId);
        files = files == null ? new ArrayList<MultipartFile>() : files;
        if (files.size() >= 5) {
            return ResponseEntity.badRequest().body("images <= 5");
        }
        List<ProductImage> productImages = new ArrayList<>();
        for (MultipartFile file : files) {
            if (file.getSize() == 0) {
                continue;
            }
            if (file.getSize() > 10 * 1024 * 1024) {
                return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                        .body("file too large");
            }
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                        .body("file must be image");
            }
            String fileName = storeFile(file);
            ProductImage productImage = productService.createProductImage(
                    existingProduct.getId(),
                    ProductImageDTO.builder()
                            .imageUrl(fileName)
                            .build()
            );
            productImages.add(productImage);
        }
        return ResponseEntity.ok("upload");
    }

    private String storeFile(MultipartFile file) throws IOException {
        if (file.getOriginalFilename() == null) {
            throw new IOException("invalid format");
        }
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String uniqueFileName = UUID.randomUUID().toString() + "_" + fileName;
        Path uploadDir = Paths.get("uploads");
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }
        Path destination = Paths.get(uploadDir.toString(), uniqueFileName);
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        return uniqueFileName;
    }

    @GetMapping()
    public ResponseEntity<ProductListResponse> getAllProducts(
            @RequestParam("page")       int page,
            @RequestParam("limit")      int limit
    ) {
        PageRequest pageRequest = PageRequest.of(
                page, limit,
                Sort.by("createdAt").descending()
        );
        Page<ProductResponse> productResponses = productService.getAllProducts(pageRequest);
        int totalPages = productResponses.getTotalPages();
        List<ProductResponse> productList = productResponses.getContent();
        return ResponseEntity.ok(ProductListResponse.builder()
                        .products(productList)
                        .totalPages(totalPages)
                        .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProduct(
            @PathVariable("id") Long productId
    ) throws Exception {
        Product product = productService.getProductById(productId);
        return ResponseEntity.ok(ProductResponse.formProduct(product));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable("id") Long productId,
            @RequestBody ProductDTO productDTO

    ) throws Exception {
        ProductResponse updateProduct = productService.updateProduct(productId, productDTO);
        return ResponseEntity.ok(updateProduct);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(
            @PathVariable("id") Long productId
    ) {
        productService.deleteProduct(productId);
        return ResponseEntity.ok("delete with id = " + productId);
    }

//    @PostMapping("/generate_products")
    public ResponseEntity<String> generateProducts() throws Exception {
        Faker faker = new Faker();
        for (int i = 0; i <= 50; i++) {
            String productName = faker.commerce().productName();
            if (productService.existsByName(productName)) {
                continue;
            }
            ProductDTO productDTO = ProductDTO.builder()
                    .name(productName)
                    .price((float)faker.number().numberBetween(10, 90000000))
                    .description(faker.lorem().sentence())
                    .categoryId((long) faker.number().numberBetween(1, 5))
                    .build();
            productService.createProduct(productDTO);
        }
        return ResponseEntity.ok("generate");
    }
}
