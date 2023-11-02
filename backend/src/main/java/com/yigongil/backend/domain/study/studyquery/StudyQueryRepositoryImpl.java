package com.yigongil.backend.domain.study.studyquery;

import static com.yigongil.backend.domain.member.QMember.member;
import static com.yigongil.backend.domain.study.QStudy.study;
import static com.yigongil.backend.domain.studymember.QStudyMember.studyMember;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yigongil.backend.domain.study.ProcessingStatus;
import com.yigongil.backend.domain.study.Study;
import com.yigongil.backend.domain.studymember.Role;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Repository;

@Repository
public class StudyQueryRepositoryImpl implements StudyQueryRepository {

    private final JPAQueryFactory queryFactory;

    public StudyQueryRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public Slice<Study> findStudiesByConditions(String search, ProcessingStatus status, Pageable page) {

        List<Long> studyIds = queryFactory.select(study.id)
                                          .from(study)
                                          .where(searchEq(search), statusEq(status))
                                          .offset(page.getOffset())
                                          .limit(page.getPageSize())
                                          .orderBy(orderSpecifier(page))
                                          .fetch();

        List<Study> studies = queryFactory.selectFrom(study)
                                          .join(study.studyMembers, studyMember).fetchJoin()
                                          .join(studyMember.member, member).fetchJoin()
                                          .where(study.id.in(studyIds))
                                          .orderBy(orderSpecifier(page))
                                          .fetch();
        return new SliceImpl<>(studies);
    }

    @Override
    public Slice<Study> findWaitingStudies(Long memberId, String search, Role role, Pageable page) {
        List<Study> studies = queryFactory.select(studyMember.study)
                                          .from(studyMember)
                                          .join(studyMember.study, study)
                                          .where(
                                                  studyMember.role.eq(role),
                                                  studyMember.member.id.eq(memberId),
                                                  searchEq(search),
                                                  study.processingStatus.eq(ProcessingStatus.RECRUITING)
                                          )
                                          .offset(page.getOffset())
                                          .limit(page.getPageSize())
                                          .orderBy(orderSpecifier(page))
                                          .fetch();

        return new SliceImpl<>(studies);
    }

    private BooleanExpression searchEq(String search) {
        if (search == null) {
            return null;
        }
        return study.name.contains(search);
    }

    private Predicate statusEq(ProcessingStatus status) {
        if (status == null || status == ProcessingStatus.ALL) {
            return null;
        }
        return study.processingStatus.eq(status);
    }

    private OrderSpecifier<?>[] orderSpecifier(Pageable page) {
        Sort sort = page.getSort();
        PathBuilder<Study> pathBuilder = new PathBuilder<>(study.getType(), study.getMetadata());

        List<OrderSpecifier<?>> result = new ArrayList<>();
        for (Order order : sort) {
            if (order.isAscending()) {
                result.add(pathBuilder.getString(order.getProperty()).asc());
            } else {
                result.add(pathBuilder.getString(order.getProperty()).desc());
            }
        }

        return result.toArray(new OrderSpecifier[result.size()]);
    }
}
