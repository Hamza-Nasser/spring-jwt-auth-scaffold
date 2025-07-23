package dev.nasserjr.jwtscaffold.interfaces.dto.auth;

import org.springframework.format.annotation.NumberFormat;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterRequest {

  @NotBlank(message = "Email is required")
  @Email(message = "Email must be in a valid format")
  private String email;

  @NotBlank(message = "Phone is required")
  @NumberFormat
  private String phone;

  @NotBlank(message = "Password is required")
  @Size(min = 6, message = "Password must be of length 6 or higher")
  private String password;

  @NotBlank(message = "Name is required")
  private String name;

  public RegisterRequest(String email, String phone, String password, String name) {
    this.email = email;
    this.phone = phone;
    this.password = password;
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

}
