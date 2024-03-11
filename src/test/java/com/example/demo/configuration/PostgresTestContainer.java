package com.example.demo.configuration;

import org.postgresql.ds.PGSimpleDataSource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;
import javax.sql.DataSource;
import java.util.Objects;

public class PostgresTestContainer extends PostgreSQLContainer<PostgresTestContainer> {

    public static PostgresTestContainer container;

    public PostgresTestContainer() {
        super(DockerImageName.parse("postgres:12"));
    }

    public static PostgresTestContainer getInstance() {
        if(Objects.isNull(container)) {
            container = new PostgresTestContainer();
            container.withInitScript("init.sql");
        }
        return container;
    }

    public static DataSource dataSource() {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();

        var url = container.getJdbcUrl();
        var userName = container.getUsername();
        var password = container.getPassword();

        dataSource.setUrl(url);
        dataSource.setUser(userName);
        dataSource.setPassword(password);

        System.out.println("JDBC URL: " + url +
                ", JDBC USER: " + userName +
                ", JDBC PASS: " + password);

        return dataSource;
    }
}
