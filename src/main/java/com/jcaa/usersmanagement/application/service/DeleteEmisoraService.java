package com.jcaa.usersmanagement.application.service;

import com.jcaa.usersmanagement.application.port.in.DeleteEmisoraUseCase;
import com.jcaa.usersmanagement.application.port.out.DeleteEmisoraPort;
import com.jcaa.usersmanagement.application.port.out.GetEmisoraByIdPort;
import com.jcaa.usersmanagement.application.service.dto.command.DeleteEmisoraCommand;
import com.jcaa.usersmanagement.application.service.mapper.EmisoraApplicationMapper;
import com.jcaa.usersmanagement.domain.exception.EmisoraNotFoundException;
import com.jcaa.usersmanagement.domain.valueobject.EmisoraId;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/** Caso de uso: eliminar una emisora. Falla con 404 si no existe. */
@Service
@RequiredArgsConstructor
public class DeleteEmisoraService implements DeleteEmisoraUseCase {

  private final DeleteEmisoraPort deleteEmisoraPort;
  private final GetEmisoraByIdPort getEmisoraByIdPort;
  private final Validator validator;

  @Override
  public void execute(final DeleteEmisoraCommand command) {
    final Set<ConstraintViolation<DeleteEmisoraCommand>> violations = validator.validate(command);
    if (!violations.isEmpty()) {
      throw new ConstraintViolationException(violations);
    }

    final EmisoraId emisoraId = EmisoraApplicationMapper.fromDeleteCommandToEmisoraId(command);
    getEmisoraByIdPort
        .getById(emisoraId)
        .orElseThrow(() -> EmisoraNotFoundException.becauseIdWasNotFound(emisoraId.value()));
    deleteEmisoraPort.delete(emisoraId);
  }
}
