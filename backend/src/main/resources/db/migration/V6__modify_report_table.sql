alter table report change column reported_id reported_member_id bigint;
alter table report add column reported_study_id bigint;
alter table report add column dtype varchar(31) not null after reported_study_id;

alter table report
   add constraint FK11x4rr3luavkymrl934b18c50
   foreign key (reported_study_id)
   references study (id);