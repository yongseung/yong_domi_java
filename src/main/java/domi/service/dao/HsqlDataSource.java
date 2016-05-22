package domi.service.dao;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;


@Profile("hsql")
@Configuration
public class HsqlDataSource {

    // jdbc:hsqldb:mem:testdb
    @Bean
    public DataSource dataSource() {

    	System.out.println("is it called?");
        // no need shutdown, EmbeddedDatabaseFactoryBean will take care of this
        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
        EmbeddedDatabase db = builder.setType(EmbeddedDatabaseType.HSQL).addScript("db/sql/create-db.sql")
                .addScript("db/sql/insert-data.sql").setName("MEETS_UP").build();
        return db;
    }
}