package ru.edme.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "response_code")
public class ResponseCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "error_code", nullable = false, unique = true, length = 10)
    private String errorCode;

    @Column(name = "error_description", length = 255)
    private String errorDescription;

    @Column(name = "error_level", length = 50)
    private String errorLevel;
}
