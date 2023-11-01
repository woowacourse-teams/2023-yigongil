ALTER TABLE round ADD day_of_week INTEGER;

UPDATE round
    INNER JOIN meeting_day_of_the_week
    ON round.meeting_day_of_the_week_id = meeting_day_of_the_week.id
SET round.day_of_week = meeting_day_of_the_week.day_of_week;

alter table round drop foreign key FKp9ga9uq8uvomn6vytcr34vfl5;
ALTER TABLE round DROP COLUMN meeting_day_of_the_week_id;

