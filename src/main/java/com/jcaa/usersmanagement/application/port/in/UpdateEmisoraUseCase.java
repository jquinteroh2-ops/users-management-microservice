package com.jcaa.usersmanagement.application.port.in;

import com.jcaa.usersmanagement.application.service.dto.command.UpdateEmisoraCommand;
import com.jcaa.usersmanagement.domain.model.EmisoraModel;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public interface UpdateEmisoraUseCase {
  EmisoraModel execute(@NotNull @Valid UpdateEmisoraCommand command);
}
