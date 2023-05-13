# pmdrtmr backend

## Overview

> This project is **DEPRECATED**, therefore might not be on par with my current coding standards and will not receive any updates now or in the future.

This repository includes the backend of my Pomodoro timer project pmdrtmr, which I build during my work placement at the [KDO](https://www.kdo.de/).

## Dependencies

I generally try to minimize dependencies, but I'm a one man crew and can therefore only support Debian-based Linux distributions as I'm running one myself. Anyway, you need to have the following packages installed for everything to work properly:

- SDKMAN! for managing all the JVM dependencies. Install it via the [installation guide](https://sdkman.io/install).
- JDK for developing Java programs. Install it with `sdk install java`.
- Maven for building the whole thing. Install it with `sdk install maven`.
- MariaDB as a database for storage. Install it with `sudo apt install mariadb-server`.

## How to use it

First secure the mariaDB installation via `sudo mysql_secure_installation` (choose `Enter`, then `N` twice and finally `Y` for all following questions), login to mariaDB via `sudo mysql -u root`, create the needed database via `create database pmdrtmr;` as well as user via `grant all privileges on pmdrtmr.* to 'pmdrtmr' identified by 'pmdrtmr';`. Then build the JAR via `mvn clean package` and then run it via `java -jar target/pmdrtmr-backend-1.0.jar`.
