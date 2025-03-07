# Stage 1: Building the application
FROM maven:3.9.6-eclipse-temurin-21 as builder

# Set the working directory in the container
WORKDIR /src

# Copy the Java source code, pom.xml, and other build files into the image
COPY . /src

WORKDIR /src/dependencies

RUN mvn install -q --no-transfer-progress

WORKDIR /src

# Build the application
RUN mvn clean package -DskipTests -q --no-transfer-progress

# Stage 2: Setting up the TomEE server
# Start with a base image containing Java runtime 21
FROM eclipse-temurin:21-jre

# Install necessary packages for downloading and managing Tomcat
RUN apt-get update && apt-get install -y wget tar

# Define environment variables for Tomcat version and installation directory
ENV TOMCAT_VERSION 10.0.0-M2
ENV TOMCAT_JAKARTA_PROFILE plume
ENV CATALINA_HOME /usr/local/tomcat
ENV PATH $CATALINA_HOME/bin:$PATH

# Download and install Tomcat
RUN wget https://dlcdn.apache.org/tomee/tomee-$TOMCAT_VERSION/apache-tomee-$TOMCAT_VERSION-$TOMCAT_JAKARTA_PROFILE.tar.gz -O /tmp/tomcat.tar.gz \
    && tar -xvf /tmp/tomcat.tar.gz -C /usr/local \
    && mv /usr/local/apache-tomee-$TOMCAT_JAKARTA_PROFILE-$TOMCAT_VERSION $CATALINA_HOME \
    && rm /tmp/tomcat.tar.gz \
    && rm $CATALINA_HOME/lib/eclipselink* \
    && chmod +x $CATALINA_HOME/bin/catalina.sh

# Copy the WAR file from the builder stage to the TomEE webapps directory
COPY --from=builder /src/ispyb-ear/target/ispyb.ear $CATALINA_HOME/webapps/

# Copy the pdf resource required for parcel label
COPY ispyb-ejb/pdf/ParcelLabelsTemplate-WithWorldCourierCL.pdf /etc/ispyb/pdf/

# Copy tomee.xml configuration file
COPY configuration/tomee/tomee.xml $CATALINA_HOME/conf/
COPY configuration/tomee/server.xml $CATALINA_HOME/conf/

# Copy jdbc driver configuration file
COPY configuration/mariadb/mariadb-java-client-3.3.3.jar $CATALINA_HOME/lib/

# Expose the port TomEE is running on
EXPOSE 8080

ENV JAVA_OPTS="-Dtomee.serialization.class.blacklist=- -Dtomee.serialization.class.whitelist=* -Dispyb.properties=file:///etc/ispyb/ispyb.properties -Dtomee.jaxws.subcontext=/ispybWS"

# Start TomEE server
CMD ["catalina.sh", "run"]