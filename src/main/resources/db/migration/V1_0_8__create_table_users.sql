create table if not exists meeting_user
(
    id_meeting BIGINT NOT NULL,
    id_user    BIGINT NOT NULL,
    CONSTRAINT pk_meeting_user PRIMARY KEY (id_meeting, id_user)
    );

ALTER TABLE meeting_user
    ADD CONSTRAINT fk_meeuse_on_meeting FOREIGN KEY (id_meeting) REFERENCES meetings (id_meeting);

ALTER TABLE meeting_user
    ADD CONSTRAINT fk_meeuse_on_user FOREIGN KEY (id_user) REFERENCES users (id_user);