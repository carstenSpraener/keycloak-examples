create user if not exists 'keycloak'@'%' identified by 'keycloakpwd';
grant all privileges on *.* to 'keycloak'@'%';
