alter table member
    change column is_deleted deleted boolean not null default false;
