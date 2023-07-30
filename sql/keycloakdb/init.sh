#!/bin/sh

mysqladmin -prootpwd create keycloakdb
mysql -prootpwd keycloakdb </docker-entrypoint-initdb.d/init.sql
