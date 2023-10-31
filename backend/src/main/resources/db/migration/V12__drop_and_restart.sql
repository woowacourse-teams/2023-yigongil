set foreign_key_checks = 0;
drop table certification;
drop table feed_post;
drop table report;
drop table round;
drop table round_of_member;
drop table study;
drop table study_member;
set foreign_key_checks = 1;

create table certification
(
    id         bigint   not null auto_increment,
    created_at datetime not null,
    updated_at datetime,
    content    varchar(255),
    image_url  varchar(255),
    author_id  bigint,
    round_id   bigint,
    study_id   bigint,
    primary key (id)
) engine = InnoDB
;

create table feed_post
(
    id         bigint   not null auto_increment,
    created_at datetime not null,
    updated_at datetime,
    content    varchar(255),
    image_url  varchar(255),
    author_id  bigint,
    study_id   bigint,
    primary key (id)
) engine = InnoDB
;

create table meeting_day_of_the_week
(
    id          bigint   not null auto_increment,
    created_at  datetime not null,
    updated_at  datetime,
    day_of_week integer,
    study_id    bigint,
    primary key (id)
) engine = InnoDB
;

create table report
(
    dtype               varchar(31)  not null,
    id                  bigint       not null auto_increment,
    created_at          datetime     not null,
    updated_at          datetime,
    content             varchar(200) not null,
    problem_occurred_at date         not null,
    title               varchar(30)  not null,
    reporter_id         bigint,
    reported_member_id  bigint,
    reported_study_id   bigint,
    primary key (id)
) engine = InnoDB
;

create table round
(
    id                         bigint   not null auto_increment,
    created_at                 datetime not null,
    updated_at                 datetime,
    must_do                    varchar(20),
    round_status               varchar(255),
    week_number                integer  not null,
    master_id                  bigint   not null,
    meeting_day_of_the_week_id bigint,
    study_id                   bigint   not null,
    primary key (id)
) engine = InnoDB
;

create table round_of_member
(
    id         bigint   not null auto_increment,
    created_at datetime not null,
    updated_at datetime,
    is_done    bit      not null,
    member_id  bigint   not null,
    round_id   bigint   not null,
    primary key (id)
) engine = InnoDB
;

create table study
(
    id                          bigint       not null auto_increment,
    created_at                  datetime     not null,
    updated_at                  datetime,
    current_round_number        bigint,
    end_at                      datetime,
    introduction                varchar(200) not null,
    meeting_days_count_per_week integer      not null,
    minimum_weeks               integer      not null,
    name                        varchar(30)  not null,
    number_of_maximum_members   integer      not null,
    processing_status           varchar(255) not null,
    primary key (id)
) engine = InnoDB
;

create table study_member
(
    id           bigint       not null auto_increment,
    created_at   datetime     not null,
    updated_at   datetime,
    role         varchar(255) not null,
    study_result varchar(255) not null,
    member_id    bigint       not null,
    study_id     bigint       not null,
    primary key (id)
) engine = InnoDB
;

alter table member
    drop index UK_6p926dfj54l7npjasf3kx2uxm
;

alter table member
    add constraint UK_6p926dfj54l7npjasf3kx2uxm unique (github_id)
;

alter table member
    drop index UK_hh9kg6jti4n1eoiertn2k6qsc
;

alter table member
    add constraint UK_hh9kg6jti4n1eoiertn2k6qsc unique (nickname)
;

alter table certification
    add constraint FKh0ll51ka0lhk94pdlikxe1q3g
        foreign key (author_id)
            references member (id)
;

alter table certification
    add constraint FK7lfejs9o853gnlj9i8l4h4mmf
        foreign key (round_id)
            references round (id)
;

alter table certification
    add constraint FK57fjgx69mxhlhcsjs9k33ea68
        foreign key (study_id)
            references study (id)
;

alter table feed_post
    add constraint FKl069x26g7tvis9mwqxqbojeg2
        foreign key (author_id)
            references member (id)
;

alter table feed_post
    add constraint FKk0xlj43k7k3ed8t8pdhq6web0
        foreign key (study_id)
            references study (id)
;

alter table meeting_day_of_the_week
    add constraint FK5h400grf7j2bovonh2xyktgdq
        foreign key (study_id)
            references study (id)
;

alter table report
    add constraint FK1uivt2jamt7slp3banldgnsef
        foreign key (reporter_id)
            references member (id)
;

alter table report
    add constraint FK3f6hlh2lko9py15mptqpwllkx
        foreign key (reported_member_id)
            references member (id)
;

alter table report
    add constraint FK11x4rr3luavkymrl934b18c50
        foreign key (reported_study_id)
            references study (id)
;

alter table round
    add constraint FKou20hom4iifhu8hqd4msvwsq0
        foreign key (master_id)
            references member (id)
;

alter table round
    add constraint FKp9ga9uq8uvomn6vytcr34vfl5
        foreign key (meeting_day_of_the_week_id)
            references meeting_day_of_the_week (id)
;

alter table round
    add constraint FK41ah5maxtjcdgiohmdr2s9fai
        foreign key (study_id)
            references study (id)
            on delete cascade
;

alter table round_of_member
    add constraint FKmcjff70sqtklcu6oxrbrgr9c1
        foreign key (member_id)
            references member (id)
;

alter table round_of_member
    add constraint FK7io91u1fjsmn88fjpe6ky3mc0
        foreign key (round_id)
            references round (id)
            on delete cascade
;

alter table study_member
    add constraint FKf2jvkah9v99o0ujl7ilpshptk
        foreign key (member_id)
            references member (id)
;

alter table study_member
    add constraint FKxu4jds4ab0mfyrvdxsu60iut
        foreign key (study_id)
            references study (id)
            on delete cascade;
