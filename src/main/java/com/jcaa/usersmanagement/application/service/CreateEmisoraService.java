package com.jcaa.usersmanagement.application.service;

import com.jcaa.usersmanagement.application.port.in.CreateEmisoraUseCase;
import com.jcaa.usersmanagement.application.port.out.GetEmisoraByIdPort;
import com.jcaa.usersmanagement.application.port.out.GetEmisoraByNombrePort;
import com.jcaa.usersmanagement.application.port.out.SaveEmisoraPort;
import com.jcaa.usersmanagement.application.service.dto.command.CreateEmisoraCommand;
import com.jcaa.usersmanagement.application.service.mapper.EmisoraApplicationMapper;
import com.jcaa.usersmanagement.domain.exception.EmisoraAlreadyExistsException;
import com.jcaa.usersmanagement.domain.model.EmisoraModel;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/** Caso de uso: registrar una emisora. El id y el nombre no pueden repetirse. */
@Service
@RequiredArgsConstructor
public class CreateEmisoraService implements CreateEmisoraUseCase {

  private final SaveEmisoraPort saveEmisoraPort;
  private final GetEmisoraByIdPort getEmisoraByIdPort;
  private final GetEmisoraByNombrePort getEmisoraByNombrePort;
  private final Validator validator;

  @Override
  public EmisoraModel execute(final CreateEmisoraCommand command) {
    validateCommand(command);

    final EmisoraModel emisoraToSave = EmisoraApplicationMapper.fromCreateCommandToModel(command);
    ensureIdIsNotTaken(emisoraToSave);
    ensureNombreIsNotTaken(emisoraToSave);

    return saveEmisoraPort.save(emisoraToSave);
  }

  private void validateCommand(final CreateEmisoraCommand command) {
    final Set<ConstraintViolation<CreateEmisoraCommand>> violations = validator.validate(command);
    if (!violations.isEmpty()) {
      throw new ConstraintViolationException(violations);
    }
  }

  private void ensureIdIsNotTaken(final EmisoraModel emisora) {
    getEmisoraByIdPort
        .getById(emisora.getId())
        .ifPresent(
            ignored -> {
              throw EmisoraAlreadyExistsException.becauseIdAlreadyExists(emisora.getId().value());
            });
  }

  private void ensureNombreIsNotTaken(final EmisoraModel emisora) {
    getEmisoraByNombrePort
        .getByNombre(emisora.getNombre())
        .ifPresent(
            ignored -> {
              throw EmisoraAlreadyExistsException.becauseNombreAlreadyExists(
                  emisora.getNombre().value());
            });
  }
}
