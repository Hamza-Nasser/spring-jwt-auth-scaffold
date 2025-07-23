package dev.nasserjr.jwtscaffold.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class User {
  private Long id;

  private String name;

  @JsonIgnore
  private String password;

  private String email;

  private String phone;

  public User(Long id, String name, String password, String email, String phone) {
    this.id = id;
    this.name = name;
    this.password = password;
    this.email = email;
    this.phone = phone;
  }

  public User() {
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
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
}
