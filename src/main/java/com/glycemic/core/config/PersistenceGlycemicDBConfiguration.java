package com.glycemic.core.config;


import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
@PropertySource({"classpath:persistence-multiple-db.properties"})
@EnableJpaRepositories(basePackages = "com.glycemic.entity.repository.glycemic", entityManagerFactoryRef = "entityManager", transactionManagerRef = "transactionManager")
@RequiredArgsConstructor
public class PersistenceGlycemicDBConfiguration {

    private final Environment env;

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManager() {
        final LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();

        em.setDataSource(dataSource());
        em.setPackagesToScan("com.glycemic.entity.model.glycemic");

        final HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);

        final HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", env.getProperty("spring.jpa.properties.hibernate.hbm2ddl.auto"));
        properties.put("hibernate.dialect", env.getProperty("spring.jpa.properties.hibernate.dialect"));
        properties.put("hibernate.physical_naming_strategy", env.getProperty("spring.jpa.properties.hibernate.naming.physical-strategy"));
        properties.put("hibernate.connection.characterEncoding", env.getProperty("spring.jpa.properties.hibernate.connection.characterEncoding"));
        properties.put("hibernate.connection.CharSet", env.getProperty("spring.jpa.properties.hibernate.connection.CharSet"));
        properties.put("hibernate.connection.useUnicode", env.getProperty("spring.jpa.properties.hibernate.connection.useUnicode"));
        properties.put("hibernate.connection.collationConnection", env.getProperty("spring.jpa.properties.hibernate.connection.collationConnection"));
        em.setJpaPropertyMap(properties);

        return em;
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        final JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManager().getObject());

        return transactionManager;
    }

    @Bean
    public DataSourceInitializer dataSourceInit() {
        ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator();
        databasePopulator.addScript(new ClassPathResource("data-glycemic.sql"));

        DataSourceInitializer initializer = new DataSourceInitializer();
        initializer.setDatabasePopulator(databasePopulator);
        initializer.setDataSource(dataSource());
        initializer.afterPropertiesSet();

        return initializer;
    }
}
