# Start with a base image containing Java runtime 21
FROM eclipse-temurin:21-jre-alpine

# Install necessary packages for downloading and managing Tomcat
RUN apt-get update && apt-get install -y wget tar

# Define environment variables for Tomcat version and installation directory
ENV TOMCAT_VERSION 10.0.0-M3
ENV TOMCAT_JAKARTA_PROFILE plume
ENV CATALINA_HOME /usr/local/tomcat
ENV PATH $CATALINA_HOME/bin:$PATH

# Environment variable to set the timezone for the containers
ENV TZ=Europe/Berlin

# Create a non-root user for Tomcat
RUN addgroup -S tomcat && adduser -S tomcat -G tomcat

# Download and install Tomcat
RUN wget https://dlcdn.apache.org/tomee/tomee-$TOMCAT_VERSION/apache-tomee-$TOMCAT_VERSION-$TOMCAT_JAKARTA_PROFILE.tar.gz -O /tmp/tomcat.tar.gz \
    && tar -xvf /tmp/tomcat.tar.gz -C /usr/local \
    && mv /usr/local/apache-tomee-$TOMCAT_JAKARTA_PROFILE-$TOMCAT_VERSION $CATALINA_HOME \
    && rm /tmp/tomcat.tar.gz \
    && rm $CATALINA_HOME/lib/eclipselink* \
    && rm $CATALINA_HOME/lib/openejb-core-eclipselink* \
    && chmod +x $CATALINA_HOME/bin/catalina.sh

RUN apt-get remove --purge wget tar

# Change ownership to non-root user
RUN chown -R tomcat:tomcat $CATALINA_HOME \
    && chmod -R 444 $CATALINA_HOME/webapps \
    && chmod -R 444 $CATALINA_HOME/conf \
    && chmod -R 444 /etc/ispyb/pdf

# Switch to non-root user
USER tomcat

# Copy the EAR file from the builder stage to the Tomcat webapps directory
COPY --chown=tomcat:tomcat ispyb-ear/target/ispyb.ear $CATALINA_HOME/webapps/

# Copy the pdf resource required for parcel label
COPY --chown=tomcat:tomcat ispyb-ejb/pdf/ParcelLabelsTemplate-WithWorldCourierCL.pdf /etc/ispyb/pdf/

# Copy tomee.xml configuration file to the Tomcat conf directory
COPY --chown=tomcat:tomcat configuration/tomee/tomee.xml $CATALINA_HOME/conf/
COPY --chown=tomcat:tomcat configuration/tomee/server.xml $CATALINA_HOME/conf/

# disbale file based log appenders
COPY --chown=tomcat:tomcat configuration/tomee/logging.properties $CATALINA_HOME/conf/

# Copy JDBC driver to the Tomcat lib directory
COPY --chown=tomcat:tomcat configuration/mariadb/mariadb-java-client-3.3.3.jar $CATALINA_HOME/lib/

# Expose the port Tomcat is running on
EXPOSE 8080

# Environment variable for Java options, include serialization configs
ENV JAVA_OPTS="-Dtomee.serialization.class.blacklist=- -Dtomee.serialization.class.whitelist=* -Dispyb.properties=file:///etc/ispyb/ispyb.properties -Dtomee.jaxws.subcontext=/ispybWS -Dopenejb.localcopy=false"

# Start Tomcat server
CMD ["catalina.sh", "run"]
