package phan.recipesite.config;

import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

// Allows spring to pick up configuration at boot time.
@Configuration
// Scans the package of the annotated configuration class for Spring Data repositories by default.
@EnableJpaRepositories(basePackages = "phan.recipesite.dao")
@EnableTransactionManagement
// Contains all hibernate properties.
@PropertySource("application.properties")
public class DataConfig {

    // Hibernate properties from application.properties are loaded into environment variable.
    @Autowired
    private Environment env;

    // FactoryBean that creates a JPA EntityManagerFactory according to JPA's standard container bootstrap contract.
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();

        factory.setDataSource(dataSource());
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setPackagesToScan(env.getProperty("recipesite.entity.package"));
        factory.setJpaProperties(getHibernateProperties());

        return factory;
    }

    // Configures a Custom DataSource
    // Spring Boot reuses DataSource anywhere one is required, including database initialization
//    @Bean
//    public DataSource dataSource() {
//        BasicDataSource ds = new BasicDataSource();
//        ds.setDriverClassName(env.getProperty("recipesite.datasource.driver-class-name"));
//        ds.setUrl(env.getProperty("recipesite.datasource.url"));
//        ds.setUsername(env.getProperty("recipesite.datasource.username"));
//        ds.setPassword(env.getProperty("recipesite.datasource.password"));
//
//        return ds;
//    }

    @Bean
    public DataSource dataSource() {
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName(env.getProperty("recipesite.db.driver"));
        ds.setUrl(env.getProperty("recipesite.db.url"));
        ds.setUsername(env.getProperty("recipesite.db.username"));
        ds.setPassword(env.getProperty("recipesite.db.password"));

        return ds;
    }

    // Specifies hibernate configuration properties
    private Properties getHibernateProperties() {
        Properties properties = new Properties();
        properties.put("hibernate.dialect", env.getProperty("hibernate.dialect"));
        properties.put("hibernate.implicit_naming_strategy", env.getProperty("hibernate.implicit_naming_strategy"));
        properties.put("hibernate.format_sql", env.getProperty("hibernate.format_sql"));
        properties.put("hibernate.show_sql", env.getProperty("hibernate.show_sql"));
        properties.put("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
        return properties;
    }
}
