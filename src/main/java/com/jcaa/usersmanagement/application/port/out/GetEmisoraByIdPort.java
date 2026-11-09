package com.jcaa.usersmanagement.application.port.out;

import com.jcaa.usersmanagement.domain.model.EmisoraModel;
import com.jcaa.usersmanagement.domain.valueobject.EmisoraId;
import java.util.Optional;

public interface GetEmisoraByIdPort {
  Optional<EmisoraModel> getById(EmisoraId emisoraId);
}
