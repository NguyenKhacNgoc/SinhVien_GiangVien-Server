package com.example.sv.DTO;

public class UserDTO {
    private String email;
    private String password;
    private int verificationCode;
    private String role;

    public UserDTO(String email, String password, int verificationCode) {
        this.email = email;
        this.password = password;
        this.verificationCode = verificationCode;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getVerificationCode() {
        return this.verificationCode;
    }

    public void setVerificationCode(int verificationCode) {
        this.verificationCode = verificationCode;
    }

    public String getRole() {
        return this.role;
    }

    public void setRole(String role) {
        this.role = role;
    }

}
