package com.jcaa.usersmanagement.application.service;

import com.jcaa.usersmanagement.application.port.in.UpdateEmisoraUseCase;
import com.jcaa.usersmanagement.application.port.out.GetEmisoraByIdPort;
import com.jcaa.usersmanagement.application.port.out.GetEmisoraByNombrePort;
import com.jcaa.usersmanagement.application.port.out.UpdateEmisoraPort;
import com.jcaa.usersmanagement.application.service.dto.command.UpdateEmisoraCommand;
import com.jcaa.usersmanagement.application.service.mapper.EmisoraApplicationMapper;
import com.jcaa.usersmanagement.domain.exception.EmisoraAlreadyExistsException;
import com.jcaa.usersmanagement.domain.exception.EmisoraNotFoundException;
import com.jcaa.usersmanagement.domain.model.EmisoraModel;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/** Caso de uso: actualizar una emisora existente sin repetir el nombre de otra. */
@Service
@RequiredArgsConstructor
public class UpdateEmisoraService implements UpdateEmisoraUseCase {

  private final UpdateEmisoraPort updateEmisoraPort;
  private final GetEmisoraByIdPort getEmisoraByIdPort;
  private final GetEmisoraByNombrePort getEmisoraByNombrePort;
  private final Validator validator;

  @Override
  public EmisoraModel execute(final UpdateEmisoraCommand command) {
    validateCommand(command);

    final EmisoraModel emisoraToUpdate = EmisoraApplicationMapper.fromUpdateCommandToModel(command);
    ensureEmisoraExists(emisoraToUpdate);
    ensureNombreIsNotTakenByAnother(emisoraToUpdate);

    return updateEmisoraPort.update(emisoraToUpdate);
  }

  private void validateCommand(final UpdateEmisoraCommand command) {
    final Set<ConstraintViolation<UpdateEmisoraCommand>> violations = validator.validate(command);
    if (!violations.isEmpty()) {
      throw new ConstraintViolationException(violations);
    }
  }

  private void ensureEmisoraExists(final EmisoraModel emisora) {
    getEmisoraByIdPort
        .getById(emisora.getId())
        .orElseThrow(() -> EmisoraNotFoundException.becauseIdWasNotFound(emisora.getId().value()));
  }

  private void ensureNombreIsNotTakenByAnother(final EmisoraModel emisora) {
    getEmisoraByNombrePort
        .getByNombre(emisora.getNombre())
        .ifPresent(
            found -> {
              if (!found.getId().equals(emisora.getId())) {
                throw EmisoraAlreadyExistsException.becauseNombreAlreadyExists(
                    emisora.getNombre().value());
              }
            });
  }
}
