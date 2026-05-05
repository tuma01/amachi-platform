package com.amachi.app.vitalia.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;

// spring-data-envers is on the classpath for version-managed hibernate-envers,
// but we don't use RevisionRepository. Force the standard JPA factory to prevent
// EnversRevisionRepositoryFactoryBean from loading entity classes by simple name,
// which causes ClassNotFoundException: User at startup.
@SpringBootApplication(scanBasePackages = "com.amachi.app")
@EntityScan(basePackages = "com.amachi.app")
@EnableJpaRepositories(basePackages = "com.amachi.app", repositoryFactoryBeanClass = JpaRepositoryFactoryBean.class)
@EnableAspectJAutoProxy
public class VitaliaApplication {

    public static void main(String[] args) {
        SpringApplication.run(VitaliaApplication.class, args);
    }
}
