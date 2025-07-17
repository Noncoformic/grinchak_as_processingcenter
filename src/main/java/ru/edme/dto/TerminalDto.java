package ru.edme.dto;

import lombok.Data;

@Data
public class TerminalDto {
    private Long id;
    private String terminalId;
    private Long mccId;
    private Long posId;
}
