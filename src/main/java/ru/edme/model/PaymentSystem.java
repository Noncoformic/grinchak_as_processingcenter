package ru.edme.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder(toBuilder = true) // Позволяет удобно использовать паттерн Builder
@Table(name = "payment_system")
public class PaymentSystem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "payment_system_name", nullable = false, unique = true, length = 50)
    private String paymentSystemName;


}
