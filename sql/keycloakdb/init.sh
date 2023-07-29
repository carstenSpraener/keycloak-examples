#!/bin/sh

mysqladmin -prootpwd create keycloakdb
mysql -prootpwd keycloakdb <./init.sql
