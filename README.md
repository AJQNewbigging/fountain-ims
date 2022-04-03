Coverage: 88%
Jira: [FIMS board](https://anewbigging.atlassian.net/jira/software/projects/FIMS/boards/2)
# Fountain Inventory Management System

A basic text-based inventory management session, dubbed 'Fountain IMS' due to the creator's membership of Team Water of the 22FebEnable3 cohort. As this application currently stands (v.1.0), logic exists for customer, item, and order management. This is a Java application using a MySQL database.

## Getting Started

Database schemas are kept in src/main/resources/sql-shemas.sql, these will need to be run before the application can be executed. Example test data is also available in src/main/resources/sql-data.sql, if you would like to use it.

Changes may need to be made to db.properties in the same directory. These properties should match the proper information for a user with access to the IMS database.

Once the IMS database has been correctly initialised, the application can be run. A compiled version of the application exists within target/. This can be run in your terminal with:

```bash
java -jar target/ims-0.0.1-jar-with-dependencies.jar
```

### Prerequisites

If you do not have an installation of MySQL, a comprehensive installation guide for your device can be found [here](https://dev.mysql.com/doc/mysql-installation-excerpt/5.7/en/).

* MySQL version 14.14; any version between this and the latest will work equally well.

* Java JDK version 16.0.1. You can find a comprehensive installation guide for this [here](https://www.java.com/en/download/help/download_options.html)

* Project Lombok (latest); this is used to generate bioler-plate code through class annotations. An installation guide is available [here](https://projectlombok.org/setup/eclipse).

* Maven (latest); Maven is our build management tool, used to manage dependencies and build our application. Find an installation guide [here](https://maven.apache.org/install.html).

### Installing

1. Import SQL schema:

```bash
mysql -u user -p < src/main/resources/sql-schema.sql
```
2. Import SQL data (optional)

```bash
mysql -u user -p < src/main/resources/sql-data.sql
```

3. Build the application in your IDE with maven (optional). Note: a pre-built jar may already exist in the target/ directory, check this if you don't want to overwrite it.

```bash
mvn clean install
```

4. Run the application in your terminal

```bash
java -jar target/ims-0.0.1-jar-with-dependencies.jar
```

## Built With

* [Maven](https://maven.apache.org/) - Dependency Management

## Versioning

We use [SemVer](http://semver.org/) for versioning.

## Authors

* **Chris Perrins** - *Initial work* - [christophperrins](https://github.com/christophperrins)
* **Ash Newbigging** - *Continued work* - [AJQNewbigging](https://github.com/AJQNewbigging)

## License

This project is licensed under the MIT license - see the [LICENSE.md](LICENSE.md) file for details 

*For help in [Choosing a license](https://choosealicense.com/)*

## Acknowledgments

* Website [tabnine](https://www.tabnine.com) provided insight into [batches in Prepared Statements](https://www.tabnine.com/code/java/methods/java.sql.PreparedStatement/addBatch).
* [Project Lombok](https://projectlombok.org)
