package com.jcaa.usersmanagement.application.service;

import com.jcaa.usersmanagement.application.port.in.GetEmisoraByIdUseCase;
import com.jcaa.usersmanagement.application.port.out.GetEmisoraByIdPort;
import com.jcaa.usersmanagement.application.service.dto.query.GetEmisoraByIdQuery;
import com.jcaa.usersmanagement.application.service.mapper.EmisoraApplicationMapper;
import com.jcaa.usersmanagement.domain.exception.EmisoraNotFoundException;
import com.jcaa.usersmanagement.domain.model.EmisoraModel;
import com.jcaa.usersmanagement.domain.valueobject.EmisoraId;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/** Consulta: obtener una emisora por id. */
@Service
@RequiredArgsConstructor
public class GetEmisoraByIdService implements GetEmisoraByIdUseCase {

  private final GetEmisoraByIdPort getEmisoraByIdPort;
  private final Validator validator;

  @Override
  public EmisoraModel execute(final GetEmisoraByIdQuery query) {
    final Set<ConstraintViolation<GetEmisoraByIdQuery>> violations = validator.validate(query);
    if (!violations.isEmpty()) {
      throw new ConstraintViolationException(violations);
    }

    final EmisoraId emisoraId = EmisoraApplicationMapper.fromGetByIdQueryToEmisoraId(query);
    return getEmisoraByIdPort
        .getById(emisoraId)
        .orElseThrow(() -> EmisoraNotFoundException.becauseIdWasNotFound(emisoraId.value()));
  }
}
