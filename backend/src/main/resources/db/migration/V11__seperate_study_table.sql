ALTER TABLE round
    ADD COLUMN week_number integer;
ALTER TABLE study
    ADD COLUMN version integer NOT NULL DEFAULT 1;

create table progress_day_of_week
(
    study_id             bigint  not null,
    progress_day_of_week integer not null,
    primary key (study_id, progress_day_of_week)
);

CREATE TABLE studyv1
(
    id                bigint       NOT NULL,
    expected_start_at timestamp    NOT NULL,
    period_of_round   integer      NOT NULL,
    period_unit       varchar(255) NOT NULL,
    total_round_count integer      NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE studyv2
(
    id            bigint  NOT NULL,
    minimum_weeks integer NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE studyv1
    ADD CONSTRAINT FKccv3gbwk8mv1btfyp1ex5033k
        FOREIGN KEY (id) REFERENCES study(id);
ALTER TABLE studyv2
    ADD CONSTRAINT FKo01e2qny9obrbctadrcnsxetk
        FOREIGN KEY (id) REFERENCES study(id);
ALTER TABLE progress_day_of_week
    ADD CONSTRAINT FKbto00gu5esijcq0wtn13fd7pd
        FOREIGN KEY (study_id) REFERENCES studyv2(id);

INSERT INTO studyv1 (id, period_of_round, period_unit, total_round_count)
SELECT id, period_of_round, period_unit, total_round_count FROM study;

ALTER TABLE study DROP COLUMN period_of_round;
ALTER TABLE study DROP COLUMN period_unit;
ALTER TABLE study DROP COLUMN total_round_count;
