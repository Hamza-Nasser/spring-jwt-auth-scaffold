package dev.nasserjr.jwtscaffold.application.service.authvalidation;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import dev.nasserjr.jwtscaffold.infrastructure.exception.InvalidDataException;
import dev.nasserjr.jwtscaffold.interfaces.dto.auth.RegisterRequest;

@Service
public class RegistrationValidationService implements IRegistrationValidationService {

  private static final Logger log = LoggerFactory.getLogger(RegistrationValidationService.class);

  private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
  private static final String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
  private static final String USERNAME_PATTERN = "^[a-zA-Z0-9_]{3,30}$";
  private static final String PHONE_PATTERN = "^\\+?[1-9]\\d{1,14}$";

  private static final int MIN_AGE = 13;
  private static final int MAX_AGE = 120;

  private static final Pattern EMAIL_REGEX = Pattern.compile(EMAIL_PATTERN);
  private static final Pattern PASSWORD_REGEX = Pattern.compile(PASSWORD_PATTERN);
  private static final Pattern USERNAME_REGEX = Pattern.compile(USERNAME_PATTERN);
  private static final Pattern PHONE_REGEX = Pattern.compile(PHONE_PATTERN);

  @Override
  public void validateRegistrationRequest(RegisterRequest request) {
    log.debug("Starting validation for registration request: {}", request.getEmail());

    List<String> errors = new ArrayList<>();

    validateEmailField(request.getEmail(), errors);
    // validatePasswordField(request.getPassword(), errors);
    // validateUsernameField(request.getName(), errors);
    validatePhoneNumberField(request.getPhone(), errors);

    validateCrossFieldRules(request, errors);

    if (!errors.isEmpty()) {
      log.warn("Validation failed for registration request: {}", String.join(", ", errors));
      throw new InvalidDataException(String.join(", ", errors));
    }

    log.debug("Validation successful for registration request: {}", request.getEmail());
  }

  @Override
  public boolean isValidEmail(String email) {
    if (email == null || email.trim().isEmpty()) {
      return false;
    }

    String trimmedEmail = email.trim().toLowerCase();

    if (trimmedEmail.length() > 254) {
      return false;
    }

    String[] parts = trimmedEmail.split("@");
    if (parts.length != 2 || parts[0].length() > 64) {
      return false;
    }

    return EMAIL_REGEX.matcher(trimmedEmail).matches();
  }

  @Override
  public boolean isValidPassword(String password) {
    if (password == null) {
      return false;
    }

    if (!PASSWORD_REGEX.matcher(password).matches()) {
      return false;
    }

    return !isCommonPassword(password) && !containsSequentialChars(password);
  }

  @Override
  public boolean isValidUsername(String username) {
    if (username == null || username.trim().isEmpty()) {
      return false;
    }

    String trimmedUsername = username.trim();

    if (!USERNAME_REGEX.matcher(trimmedUsername).matches()) {
      return false;
    }

    return !isReservedUsername(trimmedUsername) &&
        !containsOnlyNumbers(trimmedUsername) &&
        !startsOrEndsWithUnderscore(trimmedUsername);
  }

  @Override
  public boolean isValidPhoneNumber(String phoneNumber) {
    if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
      return true;
    }

    String cleanedPhone = phoneNumber.replaceAll("[\\s\\-\\(\\)\\.]", "");

    return PHONE_REGEX.matcher(cleanedPhone).matches();
  }

  @Override
  public boolean isValidAge(Integer age) {
    return age != null && age >= MIN_AGE && age <= MAX_AGE;
  }

  private void validateEmailField(String email, List<String> errors) {
    if (email == null || email.trim().isEmpty()) {
      errors.add("Email is required");
      return;
    }

    if (!isValidEmail(email)) {
      errors.add("Invalid email format");
    }
  }

  private void validatePasswordField(String password, List<String> errors) {
    if (password == null || password.isEmpty()) {
      errors.add("Password is required");
      return;
    }

    if (password.length() < 8) {
      errors.add("Password must be at least 8 characters long");
    } else if (!isValidPassword(password)) {
      errors.add(
          "Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character");
    }
  }

  private void validateUsernameField(String username, List<String> errors) {
    if (username == null || username.trim().isEmpty()) {
      errors.add("Username is required");
      return;
    }

    if (!isValidUsername(username)) {
      errors.add("Username must be 3-30 characters long and contain only letters, numbers, and underscores");
    }
  }

  private void validatePhoneNumberField(String phoneNumber, List<String> errors) {
    if (phoneNumber != null && !phoneNumber.trim().isEmpty() && !isValidPhoneNumber(phoneNumber)) {
      errors.add("Invalid phone number format");
    }
  }

  private void validateCrossFieldRules(RegisterRequest request, List<String> errors) {

    if (request.getEmail() != null && request.getEmail() != null) {
      String emailLocalPart = request.getEmail().split("@")[0];
      if (emailLocalPart.equalsIgnoreCase(request.getEmail())) {
        errors.add("Username cannot be the same as email address");
      }
    }

    if (request.getPassword() != null && request.getEmail() != null) {
      if (request.getPassword().toLowerCase().contains(request.getEmail().toLowerCase())) {
        errors.add("Password cannot contain username");
      }
    }
  }

  private boolean isCommonPassword(String password) {

    Set<String> commonPasswords = Set.of(
        "password", "123456789", "12345678", "password123",
        "admin123", "welcome123", "qwerty123", "letmein123");
    return commonPasswords.contains(password.toLowerCase());
  }

  private boolean containsSequentialChars(String password) {

    for (int i = 0; i < password.length() - 2; i++) {
      char c1 = password.charAt(i);
      char c2 = password.charAt(i + 1);
      char c3 = password.charAt(i + 2);

      if ((c2 == c1 + 1 && c3 == c2 + 1) || (c2 == c1 - 1 && c3 == c2 - 1)) {
        return true;
      }
    }
    return false;
  }

  private boolean isReservedUsername(String username) {

    Set<String> reservedUsernames = Set.of(
        "admin", "administrator", "root", "user", "test", "guest",
        "api", "system", "support", "help", "info", "mail", "email",
        "www", "ftp", "ssh", "null", "undefined");
    return reservedUsernames.contains(username.toLowerCase());
  }

  private boolean containsOnlyNumbers(String username) {
    return username.matches("^\\d+$");
  }

  private boolean startsOrEndsWithUnderscore(String username) {
    return username.startsWith("_") || username.endsWith("_");
  }
}
