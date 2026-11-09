package com.jcaa.usersmanagement.application.port.in;

import com.jcaa.usersmanagement.application.service.dto.query.GetEmisoraByIdQuery;
import com.jcaa.usersmanagement.domain.model.EmisoraModel;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public interface GetEmisoraByIdUseCase {
  EmisoraModel execute(@NotNull @Valid GetEmisoraByIdQuery query);
}
