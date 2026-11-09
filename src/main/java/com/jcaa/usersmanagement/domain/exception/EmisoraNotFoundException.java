package com.jcaa.usersmanagement.domain.exception;

public final class EmisoraNotFoundException extends DomainException {

  private static final String MESSAGE_BY_ID = "The emisora with id '%s' was not found.";

  private EmisoraNotFoundException(final String message) {
    super(message);
  }

  public static EmisoraNotFoundException becauseIdWasNotFound(final String emisoraId) {
    return new EmisoraNotFoundException(String.format(MESSAGE_BY_ID, emisoraId));
  }
}
