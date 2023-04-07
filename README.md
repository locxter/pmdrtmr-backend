# pmdrtmr backend

## Overview

> Please keep in mind that this way my first try at building a client-server app and is **not my primary focus**
> anymore.

This repository includes the backend of my Pomodoro timer project pmdrtmr, which I build during my work placement at
the [KDO](https://www.kdo.de/).

## Dependencies

I generally try to minimize dependencies, but I'm a one man crew and can therefore only support Debian-based Linux
distributions as I'm running one myself. Anyway, you need to have the following packages installed for everything to
work properly:

- JDK for developing Java programs. Install it with `sudo apt install openjdk-17-jdk`.
- Maven for building the whole thing. Install it with `sudo apt install maven`.
- MariaDB as a database for storage. Install it with `sudo apt install mariadb-server`.

## How to use it

First secure the mariaDB installation via `sudo mysql_secure_installation` (choose `Enter`, then `N` twice and
finally `Y` for all following questions), login to mariaDB via `sudo mysql -u root`, create the needed database
via `create database pmdrtmr;` as well as user
via `grant all privileges on pmdrtmr.* to 'pmdrtmr' identified by 'pmdrtmr';`. Then build the JAR
via `mvn clean package` and then run it via `java -jar target/pmdrtmr-backend-1.0.jar`.
