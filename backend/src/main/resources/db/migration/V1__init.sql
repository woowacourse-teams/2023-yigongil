create table member
(
    id                bigint auto_increment,
    created_at        timestamp    not null,
    updated_at        timestamp,
    github_id         varchar(255) not null,
    introduction      varchar(200),
    nickname          varchar(8),
    profile_image_url varchar(255),
    tier              integer      not null,
    primary key (id)
);

create table optional_todo
(
    id                 bigint auto_increment,
    created_at         timestamp   not null,
    updated_at         timestamp,
    content            varchar(20) not null,
    is_done            boolean     not null,
    round_of_member_id bigint,
    primary key (id)
);

create table round
(
    id                      bigint auto_increment,
    created_at              timestamp not null,
    updated_at              timestamp,
    end_at                  timestamp,
    necessary_to_do_content varchar(20),
    round_number            integer   not null,
    master_id               bigint    not null,
    study_id                bigint    not null,
    primary key (id)
);

create table round_of_member
(
    id         bigint auto_increment,
    created_at timestamp not null,
    updated_at timestamp,
    is_done    boolean   not null,
    member_id  bigint    not null,
    round_id   bigint    not null,
    primary key (id)
);

create table study
(
    id                        bigint auto_increment,
    created_at                timestamp    not null,
    updated_at                timestamp,
    end_at                    timestamp,
    introduction              varchar(200) not null,
    name                      varchar(30)  not null,
    number_of_maximum_members integer      not null,
    period_of_round           integer      not null,
    period_unit               varchar(255) not null,
    processing_status         varchar(255) not null,
    start_at                  timestamp    not null,
    total_round_count         integer      not null,
    current_round_id          bigint,
    primary key (id)
);

create table study_member
(
    id           bigint auto_increment,
    created_at   timestamp    not null,
    updated_at   timestamp,
    role         varchar(255) not null,
    study_result varchar(255) not null,
    member_id    bigint       not null,
    study_id     bigint       not null,
    primary key (id)
);

alter table member
    add constraint UK_6p926dfj54l7npjasf3kx2uxm unique (github_id);

alter table member
    add constraint UK_hh9kg6jti4n1eoiertn2k6qsc unique (nickname);

alter table optional_todo
    add constraint FKjnishr4h1qlmtna5q0a2b40dd
        foreign key (round_of_member_id)
            references round_of_member;

alter table round
    add constraint FKou20hom4iifhu8hqd4msvwsq0
        foreign key (master_id)
            references member;

alter table round
    add constraint FK41ah5maxtjcdgiohmdr2s9fai
        foreign key (study_id)
            references study;

alter table round_of_member
    add constraint FKmcjff70sqtklcu6oxrbrgr9c1
        foreign key (member_id)
            references member;

alter table round_of_member
    add constraint FK7io91u1fjsmn88fjpe6ky3mc0
        foreign key (round_id)
            references round;

alter table study
    add constraint FK67dn3mpd4083de7434r7k5xa9
        foreign key (current_round_id)
            references round;

alter table study_member
    add constraint FKf2jvkah9v99o0ujl7ilpshptk
        foreign key (member_id)
            references member;

alter table study_member
    add constraint FKxu4jds4ab0mfyrvdxsu60iut
        foreign key (study_id)
            references study;
