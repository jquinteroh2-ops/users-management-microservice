package com.jcaa.usersmanagement.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.jcaa.usersmanagement.application.port.out.DeleteEmisoraPort;
import com.jcaa.usersmanagement.application.port.out.GetAllEmisorasPort;
import com.jcaa.usersmanagement.application.port.out.GetEmisoraByIdPort;
import com.jcaa.usersmanagement.application.port.out.GetEmisoraByNombrePort;
import com.jcaa.usersmanagement.application.port.out.SaveEmisoraPort;
import com.jcaa.usersmanagement.application.port.out.UpdateEmisoraPort;
import com.jcaa.usersmanagement.application.service.dto.command.CreateEmisoraCommand;
import com.jcaa.usersmanagement.application.service.dto.command.DeleteEmisoraCommand;
import com.jcaa.usersmanagement.application.service.dto.command.UpdateEmisoraCommand;
import com.jcaa.usersmanagement.application.service.dto.query.GetEmisoraByIdQuery;
import com.jcaa.usersmanagement.application.service.mapper.EmisoraApplicationMapper;
import com.jcaa.usersmanagement.domain.exception.EmisoraAlreadyExistsException;
import com.jcaa.usersmanagement.domain.exception.EmisoraNotFoundException;
import com.jcaa.usersmanagement.domain.model.EmisoraModel;
import com.jcaa.usersmanagement.domain.valueobject.EmisoraId;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("Casos de uso de Emisora")
@ExtendWith(MockitoExtension.class)
class EmisoraServicesTest {

  @Mock private SaveEmisoraPort savePort;
  @Mock private UpdateEmisoraPort updatePort;
  @Mock private DeleteEmisoraPort deletePort;
  @Mock private GetEmisoraByIdPort getByIdPort;
  @Mock private GetEmisoraByNombrePort getByNombrePort;
  @Mock private GetAllEmisorasPort getAllPort;

  private Validator validator;

  @BeforeEach
  void setUp() {
    try (final ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
      validator = factory.getValidator();
    }
  }

  private static CreateEmisoraCommand createCommand(final String id, final String nombre) {
    return new CreateEmisoraCommand(
        id, nombre, "HJKC", 95.5, null, 6, "MUSICAL", "24 horas", "Patrocinador", "Colombia",
        "Descripción", 12, 3);
  }

  private static UpdateEmisoraCommand updateCommand(final String id, final String nombre) {
    return new UpdateEmisoraCommand(
        id, nombre, "HJKC", null, 1170, 8, "NOTICIAS", "05:00 - 23:00", "", "Colombia", "", 15, 4);
  }

  private static EmisoraModel model(final String id, final String nombre) {
    return EmisoraApplicationMapper.fromCreateCommandToModel(createCommand(id, nombre));
  }

  // ---------------------------------------------------------------- crear

  @Test
  @DisplayName("Crear guarda la emisora cuando id y nombre están libres")
  void createShouldSave() {
    final CreateEmisoraService service =
        new CreateEmisoraService(savePort, getByIdPort, getByNombrePort, validator);
    when(getByIdPort.getById(any())).thenReturn(Optional.empty());
    when(getByNombrePort.getByNombre(any())).thenReturn(Optional.empty());
    when(savePort.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

    final EmisoraModel result = service.execute(createCommand("emi-010", "Radio Nueva"));

    assertEquals("emi-010", result.getId().value());
    verify(savePort).save(any());
  }

  @Test
  @DisplayName("Crear falla con 409 si el nombre ya existe")
  void createShouldRejectDuplicatedNombre() {
    final CreateEmisoraService service =
        new CreateEmisoraService(savePort, getByIdPort, getByNombrePort, validator);
    when(getByIdPort.getById(any())).thenReturn(Optional.empty());
    when(getByNombrePort.getByNombre(any()))
        .thenReturn(Optional.of(model("emi-001", "Radio Nueva")));

    assertThrows(
        EmisoraAlreadyExistsException.class,
        () -> service.execute(createCommand("emi-010", "Radio Nueva")));
    verify(savePort, never()).save(any());
  }

  @Test
  @DisplayName("Crear valida el comando (nombre vacío)")
  void createShouldValidateCommand() {
    final CreateEmisoraService service =
        new CreateEmisoraService(savePort, getByIdPort, getByNombrePort, validator);

    assertThrows(
        ConstraintViolationException.class, () -> service.execute(createCommand("emi-010", "")));
  }

  // ----------------------------------------------------------- actualizar

  @Test
  @DisplayName("Actualizar permite conservar el mismo nombre de la propia emisora")
  void updateShouldAllowSameNombre() {
    final UpdateEmisoraService service =
        new UpdateEmisoraService(updatePort, getByIdPort, getByNombrePort, validator);
    when(getByIdPort.getById(any())).thenReturn(Optional.of(model("emi-001", "Radio Uno")));
    when(getByNombrePort.getByNombre(any())).thenReturn(Optional.of(model("emi-001", "Radio Uno")));
    when(updatePort.update(any())).thenAnswer(invocation -> invocation.getArgument(0));

    final EmisoraModel result = service.execute(updateCommand("emi-001", "Radio Uno"));

    assertTrue(result.transmiteEnAm());
    assertEquals(1170, result.getBandaAm().kilohertz());
  }

  @Test
  @DisplayName("Actualizar falla con 404 si la emisora no existe")
  void updateShouldFailWhenNotFound() {
    final UpdateEmisoraService service =
        new UpdateEmisoraService(updatePort, getByIdPort, getByNombrePort, validator);
    when(getByIdPort.getById(any())).thenReturn(Optional.empty());

    assertThrows(
        EmisoraNotFoundException.class, () -> service.execute(updateCommand("emi-999", "Radio X")));
  }

  // --------------------------------------------------- eliminar y consultas

  @Test
  @DisplayName("Eliminar invoca el puerto cuando la emisora existe")
  void deleteShouldCallPort() {
    final DeleteEmisoraService service =
        new DeleteEmisoraService(deletePort, getByIdPort, validator);
    when(getByIdPort.getById(any())).thenReturn(Optional.of(model("emi-001", "Radio Uno")));

    service.execute(new DeleteEmisoraCommand("emi-001"));

    verify(deletePort).delete(new EmisoraId("emi-001"));
  }

  @Test
  @DisplayName("Consultar por id inexistente lanza EmisoraNotFoundException")
  void getByIdShouldFailWhenMissing() {
    final GetEmisoraByIdService service = new GetEmisoraByIdService(getByIdPort, validator);
    when(getByIdPort.getById(any())).thenReturn(Optional.empty());

    assertThrows(
        EmisoraNotFoundException.class, () -> service.execute(new GetEmisoraByIdQuery("nope")));
  }

  @Test
  @DisplayName("Listar devuelve lo que entrega el puerto")
  void getAllShouldReturnList() {
    final GetAllEmisorasService service = new GetAllEmisorasService(getAllPort);
    when(getAllPort.getAll()).thenReturn(List.of(model("emi-001", "Radio Uno")));

    assertEquals(1, service.execute().size());
  }
}
