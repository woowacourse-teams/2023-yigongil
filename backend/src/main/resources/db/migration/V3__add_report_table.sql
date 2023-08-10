create table report (
    id                   bigint auto_increment,
    created_at           timestamp not null,
    updated_at           timestamp,
    content              varchar(200) not null,
    problem_occured_at   datetime not null,
    title                varchar(30) not null,
    reported_id          bigint,
    reporter_id          bigint,
    primary key (id)
);

alter table report
   add constraint FK6ovdlwgf174uw16m9cynvbgal
      foreign key (reported_id)
         references member(id);

alter table report
   add constraint FK1uivt2jamt7slp3banldgnsef
      foreign key (reporter_id)
         references member(id);
