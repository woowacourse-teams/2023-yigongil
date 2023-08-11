alter table member modify column github_id varchar(39);
alter table member modify column nickname varchar(8);
alter table member add column is_deleted boolean not null default false;
