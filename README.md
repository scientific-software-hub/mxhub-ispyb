[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=waltz-controls_mxhub-ispyb&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=waltz-controls_mxhub-ispyb)

[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=waltz-controls_mxhub-ispyb&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=waltz-controls_mxhub-ispyb)[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=waltz-controls_mxhub-ispyb&metric=reliability_rating)](https://sonarcloud.io/summary/new_code?id=waltz-controls_mxhub-ispyb)[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=waltz-controls_mxhub-ispyb&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=waltz-controls_mxhub-ispyb)

[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=waltz-controls_mxhub-ispyb&metric=sqale_index)](https://sonarcloud.io/summary/new_code?id=waltz-controls_mxhub-ispyb)[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=waltz-controls_mxhub-ispyb&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=waltz-controls_mxhub-ispyb)[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=waltz-controls_mxhub-ispyb&metric=bugs)](https://sonarcloud.io/summary/new_code?id=waltz-controls_mxhub-ispyb)

[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=waltz-controls_mxhub-ispyb&metric=ncloc)](https://sonarcloud.io/summary/new_code?id=waltz-controls_mxhub-ispyb)[![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=waltz-controls_mxhub-ispyb&metric=duplicated_lines_density)](https://sonarcloud.io/summary/new_code?id=waltz-controls_mxhub-ispyb)



# ISPyB Project

1. [Installing](#installing)
2. [Database creation and update](#database-creation-and-update)
3. [Database schema](#database-schema)
4. [Running with TomEE](#Project-Setup-Guide-with-TomEE-(10.x))

## Installing

1. Clone the ISPyB repository:

   ```
   git clone https://github.com/ispyb/ISPyB.git
   ```

2. ISPyB needs the third-party libraries provided in the `dependencies`
   directory.  These don't exist in a public repository, so install them to
   the local Maven repository so that Maven can find them:

   ```
   cd dependencies && mvn install
   ```

3. Build ISPyB using Maven:

   ```
   mvn clean install
   ```

   By default, ISPyB builds for the `GENERIC` site and the `development`
   environment.  These can be changed with the `ispyb.site` and `ispyb.env`
   system properties.  For example, to build for the `ESRF` site and the
   `test` environment:

   ```
   mvn -Dispyb.site=ESRF -Dispyb.env=test clean install
   ```

   If the build succeeds, a summary message will be printed like this:

   ```
   [INFO] Reactor Summary:
   [INFO]
   [INFO] ispyb-parent ...................................... SUCCESS [0.251s]
   [INFO] ispyb-ejb3 ........................................ SUCCESS [10.243s]
   [INFO] ispyb-ws .......................................... SUCCESS [1.751s]
   [INFO] ispyb-ui .......................................... SUCCESS [7.212s]
   [INFO] ispyb-ear ......................................... SUCCESS [5.048s]
   [INFO] ispyb-bcr ......................................... SUCCESS [2.217s]
   [INFO] ispyb-bcr-ear ..................................... SUCCESS [1.806s]
   ```
# Database

For development purposes and quick start up use bootstrap script in ispyb-database:

```
$> cd ispyb-database/scripts
$> ./bootstrap_db.sh
```
This will create all required users and tables in MariaDb. See the scripts file for more details.

## Database creation and update

### Prerequisites

MariaDB docker container for local development:

`docker pull mariadb:11.3-jammy`

`docker run --rm -p 3306:3306 -e  MARIADB_ROOT_PASSWORD=root --name mariadb mariadb:11.3-jammy`

`mysql` client: `sudo apt install mariadb-client`

### Create tables and schemas

Run the following creation scripts from the `ispyb-ejb` module (note
that this requires the `pxadmin` database user to exist and have full
permissions):

1. `ispyb-ejb/db/scripts/pyconfig.sql`: This corresponds to the menu
   options and contains both structure and data.

2. `ispyb-ejb/db/scripts/pydb.sql`: This corresponds to the ISPyB metadata
   and contains only the database structure.

3. `ispyb-ejb/db/scripts/schemastatus.sql`: This corresponds to the
   `SchemaStatus` table and contains both structure and data.  The entries
   indicate which update scripts have been run.

4. `ispyb-ejb/db/scripts/ispybAutoprocAttachment.sql`: This corresponds to
   the type and names of different autoPROC attachments.

The creation scripts are normally updated for each tag, but if you are
using the master branch, you may have to run the update scripts in
`ispyb-ejb/db/scripts/ahead`.

Check the entries in the `SchemaStatus` table to know which scripts
to execute.  The scripts already run for the current tag are in
`ispyb-ejb/db/scripts/passed`.

### Creating an update script

The first line must be:

```sql
insert into SchemaStatus (scriptName, schemaStatus) values ('2017_06_06_blabla.sql','ONGOING');
```

Then comes the actual updates of the script:

```sql
-- SQL statements here
```

And the last line must be:

```sql
update SchemaStatus set schemaStatus = 'DONE' where scriptName = '2017_06_06_blabla.sql';
```

The last line updates the `SchemaStatus` table to mark the script as
having been run.

## Database schema

A patch or commit that changes the database schema must be accompanied
by a corresponding change to the [schema file][schema-file]
to keep it up to date.  This file can be edited using [MySQL
Workbench][mysql-workbench] (a free tool from MySQL).

[schema-file]: https://github.com/ispyb/ISPyB/blob/master/documentation/database/ISPyB_DataModel.mwb
[mysql-workbench]: https://www.mysql.com/products/workbench/

## Project Setup Guide with TomEE (10.x)

This README provides instructions on how to set up the database connection for your project using Apache TomEE.

### Prerequisites

- Apache TomEE (configured to run your project)
- MariaDB or MySQL installed and running
- JDBC driver for MariaDB/MySQL placed in the `lib` directory of TomEE

### TomEE Configuration

1. **Datasource Configuration in `tomee.xml`**:
   - Navigate to the `conf` directory of your TomEE installation.
   - Open or create the `tomee.xml` file and add the following configuration:
     ```xml
     <Resource id="ispyb" type="DataSource">
       JdbcDriver org.mariadb.jdbc.Driver
       JdbcUrl jdbc:mariadb://localhost:3306/ispyb_build
       UserName pxuser
       Password pxuser
       JtaManaged true
     </Resource>
     ```

### Persistence Configuration

Ensure that your `persistence.xml` file is set up to use the JTA data source. Here is an example snippet:

```xml
<persistence-unit name="ispyb_db" transaction-type="JTA">
    <provider>org.apache.openjpa.persistence.PersistenceProviderImpl</provider>
    <jta-data-source>ispyb</jta-data-source>
    <properties>
        <property name="openjpa.Log" value="DefaultLevel=WARN, Runtime=INFO, Tool=INFO, SQL=TRACE"/>
    </properties>
</persistence-unit>
```

### Deployment

Build your project and deploy the resulting EAR file to TomEE.

Start or restart TomEE to pick up the new configurations.

### Using docker

For CI/CD use .docker/Dockerfile as it uses multistaged build: `docker build . -f .docker/Dockerfile`

For local development/testing use Dockerfile: `docker build .` [Optionaly provide name/tag etc]

### Troubleshooting

- Connection Issues: Ensure the database URL, username, and password are correct in tomee.xml.
- JDBC Driver: Verify that the JDBC driver for MariaDB/MySQL is correctly placed in the lib folder of your TomEE installation.
- For further assistance, consult the TomEE documentation or reach out to the community forums.

[![SonarCloud](https://sonarcloud.io/images/project_badges/sonarcloud-orange.svg)](https://sonarcloud.io/summary/new_code?id=waltz-controls_mxhub-ispyb)