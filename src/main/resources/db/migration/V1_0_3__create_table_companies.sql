create table if not exists companies
(
    id_company      BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name_company    VARCHAR(20),
    address_company VARCHAR(100),
    CONSTRAINT pk_companies PRIMARY KEY (id_company)
);