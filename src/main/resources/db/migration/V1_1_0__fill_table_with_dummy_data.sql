CREATE EXTENSION pgcrypto;

INSERT INTO COMPANIES (NAME_COMPANY, ADDRESS_COMPANY) VALUES
('Google', 'Mountain View, California'),
('Apple', 'Cupertino, California, United States'),
('Microsoft', 'Redmond, Washington, United States'),
('Facebook', 'Hacker Way, Menlo Park'),
('Samsung', 'Samsung Town, Seoul, South Korea'),
('Amazon', 'Amazon Headquarters 410 Terry Ave'),
('Intel', 'Santa Clara, California'),
('Netflix', 'Los Gatos, California'),
('IBM', 'Armonk, New York'),
('General Electric', 'Boston, Massachusetts, U.S');

INSERT INTO ROLES (NAME_ROLE) VALUES ('reader'), ('editor');

INSERT INTO STATUSES (NAME_STATUS) VALUES ('online'), ('offline');

INSERT INTO COUNTRIES (NAME_COUNTRY) VALUES ('Ukraine'), ('Spain'), ('England'), ('Poland'),
('Norway'), ('German'), ('USA'), ('Canada'), ('Turkey'), ('Italy'), ('Brazil'), ('Georgia'),
('Israel'), ('Scotland'), ('Ireland'), ('Latvia'), ('Sweden'), ('Japan'), ('Portugal'), ('France'),
('Australia'), ('China');

INSERT INTO USERS (NAME_USER, LOGIN_USER, PASSWORD_USER, ID_COMPANY, ID_ROLE)
VALUES('Dinny', 'Dinny_admin', 'admin', 9, 2)
