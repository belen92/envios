package sube.interviews.mareoenvios.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;


@Getter
@Setter
@NoArgsConstructor
public class ShippingItemResponse implements Serializable {

    public Long shipping_id;
    private Product product;
    private int productCount;
}