package sube.interviews.mareoenvios.entity;
import lombok.Data;
import java.util.List;

@Data
public class ShippingRequestDTO {
    private String customerFirstName;
    private String customerLastName;
    private String customerAddress;
    private String customerCity;
    private List<ShippingItemDTO> items;
}