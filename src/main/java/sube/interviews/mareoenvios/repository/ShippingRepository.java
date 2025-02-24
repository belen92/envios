package sube.interviews.mareoenvios.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sube.interviews.mareoenvios.entity.Shipping;
import sube.interviews.mareoenvios.entity.State;

import java.time.LocalDate;
import java.util.List;

public interface ShippingRepository extends JpaRepository<Shipping, Long> {

    List<Shipping> findBySendDateBetween(LocalDate startDate, LocalDate endDate);
    List<Shipping> findByState(State state);


}