package dev.nasserjr.jwtscaffold.application.service.authvalidation;

import dev.nasserjr.jwtscaffold.interfaces.dto.auth.RegisterRequest;

public interface IRegistrationValidationService {
  /**
   * Validates a registration request against business rules
   * 
   * @param request the registration request to validate
   * @throws InvalidDataException if validation fails
   */
  void validateRegistrationRequest(RegisterRequest request);

  /**
   * Validates email format
   * 
   * @param email the email to validate
   * @return true if valid, false otherwise
   */
  boolean isValidEmail(String email);

  /**
   * Validates password strength
   * 
   * @param password the password to validate
   * @return true if valid, false otherwise
   */
  boolean isValidPassword(String password);

  /**
   * Validates username format and length
   * 
   * @param username the username to validate
   * @return true if valid, false otherwise
   */
  boolean isValidUsername(String username);

  /**
   * Validates phone number format
   * 
   * @param phoneNumber the phone number to validate
   * @return true if valid, false otherwise
   */
  boolean isValidPhoneNumber(String phoneNumber);

  /**
   * Validates age range
   * 
   * @param age the age to validate
   * @return true if valid, false otherwise
   */
  boolean isValidAge(Integer age);
}
