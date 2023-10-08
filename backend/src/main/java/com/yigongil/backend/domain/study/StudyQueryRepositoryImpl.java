package com.yigongil.backend.domain.study;

import java.util.List;
import javax.persistence.EntityManager;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

@Repository
public class StudyQueryRepositoryImpl implements StudyQueryRepository {

    private final EntityManager entityManager;
    private final StudyQueryFactory studyQueryFactory;

    public StudyQueryRepositoryImpl(EntityManager entityManager, StudyQueryFactory studyQueryFactory) {
        this.entityManager = entityManager;
        this.studyQueryFactory = studyQueryFactory;
    }

    @Override
    public Slice<Study> findStudiesByConditions(String search, String status, Pageable page) {
        String sort = String.join(" ", page.getSort().toString().split(": "));

        String jpql = studyQueryFactory.builder()
                                       .search(search)
                                       .status(status)
                                       .sort(sort)
                                       .build();

        List<Study> result = entityManager.createQuery(jpql, Study.class)
                                          .setFirstResult((int) page.getOffset())
                                          .setMaxResults(page.getPageSize())
                                          .getResultList();

        return new SliceImpl<>(result);
    }
}
