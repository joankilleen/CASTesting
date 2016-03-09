package org.books.application.exception;

import javax.ejb.ApplicationException;

@ApplicationException(rollback = true)
public class BookAlreadyExistsException extends Exception {
}
