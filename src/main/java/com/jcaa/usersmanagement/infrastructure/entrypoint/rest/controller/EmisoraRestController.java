package com.jcaa.usersmanagement.infrastructure.entrypoint.rest.controller;

import com.jcaa.usersmanagement.application.port.in.CreateEmisoraUseCase;
import com.jcaa.usersmanagement.application.port.in.DeleteEmisoraUseCase;
import com.jcaa.usersmanagement.application.port.in.GetAllEmisorasUseCase;
import com.jcaa.usersmanagement.application.port.in.GetEmisoraByIdUseCase;
import com.jcaa.usersmanagement.application.port.in.UpdateEmisoraUseCase;
import com.jcaa.usersmanagement.infrastructure.entrypoint.rest.dto.request.CreateEmisoraRestRequest;
import com.jcaa.usersmanagement.infrastructure.entrypoint.rest.dto.request.UpdateEmisoraRestRequest;
import com.jcaa.usersmanagement.infrastructure.entrypoint.rest.dto.response.EmisoraRestResponse;
import com.jcaa.usersmanagement.infrastructure.entrypoint.rest.mapper.EmisoraRestMapper;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/** Entrypoint REST del CRUDL de emisoras. Solo traduce HTTP y delega en los casos de uso. */
@RestController
@RequestMapping("/api/emisoras")
@RequiredArgsConstructor
public class EmisoraRestController implements EmisoraRestControllerDocs {

  private final CreateEmisoraUseCase createEmisoraUseCase;
  private final UpdateEmisoraUseCase updateEmisoraUseCase;
  private final DeleteEmisoraUseCase deleteEmisoraUseCase;
  private final GetEmisoraByIdUseCase getEmisoraByIdUseCase;
  private final GetAllEmisorasUseCase getAllEmisorasUseCase;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public EmisoraRestResponse create(@Valid @RequestBody final CreateEmisoraRestRequest request) {
    return EmisoraRestMapper.toResponse(
        createEmisoraUseCase.execute(EmisoraRestMapper.toCreateCommand(request)));
  }

  @GetMapping
  public List<EmisoraRestResponse> getAll() {
    return EmisoraRestMapper.toResponseList(getAllEmisorasUseCase.execute());
  }

  @GetMapping("/{id}")
  public EmisoraRestResponse getById(@PathVariable final String id) {
    return EmisoraRestMapper.toResponse(
        getEmisoraByIdUseCase.execute(EmisoraRestMapper.toGetByIdQuery(id)));
  }

  @PutMapping("/{id}")
  public EmisoraRestResponse update(
      @PathVariable final String id, @Valid @RequestBody final UpdateEmisoraRestRequest request) {
    return EmisoraRestMapper.toResponse(
        updateEmisoraUseCase.execute(EmisoraRestMapper.toUpdateCommand(id, request)));
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable final String id) {
    deleteEmisoraUseCase.execute(EmisoraRestMapper.toDeleteCommand(id));
  }
}
