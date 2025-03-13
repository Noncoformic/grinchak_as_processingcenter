package ru.edme.model;

import lombok.*;

@Data
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AcquiringBank {
    private Long id;
    private String bic;
    private String abbreviatedName;
}
