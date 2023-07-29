package de.spraener.keycloak.demo.payload;

import lombok.Data;

@Data
public class LinkRequest {
    private String userEmail;
    private String clientID;
}
