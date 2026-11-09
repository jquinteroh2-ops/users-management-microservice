package com.jcaa.usersmanagement.application.service;

import com.jcaa.usersmanagement.application.port.in.GetAllEmisorasUseCase;
import com.jcaa.usersmanagement.application.port.out.GetAllEmisorasPort;
import com.jcaa.usersmanagement.domain.model.EmisoraModel;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/** Consulta: listar todas las emisoras ordenadas por nombre. */
@Service
@RequiredArgsConstructor
public class GetAllEmisorasService implements GetAllEmisorasUseCase {

  private final GetAllEmisorasPort getAllEmisorasPort;

  @Override
  public List<EmisoraModel> execute() {
    return getAllEmisorasPort.getAll();
  }
}
