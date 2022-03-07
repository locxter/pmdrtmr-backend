# pmdrtmr-backend

## Overview

This repository includes the backend of my Pomodoro timer project pmdrtmr, which I build during my work placement at the [KDO](https://www.kdo.de/).

## Dependencies

I generally try to minimize dependencies, but I'm a one man crew and can therefore only support Debian as I'm running it myself. Anyway, you need to have the following packages installed for everything to work properly:

- JDK for developing Java programs. Install it with `sudo apt install openjdk-17-jdk`.
- Maven for building the whole thing. Install it with `sudo apt install maven`.
- MariaDB as a database for storage. Install it with `sudo apt install mariadb-server`.
- VSCodium as universal text editor and IDE. To install it visit their [website](https://vscodium.com/#install).
- VSCodium extensions Debugger for Java, Language support for Java, Maven for Java, Project Manager for Java and Test Runner for Java to enhance it's capabilities. Install them with `codium --install-extension vscjava.vscode-java-debug --install-extension redhat.java --install-extension vscjava.vscode-maven --install-extension vscjava.vscode-java-dependency --install-extension vscjava.vscode-java-test`.

## How to use it

First secure the mariaDB installation via `sudo mysql_secure_installation` (choose `Enter`, then `N` twice and finally `Y` for all following questions), login to mariaDB via `sudo mysql -u root`, create the needed database via `create database pmdrtmr;` as well as user via `grant all privileges on pmdrtmr.* to 'pmdrtmr' identified by 'pmdrtmr';`. Then run the program directly via `mvn spring-boot:run` or build the JAR via `mvn clean package` and then run it via `java -jar target/pmdrtmr-backend-1.0.jar`.
