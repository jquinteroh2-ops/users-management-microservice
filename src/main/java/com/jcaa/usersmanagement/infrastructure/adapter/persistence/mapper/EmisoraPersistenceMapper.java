package com.jcaa.usersmanagement.infrastructure.adapter.persistence.mapper;

import com.jcaa.usersmanagement.domain.enums.GeneroEmisora;
import com.jcaa.usersmanagement.domain.model.EmisoraModel;
import com.jcaa.usersmanagement.domain.valueobject.BandaAm;
import com.jcaa.usersmanagement.domain.valueobject.BandaFm;
import com.jcaa.usersmanagement.domain.valueobject.EmisoraId;
import com.jcaa.usersmanagement.domain.valueobject.EmisoraNombre;
import com.jcaa.usersmanagement.infrastructure.adapter.persistence.dto.EmisoraPersistenceDto;
import com.jcaa.usersmanagement.infrastructure.adapter.persistence.entity.EmisoraEntity;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import lombok.experimental.UtilityClass;

/** Traduce entre el modelo de dominio y las filas de la tabla "emisoras". */
@UtilityClass
public class EmisoraPersistenceMapper {

  public EmisoraPersistenceDto fromModelToDto(final EmisoraModel emisora) {
    return new EmisoraPersistenceDto(
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

  public EmisoraEntity fromResultSetToEntity(final ResultSet resultSet) throws SQLException {
    final BigDecimal fm = resultSet.getBigDecimal("banda_fm");
    final int am = resultSet.getInt("banda_am");
    final boolean amIsNull = resultSet.wasNull();
    return new EmisoraEntity(
        resultSet.getString("id"),
        resultSet.getString("nombre"),
        resultSet.getString("canal"),
        fm == null ? null : fm.doubleValue(),
        amIsNull ? null : am,
        resultSet.getInt("num_locutores"),
        resultSet.getString("genero"),
        resultSet.getString("horario"),
        resultSet.getString("patrocinador"),
        resultSet.getString("pais"),
        resultSet.getString("descripcion"),
        resultSet.getInt("num_programas"),
        resultSet.getInt("num_ciudades"),
        resultSet.getString("created_at"),
        resultSet.getString("updated_at"));
  }

  public EmisoraModel fromEntityToModel(final EmisoraEntity entity) {
    return new EmisoraModel(
        new EmisoraId(entity.id()),
        new EmisoraNombre(entity.nombre()),
        entity.canal(),
        BandaFm.ofNullable(entity.bandaFm()),
        BandaAm.ofNullable(entity.bandaAm()),
        entity.numLocutores(),
        GeneroEmisora.fromString(entity.genero()),
        entity.horario(),
        entity.patrocinador(),
        entity.pais(),
        entity.descripcion(),
        entity.numProgramas(),
        entity.numCiudades());
  }

  public EmisoraModel fromResultSetToModel(final ResultSet resultSet) throws SQLException {
    return fromEntityToModel(fromResultSetToEntity(resultSet));
  }

  public List<EmisoraModel> fromResultSetToModelList(final ResultSet resultSet)
      throws SQLException {
    final List<EmisoraModel> emisoras = new ArrayList<>();
    while (resultSet.next()) {
      emisoras.add(fromResultSetToModel(resultSet));
    }
    return emisoras;
  }
}
