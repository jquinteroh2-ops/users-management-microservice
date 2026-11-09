package com.jcaa.usersmanagement.domain.exception;

public final class EmisoraAlreadyExistsException extends DomainException {

  private static final String MESSAGE_ID = "An emisora with id '%s' already exists.";
  private static final String MESSAGE_NOMBRE = "An emisora named '%s' already exists.";

  private EmisoraAlreadyExistsException(final String message) {
    super(message);
  }

  public static EmisoraAlreadyExistsException becauseIdAlreadyExists(final String id) {
    return new EmisoraAlreadyExistsException(String.format(MESSAGE_ID, id));
  }

  public static EmisoraAlreadyExistsException becauseNombreAlreadyExists(final String nombre) {
    return new EmisoraAlreadyExistsException(String.format(MESSAGE_NOMBRE, nombre));
  }
}
