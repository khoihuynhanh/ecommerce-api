package com.ecm.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDetailDTO {
    @JsonProperty("order_id")
    @Min(value = 1)
    private Long orderId;

    @JsonProperty("product_id")
    @Min(value = 1)
    private Long productId;

    @Min(value = 0)
    private Float price;

    @JsonProperty("number_of_products")
    @Min(value = 1)
    private int numberOfProducts;

    @JsonProperty("total_money")
    private Float totalMoney;

    private String color;
}
