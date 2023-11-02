alter table certification
    modify column content varchar(1000) not null;

alter table feed_post
    modify column content varchar(1000) not null;
