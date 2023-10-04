ALTER TABLE round
    ADD COLUMN week_number integer;
ALTER TABLE study
    ADD COLUMN minimum_weeks integer NOT NULL;

create table progress_day_of_week
(
    study_id             bigint  not null,
    progress_day_of_week integer not null,
    primary key (study_id, progress_day_of_week)
);

ALTER TABLE progress_day_of_week
    ADD CONSTRAINT FKbto00gu5esijcq0wtn13fd7pd
        FOREIGN KEY (study_id) REFERENCES study(id);

ALTER TABLE study DROP COLUMN period_of_round;
ALTER TABLE study DROP COLUMN period_unit;
ALTER TABLE study DROP COLUMN total_round_count;
