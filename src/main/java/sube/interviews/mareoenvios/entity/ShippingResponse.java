package sube.interviews.mareoenvios.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class ShippingResponse implements Serializable {

    private Shipping shipping;
    private List<ShippingItemResponse> shippingItems;

}
