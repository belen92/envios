package sube.interviews.mareoenvios.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sube.interviews.mareoenvios.entity.ShippingItem;

import org.springframework.data.domain.Pageable;
import java.util.List;

public interface ShippingItemRepository extends JpaRepository<ShippingItem, Long> {


    @Query("SELECT si.product.description AS description, SUM(si.productCount) AS totalQuantity " +
            "FROM ShippingItem si " +
            "GROUP BY si.product.description " +
            "ORDER BY totalQuantity DESC")
    List<Object[]> findTop3MostShippedProducts(Pageable pageable);

    List<ShippingItem> findByShippingId(Long shippingId);
}