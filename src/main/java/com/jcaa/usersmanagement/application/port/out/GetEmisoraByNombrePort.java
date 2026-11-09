package com.jcaa.usersmanagement.application.port.out;

import com.jcaa.usersmanagement.domain.model.EmisoraModel;
import com.jcaa.usersmanagement.domain.valueobject.EmisoraNombre;
import java.util.Optional;

public interface GetEmisoraByNombrePort {
  Optional<EmisoraModel> getByNombre(EmisoraNombre nombre);
}
