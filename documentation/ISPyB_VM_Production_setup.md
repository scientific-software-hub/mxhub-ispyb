# ISPyB VM Production setup
[Virtual Machine setup](#virtual-machine-setup)

[Docker containers' creation](#docker-containers-creation)

[Observability for ISPyB](#observability-for-ISPyB)

[Security and nginx](#security-and-nginx)

#
## Virtual Machine setup
This guide outlines the steps required to set up the ISPyB environment on a Virtual Machine (VM) running **Ubuntu**. 
The process includes installing necessary components such as Nginx, Docker Engine and pulling essential Docker images.

#### Install Nginx for Security and Reverse Proxy
Nginx will be used to secure the deployment and manage reverse proxy configurations.

Follow one of the guides below to install Nginx:
* DigitalOcean: [How to Install Nginx on Ubuntu 22.04](https://www.digitalocean.com/community/tutorials/how-to-install-nginx-on-ubuntu-22-04)
* NGINX Official Documentation: [Installing NGINX Open Source](https://docs.nginx.com/nginx/admin-guide/installing-nginx/installing-nginx-open-source/#downloading-the-sources)

#### Install Docker Engine
Docker is required to run ISPyB and its supporting services in isolated containers. To install Docker on Ubuntu, follow the official guide:
* Docker Documentation: [Install Docker Engine on Ubuntu on VM](https://docs.docker.com/engine/install/ubuntu/)

#### Pull Required Docker Images

Pull the following docker images:
* EXI
* ISPYB
* MariaDB (the MariaDB image to serve as the database backend for ISPyB):
```
docker pull mariadb
```
* phpmyadmin (the phpMyAdmin image for managing the MariaDB database through a web interface):
```
docker pull phpmyadmin/phpmyadmin
```
#### Configure ISPyB Properties
Place the ISPyB configuration file (ISPyB.properties) in a dedicated directory. For example:
```
/etc/ispyb/ISPyB.properties
```

#
## Docker containers' creation


#
## Observability for ISPyB


#
## Security and nginx


