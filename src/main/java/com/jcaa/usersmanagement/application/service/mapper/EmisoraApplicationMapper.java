package com.jcaa.usersmanagement.application.service.mapper;

import com.jcaa.usersmanagement.application.service.dto.command.CreateEmisoraCommand;
import com.jcaa.usersmanagement.application.service.dto.command.DeleteEmisoraCommand;
import com.jcaa.usersmanagement.application.service.dto.command.UpdateEmisoraCommand;
import com.jcaa.usersmanagement.application.service.dto.query.GetEmisoraByIdQuery;
import com.jcaa.usersmanagement.domain.enums.GeneroEmisora;
import com.jcaa.usersmanagement.domain.model.EmisoraModel;
import com.jcaa.usersmanagement.domain.valueobject.BandaAm;
import com.jcaa.usersmanagement.domain.valueobject.BandaFm;
import com.jcaa.usersmanagement.domain.valueobject.EmisoraId;
import com.jcaa.usersmanagement.domain.valueobject.EmisoraNombre;
import lombok.experimental.UtilityClass;

/** Convierte comandos y consultas de la capa de aplicación en objetos del dominio. */
@UtilityClass
public class EmisoraApplicationMapper {

  public EmisoraModel fromCreateCommandToModel(final CreateEmisoraCommand command) {
    return new EmisoraModel(
        new EmisoraId(command.id()),
        new EmisoraNombre(command.nombre()),
        command.canal(),
        BandaFm.ofNullable(command.bandaFm()),
        BandaAm.ofNullable(command.bandaAm()),
        command.numLocutores(),
        GeneroEmisora.fromString(command.genero()),
        command.horario(),
        command.patrocinador(),
        command.pais(),
        command.descripcion(),
        command.numProgramas(),
        command.numCiudades());
  }

  public EmisoraModel fromUpdateCommandToModel(final UpdateEmisoraCommand command) {
    return new EmisoraModel(
        new EmisoraId(command.id()),
        new EmisoraNombre(command.nombre()),
        command.canal(),
        BandaFm.ofNullable(command.bandaFm()),
        BandaAm.ofNullable(command.bandaAm()),
        command.numLocutores(),
        GeneroEmisora.fromString(command.genero()),
        command.horario(),
        command.patrocinador(),
        command.pais(),
        command.descripcion(),
        command.numProgramas(),
        command.numCiudades());
  }

  public EmisoraId fromGetByIdQueryToEmisoraId(final GetEmisoraByIdQuery query) {
    return new EmisoraId(query.id());
  }

  public EmisoraId fromDeleteCommandToEmisoraId(final DeleteEmisoraCommand command) {
    return new EmisoraId(command.id());
  }
}
