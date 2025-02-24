package sube.interviews.mareoenvios.entity;


import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;


@Entity
@Table(name = "shipping")
@Getter
@Setter
public class Shipping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private State state;

    @Column(name = "send_date", nullable = false)
    private LocalDate sendDate;

    @Column(name = "arrive_date")
    private LocalDate arriveDate;

    @Column(nullable = false)
    private int priority;


}

