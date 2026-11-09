package com.jcaa.usersmanagement.application.port.out;

import com.jcaa.usersmanagement.domain.model.EmisoraModel;
import java.util.List;

public interface GetAllEmisorasPort {
  List<EmisoraModel> getAll();
}
