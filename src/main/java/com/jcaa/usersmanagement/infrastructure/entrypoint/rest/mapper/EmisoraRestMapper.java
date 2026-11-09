package com.jcaa.usersmanagement.infrastructure.entrypoint.rest.mapper;

import com.jcaa.usersmanagement.application.service.dto.command.CreateEmisoraCommand;
import com.jcaa.usersmanagement.application.service.dto.command.DeleteEmisoraCommand;
import com.jcaa.usersmanagement.application.service.dto.command.UpdateEmisoraCommand;
import com.jcaa.usersmanagement.application.service.dto.query.GetEmisoraByIdQuery;
import com.jcaa.usersmanagement.domain.model.EmisoraModel;
import com.jcaa.usersmanagement.infrastructure.entrypoint.rest.dto.request.CreateEmisoraRestRequest;
import com.jcaa.usersmanagement.infrastructure.entrypoint.rest.dto.request.UpdateEmisoraRestRequest;
import com.jcaa.usersmanagement.infrastructure.entrypoint.rest.dto.response.EmisoraRestResponse;
import java.util.List;
import lombok.experimental.UtilityClass;

/** Convierte peticiones HTTP en comandos/consultas y modelos en respuestas JSON. */
@UtilityClass
public class EmisoraRestMapper {

  public CreateEmisoraCommand toCreateCommand(final CreateEmisoraRestRequest request) {
    return new CreateEmisoraCommand(
        request.id(),
        request.nombre(),
        request.canal(),
        request.bandaFm(),
        request.bandaAm(),
        request.numLocutores(),
        request.genero(),
        request.horario(),
        request.patrocinador(),
        request.pais(),
        request.descripcion(),
        request.numProgramas(),
        request.numCiudades());
  }

  public UpdateEmisoraCommand toUpdateCommand(
      final String id, final UpdateEmisoraRestRequest request) {
    return new UpdateEmisoraCommand(
        id,
        request.nombre(),
        request.canal(),
        request.bandaFm(),
        request.bandaAm(),
        request.numLocutores(),
        request.genero(),
        request.horario(),
        request.patrocinador(),
        request.pais(),
        request.descripcion(),
        request.numProgramas(),
        request.numCiudades());
  }

  public GetEmisoraByIdQuery toGetByIdQuery(final String id) {
    return new GetEmisoraByIdQuery(id);
  }

  public DeleteEmisoraCommand toDeleteCommand(final String id) {
    return new DeleteEmisoraCommand(id);
  }

  public EmisoraRestResponse toResponse(final EmisoraModel emisora) {
    return new EmisoraRestResponse(
        emisora.getId().value(),
        emisora.getNombre().value(),
        emisora.getCanal(),
        emisora.transmiteEnFm() ? emisora.getBandaFm().megahertz() : null,
        emisora.transmiteEnAm() ? emisora.getBandaAm().kilohertz() : null,
        emisora.getNumLocutores(),
        emisora.getGenero().name(),
        emisora.getHorario(),
        emisora.getPatrocinador(),
        emisora.getPais(),
        emisora.getDescripcion(),
        emisora.getNumProgramas(),
        emisora.getNumCiudades());
  }

  public List<EmisoraRestResponse> toResponseList(final List<EmisoraModel> emisoras) {
    return emisoras.stream().map(EmisoraRestMapper::toResponse).toList();
  }
}
