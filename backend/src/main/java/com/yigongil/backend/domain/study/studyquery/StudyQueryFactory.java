package com.yigongil.backend.domain.study.studyquery;

import com.yigongil.backend.domain.study.ProcessingStatus;
import org.springframework.stereotype.Component;

@Component
public class StudyQueryFactory {

    private static final String QUERY_PREFIX = "select s from Study s ";

    public StudyQueryBuilder builder() {
        return new StudyQueryBuilder(QUERY_PREFIX);
    }

    public static class StudyQueryBuilder {

        private String query;
        private boolean hasCondition;

        public StudyQueryBuilder(String query) {
            this.query = query;
        }

        public StudyQueryBuilder search(String param) {
            if (param == null) {
                return this;
            }
            if (hasCondition) {
                query += "and s.name like '%" + param + "%' ";
                return this;
            }
            query += "where s.name like '%" + param + "%' ";
            hasCondition = true;
            return this;
        }

        public StudyQueryBuilder status(ProcessingStatus param) {
            if (param == null || param == ProcessingStatus.ALL) {
                return this;
            }
            if (hasCondition) {
                query += "and s.processingStatus = '" + param.name() + "' ";
                return this;
            }
            query += "where s.processingStatus = '" + param.name() + "' ";
            hasCondition = true;
            return this;
        }

        public StudyQueryBuilder sort(String param) {
            if (param != null) {
                query += String.format("order by s.%s ", param);
            }
            return this;
        }

        public String build() {
            return query;
        }
    }
}
