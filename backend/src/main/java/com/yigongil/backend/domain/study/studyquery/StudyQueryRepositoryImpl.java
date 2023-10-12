package com.yigongil.backend.domain.study.studyquery;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yigongil.backend.domain.study.ProcessingStatus;
import com.yigongil.backend.domain.study.Study;
import com.yigongil.backend.domain.studymember.Role;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

import static com.yigongil.backend.domain.study.QStudy.study;
import static com.yigongil.backend.domain.studymember.QStudyMember.studyMember;

@Repository
public class StudyQueryRepositoryImpl implements StudyQueryRepository {

    private final EntityManager entityManager;
    private final StudyQueryFactory studyQueryFactory;

    public StudyQueryRepositoryImpl(EntityManager entityManager, StudyQueryFactory studyQueryFactory) {
        this.entityManager = entityManager;
        this.studyQueryFactory = studyQueryFactory;
    }

    @Override
    public Slice<Study> findStudiesByConditions(String search, ProcessingStatus status, Pageable page) {
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

    public Slice<Study> findStudiesApplied(Long memberId, String search, Role role, Pageable page) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);

        List<Study> studies = queryFactory.select(studyMember.study)
                .from(studyMember)
                .join(studyMember.study, study)
                .where(studyMember.role.eq(role),
                        studyMember.member.id.eq(memberId),
                        searchEq(search)
                )
                .offset(page.getOffset())
                .limit(page.getPageSize())
                .fetch();

        return new SliceImpl<>(studies);
    }

    private BooleanExpression searchEq(String search) {
        if (search == null) {
            return null;
        }
        return study.name.contains(search);
    }
}
