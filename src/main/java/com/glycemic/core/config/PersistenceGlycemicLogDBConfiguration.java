package com.glycemic.core.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
@PropertySource({"classpath:persistence-multiple-db.properties"})
@EnableJpaRepositories(basePackages = "com.glycemic.entity.repository.glycemiclog", entityManagerFactoryRef = "logEntityManager", transactionManagerRef = "logTransactionManager")
@RequiredArgsConstructor
public class PersistenceGlycemicLogDBConfiguration {

    private final Environment env;

    @Bean
    public LocalContainerEntityManagerFactoryBean logEntityManager() {
        final LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(logDataSource());
        em.setPackagesToScan("com.glycemic.entity.model.glycemiclog");

        final HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        final HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", env.getProperty("spring.jpa.properties.hibernate.hbm2ddl.auto"));
        properties.put("hibernate.dialect", env.getProperty("spring.jpa.properties.hibernate.dialect"));
        properties.put("hibernate.physical_naming_strategy",env.getProperty("spring.jpa.properties.hibernate.naming.physical-strategy"));
        properties.put("hibernate.connection.characterEncoding", env.getProperty("spring.jpa.properties.hibernate.connection.characterEncoding"));
        properties.put("hibernate.connection.CharSet", env.getProperty("spring.jpa.properties.hibernate.connection.CharSet"));
        properties.put("hibernate.connection.useUnicode", env.getProperty("spring.jpa.properties.hibernate.connection.useUnicode"));
        properties.put("hibernate.connection.collationConnection", env.getProperty("spring.jpa.properties.hibernate.connection.collationConnection"));
        em.setJpaPropertyMap(properties);

        return em;
    }

    @Bean
    @ConfigurationProperties(prefix="spring.second-datasource")
    public DataSource logDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    public PlatformTransactionManager logTransactionManager() {
        final JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(logEntityManager().getObject());
        return transactionManager;
    }
}
