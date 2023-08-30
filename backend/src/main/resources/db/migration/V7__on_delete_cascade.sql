alter table study
    drop column current_round_id;

alter table study
    add column current_round_number int not null default 1;

alter table optional_todo
    add constraint FKjnishr4h1qlmtna5q0a2b40dd
        foreign key (round_of_member_id)
            references round_of_member (id)
            on delete cascade;

alter table round
    add constraint FK41ah5maxtjcdgiohmdr2s9fai
        foreign key (study_id)
            references study (id)
            on delete cascade;

alter table round_of_member
    add constraint FK7io91u1fjsmn88fjpe6ky3mc0
        foreign key (round_id)
            references round (id)
            on delete cascade;

alter table study_member
    add constraint FKxu4jds4ab0mfyrvdxsu60iut
        foreign key (study_id)
            references study (id)
            on delete cascade;
