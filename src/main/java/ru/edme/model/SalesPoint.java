package ru.edme.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalesPoint {
    private Long id;
    private String posName;
    private String posAddress;
    private String posInn;
    private Long acquiringBankId;
}