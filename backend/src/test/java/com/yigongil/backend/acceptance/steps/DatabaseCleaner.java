package com.yigongil.backend.acceptance.steps;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.metamodel.EntityType;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Profile("test")
@Component
public class DatabaseCleaner implements InitializingBean {

    @PersistenceContext
    private EntityManager entityManager;

    private List<String> tables;

    @Override
    public void afterPropertiesSet() {
        this.tables = entityManager.getMetamodel().getEntities().stream()
                                   .map(EntityType::getName)
                                   .map(this::toSnake)
                                   .toList();
        System.out.println("tables = " + tables);
    }

    private String toSnake(String camel) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < camel.length(); i++) {
            char c = camel.charAt(i);
            if (c >= 'A' && c <= 'Z' && i != 0) {
                sb.append('_');
            }
            sb.append(c);
        }
        return sb.toString().toLowerCase();
    }

    @Transactional
    public void clean() {
        entityManager.createNativeQuery("set foreign_key_checks = 0").executeUpdate();

        for (String table : tables) {
            entityManager.createNativeQuery("truncate table " + table).executeUpdate();
        }

        entityManager.createNativeQuery("set foreign_key_checks = 1").executeUpdate();
    }
}
