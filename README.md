# keycloak-examples

## SAML with keycloak

The project demonstrate a possible implementation of the SAML-Protocol with
KeyCloak and spring-boot 3.1. 

### Start the example

The whole example build and runs in docker-compose so the only thing
you need to have on your development machine is __Docker__.

When you met this requirement you can clone this projrct into a local 
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
again. This is because the mysql database needs to initialze and that 
takes a while.

When the mysql database is initialized the keycloak-server will also start
to initialize itself. This again will take some time and spring-boot keeps
restarting.

When keycloak-server is also finished with its initialization, the spring-boot
application will also come to a successfull boot:

```log
cs-saml-app    | ... kc.saml.backend.SamlApp : Started SamlApp in 3.51 seconds (process running for 4.015)
```

## Create a first user

The keycloak realm _SAML-IDP-Test_ is not configured for registration so 
you have to create a first user.

Open the following URL and log in as user `admin` with password `admin1`:

http://localhost:8080/admin/master/console/

Select the __SAML-IDP-Test__ realm and create a test user as you like.

## Login to the application

Open the saml-app und the url: http://localhost:8081/ and click on the
_[Go to the protected site home.html](http://localhost:8081/home.html)_. 

This will delegate you to the KeyCloak login page, and you can log in with
the user you created in the previous step.
