package com.yigongil.backend.acceptance.steps;

import java.util.List;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.metamodel.EntityType;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Profile("test")
@Component
public class DatabaseCleaner {

    @PersistenceContext
    private EntityManager entityManager;

    private List<String> tables;

    @PostConstruct
    public void init() {
        this.tables = entityManager.getMetamodel().getEntities().stream()
                                   .filter(entityType -> entityType.getJavaType().getSuperclass()
                                                                   .getSimpleName()
                                                                   .equals("BaseEntity")
                                   || entityType.getJavaType().getSuperclass()
                                                .getSimpleName()
                                                .equals("BaseRootEntity"))
                                   .map(EntityType::getName)
                                   .map(this::toSnake)
                                   .toList();
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
        entityManager.createNativeQuery("truncate table day_of_week").executeUpdate();

        entityManager.createNativeQuery("set foreign_key_checks = 1").executeUpdate();
    }
}
