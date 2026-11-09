package com.jcaa.usersmanagement.application.port.in;

import com.jcaa.usersmanagement.application.service.dto.command.DeleteEmisoraCommand;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public interface DeleteEmisoraUseCase {
  void execute(@NotNull @Valid DeleteEmisoraCommand command);
}
