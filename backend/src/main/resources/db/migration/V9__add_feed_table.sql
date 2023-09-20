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
);

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
);

alter table certification
    add constraint FKh0ll51ka0lhk94pdlikxe1q3g
        foreign key (author_id)
            references member (id);
alter table certification
    add constraint FK7lfejs9o853gnlj9i8l4h4mmf
        foreign key (round_id)
            references round (id);

alter table certification
    add constraint FK57fjgx69mxhlhcsjs9k33ea68
        foreign key (study_id)
            references study (id);
alter table feed_post
    add constraint FKl069x26g7tvis9mwqxqbojeg2
        foreign key (author_id)
            references member (id);
alter table feed_post
    add constraint FKk0xlj43k7k3ed8t8pdhq6web0
        foreign key (study_id)
            references study (id);
