package com.jcaa.usersmanagement.application.port.out;

import com.jcaa.usersmanagement.domain.valueobject.EmisoraId;

public interface DeleteEmisoraPort {
  void delete(EmisoraId emisoraId);
}
