package com.jcaa.usersmanagement.infrastructure.adapter.persistence.exception;

/** Error técnico al leer o escribir emisoras en la base de datos. */
public final class EmisoraPersistenceException extends RuntimeException {

  private static final String MESSAGE_SAVE = "Failed to save emisora with ID: '%s'.";
  private static final String MESSAGE_UPDATE = "Failed to update emisora with ID: '%s'.";
  private static final String MESSAGE_FIND = "Failed to find emisora with ID: '%s'.";
  private static final String MESSAGE_NOMBRE = "Failed to find emisora with name: '%s'.";
  private static final String MESSAGE_ALL = "Failed to retrieve all emisoras.";
  private static final String MESSAGE_DELETE = "Failed to delete emisora with ID: '%s'.";

  private EmisoraPersistenceException(final String message, final Throwable cause) {
    super(message, cause);
  }

  public static EmisoraPersistenceException becauseSaveFailed(final String id, final Throwable cause) {
    return new EmisoraPersistenceException(String.format(MESSAGE_SAVE, id), cause);
  }

  public static EmisoraPersistenceException becauseUpdateFailed(
      final String id, final Throwable cause) {
    return new EmisoraPersistenceException(String.format(MESSAGE_UPDATE, id), cause);
  }

  public static EmisoraPersistenceException becauseFindByIdFailed(
      final String id, final Throwable cause) {
    return new EmisoraPersistenceException(String.format(MESSAGE_FIND, id), cause);
  }

  public static EmisoraPersistenceException becauseFindByNombreFailed(
      final String nombre, final Throwable cause) {
    return new EmisoraPersistenceException(String.format(MESSAGE_NOMBRE, nombre), cause);
  }

  public static EmisoraPersistenceException becauseFindAllFailed(final Throwable cause) {
    return new EmisoraPersistenceException(MESSAGE_ALL, cause);
  }

  public static EmisoraPersistenceException becauseDeleteFailed(
      final String id, final Throwable cause) {
    return new EmisoraPersistenceException(String.format(MESSAGE_DELETE, id), cause);
  }
}
