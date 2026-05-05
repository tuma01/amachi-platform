package com.amachi.app.core.auth.dto.invitation;

import jakarta.validation.constraints.*;

/**
 * DTO submitted by the invited person when completing their self-onboarding.
 * (Manual Implementation to resolve Lombok resolution issues)
 */
public class CompleteRegistrationRequest {

    @NotBlank(message = "validation.registration.token.required")
    private String token;

    @NotBlank(message = "validation.registration.loginEmail.required")
    @Email(message = "validation.registration.loginEmail.invalid")
    @Size(max = 100, message = "validation.registration.loginEmail.maxLength")
    private String loginEmail;

    @NotBlank(message = "validation.registration.password.required")
    @Size(min = 8, max = 100, message = "validation.registration.password.minLength")
    private String password;

    @NotBlank(message = "validation.registration.firstName.required")
    @Size(min = 2, max = 50, message = "validation.registration.firstName.size")
    private String firstName;

    private String middleName;

    @NotBlank(message = "validation.registration.lastName.required")
    @Size(min = 2, max = 50, message = "validation.registration.lastName.size")
    private String lastName;

    private String secondLastName;

    @Size(max = 50, message = "validation.registration.phoneNumber.maxLength")
    @Pattern(regexp = "^[+]?[0-9\\s\\-()]{7,20}$", message = "validation.registration.phoneNumber.pattern")
    private String phoneNumber;

    @Email(message = "validation.registration.personalEmail.invalid")
    @Size(max = 100, message = "validation.registration.personalEmail.maxLength")
    private String personalEmail;

    public CompleteRegistrationRequest() {}

    public CompleteRegistrationRequest(String token, String loginEmail, String password, String firstName, String middleName, String lastName, String secondLastName, String phoneNumber, String personalEmail) {
        this.token = token;
        this.loginEmail = loginEmail;
        this.password = password;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.secondLastName = secondLastName;
        this.phoneNumber = phoneNumber;
        this.personalEmail = personalEmail;
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public String getLoginEmail() { return loginEmail; }
    public void setLoginEmail(String loginEmail) { this.loginEmail = loginEmail; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getMiddleName() { return middleName; }
    public void setMiddleName(String middleName) { this.middleName = middleName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getSecondLastName() { return secondLastName; }
    public void setSecondLastName(String secondLastName) { this.secondLastName = secondLastName; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public String getPersonalEmail() { return personalEmail; }
    public void setPersonalEmail(String personalEmail) { this.personalEmail = personalEmail; }

    public static CompleteRegistrationRequestBuilder builder() {
        return new CompleteRegistrationRequestBuilder();
    }

    public static class CompleteRegistrationRequestBuilder {
        private String token;
        private String loginEmail;
        private String password;
        private String firstName;
        private String middleName;
        private String lastName;
        private String secondLastName;
        private String phoneNumber;
        private String personalEmail;

        public CompleteRegistrationRequestBuilder token(String token) { this.token = token; return this; }
        public CompleteRegistrationRequestBuilder loginEmail(String loginEmail) { this.loginEmail = loginEmail; return this; }
        public CompleteRegistrationRequestBuilder password(String password) { this.password = password; return this; }
        public CompleteRegistrationRequestBuilder firstName(String firstName) { this.firstName = firstName; return this; }
        public CompleteRegistrationRequestBuilder middleName(String middleName) { this.middleName = middleName; return this; }
        public CompleteRegistrationRequestBuilder lastName(String lastName) { this.lastName = lastName; return this; }
        public CompleteRegistrationRequestBuilder secondLastName(String secondLastName) { this.secondLastName = secondLastName; return this; }
        public CompleteRegistrationRequestBuilder phoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; return this; }
        public CompleteRegistrationRequestBuilder personalEmail(String personalEmail) { this.personalEmail = personalEmail; return this; }

        public CompleteRegistrationRequest build() {
            return new CompleteRegistrationRequest(token, loginEmail, password, firstName, middleName, lastName, secondLastName, phoneNumber, personalEmail);
        }
    }
}
