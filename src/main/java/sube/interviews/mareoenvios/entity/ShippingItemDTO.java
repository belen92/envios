package sube.interviews.mareoenvios.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShippingItemDTO {
    private Long productId;
    private int productCount;


}