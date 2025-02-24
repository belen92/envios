package sube.interviews.mareoenvios.entity;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;

@Entity
@Table(name = "shipping_item")
@Getter
@Setter
public class ShippingItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "shipping_id", nullable = false)
    public Shipping shipping;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "product_count", nullable = false)
    private int productCount;

}

