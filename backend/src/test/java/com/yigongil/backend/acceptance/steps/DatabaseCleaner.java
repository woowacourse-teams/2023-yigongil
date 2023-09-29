package com.yigongil.backend.acceptance.steps;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
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

    private List<String> tables = new ArrayList<>();

    @PostConstruct
    public void init() {
        List<? extends Class<?>> entities = entityManager.getMetamodel().getEntities().stream()
                                                         .map(EntityType::getJavaType)
                                                         .toList();
        for (Class<?> entity : entities) {
            if (entity.getSuperclass().isAnnotationPresent(Inheritance.class)) {
                Inheritance inheritance = entity.getSuperclass().getAnnotation(Inheritance.class);
                InheritanceType strategy = inheritance.strategy();
                if (strategy == InheritanceType.SINGLE_TABLE) {
                    continue;
                }
                tables.add(toSnake(entity.getSimpleName()));
                continue;
            }
            tables.add(toSnake(entity.getSimpleName()));
        }
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
