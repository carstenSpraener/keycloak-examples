# SAML with keycloak

The project demonstrate a possible implementation of the SAML-Protocol with
KeyCloak and spring-boot 3.1. 

### Start the example

The whole example build and runs in docker-compose so the only thing
you need to have on your development machine is __Docker__. It will use the 
ports 8080 and 8081 for keycloak and spring-boot. This ports needs to be free.

When you met this requirement you can clone this project into a local 
directory by:

```bash
git clone https://github.com/carstenSpraener/keycloak-examples.git
```

Then go into to keycloak-example folder and start docker-comopse with:

```bash
docker-compose up -d; docker-compose logs -f
```

This will start the build process and the docker-compose containers will
start automatically. 

__Note__ At the first startup you will see __a lot of errors__ and the
spring-boot application and the keycloak-server will start over and over
again. This is because the mysql database needs to initialize and that 
takes a while. To prevent this see _Starting first time one by one_

When the mysql database is initialized the keycloak-server will also start
to initialize itself. This again will take some time and spring-boot keeps
restarting.

When keycloak-server is also finished with its initialization, the spring-boot
application will also come to a successful boot:


```log
cs-saml-app    | ... kc.saml.backend.SamlApp : Started SamlApp in 3.51 seconds (process running for 4.015)
```
### Starting first time one by one

If you want to start the containers one by one for the first time to prevent the error messages you 
can do the following commands one after the other:

__Start the mariadb database__

```bash
 docker-compose up -d cs-keycloakdb; docker-compose logs -f cs-keycloakdb 
```

After a while the database will finish its initialization and the server states that it is ready
to accept connections:
```log
cs-keycloakdb  | ...[Note] mysqld: ready for connections.
cs-keycloakdb  | Version: '10.9.5-MariaDB-1:10.9.5+maria~ubu2204'  socket: '/run/mysqld/mysqld.sock'  port: 3306  mariadb.org binary distribution
```

Press Ctrl-C to terminate the log output. The container will keep running.

__Starting KeyCloak server__

```bash
docker-compose up -d cs-keycloak; docker-compose logs -f cs-keycloak
```
The server starts up, imports the realm and after a while you will see the following 
log message:

```log
cs-keycloak  | ... INFO  [org.keycloak.services] (main) KC-SERVICES0009: Added user 'admin' to realm 'master'
cs-keycloak  | ... WARN  [org.keycloak.quarkus.runtime.KeycloakMain] (main) Running the server in development mode. DO NOT use this configuration in production.
```

Press Ctrl-C to terminate the log output. The container will keep running.

__Starting spring boot application__

To start the spring boot application do:

```bash
docker-compose up -d cs-saml-app; docker-compose logs -f cs-saml-app
```

When you see the following message, the server is up and running.

```log
cs-saml-app  | ...  INFO 1 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 8081 (http) with context path ''
cs-saml-app  | ...  INFO 1 --- [           main] kc.saml.backend.SamlApp                  : Started SamlApp in 3.338 seconds (process running for 4.128)
```

Again you can press Ctrl-C to terminate the log output. The container will keep running.

If you want to see all logs enter the following command:
```bash
docker-compose logs -f
```

## Create a first user

The keycloak realm _SAML-IDP-Test_ is not configured for registration, so 
you have to create a first user.

Open the following URL and log in as user `admin` with password `admin1`:

http://localhost:8080/admin/master/console/

Select the __SAML-IDP-Test__ realm and create a test user as you like.

## Log in to the application

Open the saml-app und the url: http://localhost:8081/ and click on the
_[Go to the protected site home.html](http://localhost:8081/home.html)_. 

This will delegate you to the KeyCloak login page, and you can log in with
the user you created in the previous step.


## MagicLink set up

[MagicLink-Integration.md](keycloak%2Fdoc%2FMagicLink-Integration.md)

## Cleaning up

When you finished with experimenting with keycloak use this commands to remove
the containers from your docker:

```bash
docker-compose stop 
docker-compose rm -f
docker image rm keycloak-examples-cs-saml-app keycloak-examples-cs-keycloak
rm -rf keycloakdb/*
```
