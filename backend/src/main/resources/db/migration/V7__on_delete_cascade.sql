alter table study
    drop constraint FK67dn3mpd4083de7434r7k5xa9;

alter table study
    add column current_round_number int not null default 1;

update study
set current_round_number =
        (select round_number
         from round
                  join study on round.id = study.current_round_id);

alter table study
    drop column current_round_id;

alter table optional_todo
    drop constraint FKjnishr4h1qsdfa5q0a2b40dd;

alter table optional_todo
    add constraint FKjnishr4h1qsdfa5q0a2b40dd
        foreign key (round_of_member_id)
            references round_of_member (id)
            on delete cascade;

alter table round
    drop constraint FK41ah5maxtjcdgiohmdr2s9fai;

alter table round
    add constraint FK41ah5maxtsdfiohmdr2s9fai
        foreign key (study_id)
            references study (id)
            on delete cascade;

alter table round_of_member
    drop constraint FK7io91u1fjsmn88fjpe6ky3mc0;

alter table round_of_member
    add constraint FK7io91u1fjxbn88fjpe6ky3mc0
        foreign key (round_id)
            references round (id)
            on delete cascade;

alter table study_member
    drop constraint FKxu4jds4ab0mfyrvdxsu60iut;

alter table study_member
    add constraint FKxu4jds4ab0mvbbrvdxsu60iut
        foreign key (study_id)
            references study (id)
            on delete cascade;

