FROM gradle:jdk17-focal as extension-build

WORKDIR /extension-build

COPY src ./src/
COPY build.gradle ./

RUN gradle --no-daemon jar

FROM quay.io/keycloak/keycloak:latest as builder

# Enable health and metrics support
ENV KC_HEALTH_ENABLED=true
ENV KC_METRICS_ENABLED=true

# Configure a database vendor
ENV KC_DB=mariadb
ENV KC_HOSTNAME_PATH=/kc-auth

WORKDIR /opt/keycloak

COPY --from=extension-build /extension-build/build/libs/*.jar /opt/keycloak/providers
RUN mkdir /opt/keycloak/data/import

COPY ./saml-idp-test-realm.json /opt/keycloak/data/import/

# for demonstration purposes only, please make sure to use proper certificates in production instead
RUN keytool -genkeypair -storepass password -storetype PKCS12 -keyalg RSA -keysize 2048 -dname "CN=server" -alias server -ext "SAN:c=DNS:localhost,IP:127.0.0.1" -keystore conf/server.keystore &&\
    /opt/keycloak/bin/kc.sh build

# change these values to point to a running postgres instance
ENV KC_DB=mariadb
ENV KC_DB_URL=jdbc:mariadb://cs-keycloakdb:3306/keycloakdb
ENV KC_DB_USERNAME=keycloak
ENV KC_DB_PASSWORD=keycloakpwd

## Only needed when running behind a reverse proxy
ENV KC_PROXY=edge
ENV KC_HOSTNAME_STRICT=false
ENV KC_HOSTNAME_STRICT_BACKCHANNEL=false
ENV KC_HOSTNAME_URL=http://localhost:8080/
ENV KC_HOSTNAME_ADMIN_URL=http://localhost:8080/
ENV DEBUG="true"
ENV DEBUG_PORT="*:8787"

ENTRYPOINT ["/opt/keycloak/bin/kc.sh", "start-dev", "--import-realm"]
