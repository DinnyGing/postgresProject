create table if not exists meeting_speaker
(
    id_meeting BIGINT NOT NULL,
    id_speaker BIGINT NOT NULL,
    CONSTRAINT pk_meeting_speaker PRIMARY KEY (id_meeting, id_speaker)
);

ALTER TABLE meeting_speaker
    ADD CONSTRAINT fk_meespe_on_meeting FOREIGN KEY (id_meeting) REFERENCES meetings (id_meeting);

ALTER TABLE meeting_speaker
    ADD CONSTRAINT fk_meespe_on_speaker FOREIGN KEY (id_speaker) REFERENCES speakers (id_speaker);