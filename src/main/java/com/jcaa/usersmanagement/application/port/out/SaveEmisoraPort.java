package com.jcaa.usersmanagement.application.port.out;

import com.jcaa.usersmanagement.domain.model.EmisoraModel;

public interface SaveEmisoraPort {
  EmisoraModel save(EmisoraModel emisora);
}
