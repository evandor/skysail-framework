package de.twenty11.skysail.server.um.domain;

public class RegistrationConfirmation {

    private String registrationId;

    public RegistrationConfirmation(String registrationId) {
        this.registrationId = registrationId;
    }

    public String getRegistrationId() {
        return registrationId;
    }
}
