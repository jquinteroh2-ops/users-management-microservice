package com.jcaa.usersmanagement.infrastructure.entrypoint.rest.controller;

import com.jcaa.usersmanagement.infrastructure.config.OpenApiConfig;
import com.jcaa.usersmanagement.infrastructure.entrypoint.rest.dto.request.CreateEmisoraRestRequest;
import com.jcaa.usersmanagement.infrastructure.entrypoint.rest.dto.request.UpdateEmisoraRestRequest;
import com.jcaa.usersmanagement.infrastructure.entrypoint.rest.dto.response.ApiErrorResponse;
import com.jcaa.usersmanagement.infrastructure.entrypoint.rest.dto.response.EmisoraRestResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

/** Contrato OpenAPI / Swagger de {@link EmisoraRestController}. */
@Tag(name = "Emisoras", description = "CRUDL de emisoras de radio (ejercicio 25).")
@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH)
public interface EmisoraRestControllerDocs {

  @Operation(
      summary = "Crear emisora",
      description =
          "Registra una emisora. El id y el nombre deben ser únicos y debe transmitir al menos "
              + "en FM (87.5-108.0 MHz) o en AM (530-1710 kHz). Requiere rol ADMIN.")
  @ApiResponses({
    @ApiResponse(
        responseCode = "201",
        description = "Emisora creada.",
        content = @Content(schema = @Schema(implementation = EmisoraRestResponse.class))),
    @ApiResponse(
        responseCode = "400",
        description = "Datos inválidos.",
        content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
    @ApiResponse(responseCode = "401", description = "Falta el token JWT."),
    @ApiResponse(responseCode = "403", description = "El usuario no es ADMIN."),
    @ApiResponse(
        responseCode = "409",
        description = "Ya existe una emisora con ese id o nombre.",
        content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
  })
  EmisoraRestResponse create(@Valid @RequestBody CreateEmisoraRestRequest request);

  @Operation(
      summary = "Listar emisoras",
      description = "Devuelve todas las emisoras ordenadas por nombre. Cualquier usuario autenticado.")
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Lista de emisoras (puede ser vacía).",
        content =
            @Content(
                array = @ArraySchema(schema = @Schema(implementation = EmisoraRestResponse.class)))),
    @ApiResponse(responseCode = "401", description = "Falta el token JWT.")
  })
  @SuppressWarnings("unused")
  List<EmisoraRestResponse> getAll();

  @Operation(summary = "Obtener emisora por id")
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Emisora encontrada.",
        content = @Content(schema = @Schema(implementation = EmisoraRestResponse.class))),
    @ApiResponse(
        responseCode = "404",
        description = "No existe una emisora con ese id.",
        content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
  })
  EmisoraRestResponse getById(
      @Parameter(description = "Id de la emisora.", example = "emi-001", required = true)
          @PathVariable
          String id);

  @Operation(summary = "Actualizar emisora", description = "Requiere rol ADMIN.")
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Emisora actualizada.",
        content = @Content(schema = @Schema(implementation = EmisoraRestResponse.class))),
    @ApiResponse(
        responseCode = "400",
        description = "Datos inválidos.",
        content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
    @ApiResponse(
        responseCode = "404",
        description = "No existe una emisora con ese id.",
        content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
    @ApiResponse(
        responseCode = "409",
        description = "El nombre ya lo usa otra emisora.",
        content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
  })
  EmisoraRestResponse update(
      @Parameter(description = "Id de la emisora.", example = "emi-001", required = true)
          @PathVariable
          String id,
      @Valid @RequestBody UpdateEmisoraRestRequest request);

  @Operation(summary = "Eliminar emisora", description = "Requiere rol ADMIN.")
  @ApiResponses({
    @ApiResponse(responseCode = "204", description = "Emisora eliminada."),
    @ApiResponse(
        responseCode = "404",
        description = "No existe una emisora con ese id.",
        content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
  })
  void delete(
      @Parameter(description = "Id de la emisora.", example = "emi-001", required = true)
          @PathVariable
          String id);
}
