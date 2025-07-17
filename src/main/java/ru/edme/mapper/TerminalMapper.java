package ru.edme.mapper;

import org.mapstruct.Mapper;
import ru.edme.dto.TerminalDto;
import ru.edme.model.Terminal;

@Mapper(componentModel = "spring")
public interface TerminalMapper {
  
    TerminalDto toDto(Terminal e);

  
    Terminal toEntity(TerminalDto d);
}
