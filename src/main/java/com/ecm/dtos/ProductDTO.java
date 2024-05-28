package com.ecm.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    @NotBlank(message = "name is required")
    @Size(min = 3, max = 200, message = "3 <= name <= 200")
    private String name;

    @Min(value = 0, message = "price >= 0")
    @Max(value = 10000000, message = "price <= 10.000.000")
    private Float price;

    private String description;

    @JsonProperty("category_id")
    private Long categoryId;
}
