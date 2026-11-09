package com.jcaa.usersmanagement.infrastructure.adapter.persistence.repository;

import com.jcaa.usersmanagement.application.port.out.DeleteEmisoraPort;
import com.jcaa.usersmanagement.application.port.out.GetAllEmisorasPort;
import com.jcaa.usersmanagement.application.port.out.GetEmisoraByIdPort;
import com.jcaa.usersmanagement.application.port.out.GetEmisoraByNombrePort;
import com.jcaa.usersmanagement.application.port.out.SaveEmisoraPort;
import com.jcaa.usersmanagement.application.port.out.UpdateEmisoraPort;
import com.jcaa.usersmanagement.domain.exception.EmisoraNotFoundException;
import com.jcaa.usersmanagement.domain.model.EmisoraModel;
import com.jcaa.usersmanagement.domain.valueobject.EmisoraId;
import com.jcaa.usersmanagement.domain.valueobject.EmisoraNombre;
import com.jcaa.usersmanagement.infrastructure.adapter.persistence.dto.EmisoraPersistenceDto;
import com.jcaa.usersmanagement.infrastructure.adapter.persistence.exception.EmisoraPersistenceException;
import com.jcaa.usersmanagement.infrastructure.adapter.persistence.mapper.EmisoraPersistenceMapper;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/** Adaptador de salida: implementa los puertos de persistencia de Emisora con JDBC + MySQL. */
@Repository
@RequiredArgsConstructor
public class EmisoraRepositoryMySQL
    implements SaveEmisoraPort,
        UpdateEmisoraPort,
        GetEmisoraByIdPort,
        GetEmisoraByNombrePort,
        GetAllEmisorasPort,
        DeleteEmisoraPort {

  private static final String COLUMNS =
      "id, nombre, canal, banda_fm, banda_am, num_locutores, genero, horario, patrocinador, "
          + "pais, descripcion, num_programas, num_ciudades, created_at, updated_at";

  private static final String SQL_INSERT =
      "INSERT INTO emisoras (id, nombre, canal, banda_fm, banda_am, num_locutores, genero, "
          + "horario, patrocinador, pais, descripcion, num_programas, num_ciudades, "
          + "created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW(), NOW())";

  private static final String SQL_UPDATE =
      "UPDATE emisoras SET nombre = ?, canal = ?, banda_fm = ?, banda_am = ?, num_locutores = ?, "
          + "genero = ?, horario = ?, patrocinador = ?, pais = ?, descripcion = ?, "
          + "num_programas = ?, num_ciudades = ?, updated_at = NOW() WHERE id = ?";

  private static final String SQL_SELECT_BY_ID =
      "SELECT " + COLUMNS + " FROM emisoras WHERE id = ? LIMIT 1";

  private static final String SQL_SELECT_BY_NOMBRE =
      "SELECT " + COLUMNS + " FROM emisoras WHERE LOWER(nombre) = LOWER(?) LIMIT 1";

  private static final String SQL_SELECT_ALL =
      "SELECT " + COLUMNS + " FROM emisoras ORDER BY nombre ASC";

  private static final String SQL_DELETE = "DELETE FROM emisoras WHERE id = ?";

  private final DataSource dataSource;

  @Override
  public EmisoraModel save(final EmisoraModel emisora) {
    final EmisoraPersistenceDto dto = EmisoraPersistenceMapper.fromModelToDto(emisora);
    try (final Connection connection = dataSource.getConnection();
        final PreparedStatement statement = connection.prepareStatement(SQL_INSERT)) {
      statement.setString(1, dto.id());
      bindFields(statement, dto, 2);
      statement.executeUpdate();
    } catch (final SQLException exception) {
      throw EmisoraPersistenceException.becauseSaveFailed(dto.id(), exception);
    }
    return findByIdOrFail(emisora.getId());
  }

  @Override
  public EmisoraModel update(final EmisoraModel emisora) {
    final EmisoraPersistenceDto dto = EmisoraPersistenceMapper.fromModelToDto(emisora);
    try (final Connection connection = dataSource.getConnection();
        final PreparedStatement statement = connection.prepareStatement(SQL_UPDATE)) {
      final int next = bindFields(statement, dto, 1);
      statement.setString(next, dto.id());
      statement.executeUpdate();
    } catch (final SQLException exception) {
      throw EmisoraPersistenceException.becauseUpdateFailed(dto.id(), exception);
    }
    return findByIdOrFail(emisora.getId());
  }

  @Override
  public Optional<EmisoraModel> getById(final EmisoraId emisoraId) {
    try (final Connection connection = dataSource.getConnection();
        final PreparedStatement statement = connection.prepareStatement(SQL_SELECT_BY_ID)) {
      statement.setString(1, emisoraId.value());
      return readOne(statement);
    } catch (final SQLException exception) {
      throw EmisoraPersistenceException.becauseFindByIdFailed(emisoraId.value(), exception);
    }
  }

  @Override
  public Optional<EmisoraModel> getByNombre(final EmisoraNombre nombre) {
    try (final Connection connection = dataSource.getConnection();
        final PreparedStatement statement = connection.prepareStatement(SQL_SELECT_BY_NOMBRE)) {
      statement.setString(1, nombre.value());
      return readOne(statement);
    } catch (final SQLException exception) {
      throw EmisoraPersistenceException.becauseFindByNombreFailed(nombre.value(), exception);
    }
  }

  @Override
  public List<EmisoraModel> getAll() {
    try (final Connection connection = dataSource.getConnection();
        final PreparedStatement statement = connection.prepareStatement(SQL_SELECT_ALL);
        final ResultSet resultSet = statement.executeQuery()) {
      return EmisoraPersistenceMapper.fromResultSetToModelList(resultSet);
    } catch (final SQLException exception) {
      throw EmisoraPersistenceException.becauseFindAllFailed(exception);
    }
  }

  @Override
  public void delete(final EmisoraId emisoraId) {
    try (final Connection connection = dataSource.getConnection();
        final PreparedStatement statement = connection.prepareStatement(SQL_DELETE)) {
      statement.setString(1, emisoraId.value());
      statement.executeUpdate();
    } catch (final SQLException exception) {
      throw EmisoraPersistenceException.becauseDeleteFailed(emisoraId.value(), exception);
    }
  }

  /**
   * Asigna los 12 atributos de negocio a partir de la posición indicada y devuelve la siguiente
   * posición libre. INSERT y UPDATE comparten el mismo orden de columnas.
   */
  private int bindFields(
      final PreparedStatement statement, final EmisoraPersistenceDto dto, final int start)
      throws SQLException {
    int i = start;
    statement.setString(i++, dto.nombre());
    statement.setString(i++, dto.canal());
    if (dto.bandaFm() == null) {
      statement.setNull(i++, Types.DECIMAL);
    } else {
      statement.setDouble(i++, dto.bandaFm());
    }
    if (dto.bandaAm() == null) {
      statement.setNull(i++, Types.INTEGER);
    } else {
      statement.setInt(i++, dto.bandaAm());
    }
    statement.setInt(i++, dto.numLocutores());
    statement.setString(i++, dto.genero());
    statement.setString(i++, dto.horario());
    statement.setString(i++, dto.patrocinador());
    statement.setString(i++, dto.pais());
    statement.setString(i++, dto.descripcion());
    statement.setInt(i++, dto.numProgramas());
    statement.setInt(i++, dto.numCiudades());
    return i;
  }

  private Optional<EmisoraModel> readOne(final PreparedStatement statement) throws SQLException {
    try (final ResultSet resultSet = statement.executeQuery()) {
      if (!resultSet.next()) {
        return Optional.empty();
      }
      return Optional.of(EmisoraPersistenceMapper.fromResultSetToModel(resultSet));
    }
  }

  private EmisoraModel findByIdOrFail(final EmisoraId emisoraId) {
    return getById(emisoraId)
        .orElseThrow(() -> EmisoraNotFoundException.becauseIdWasNotFound(emisoraId.value()));
  }
}
