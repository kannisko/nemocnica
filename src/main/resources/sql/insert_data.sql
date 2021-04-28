ALTER SESSION SET NLS_DATE_FORMAT = "DD/MM/YYYY";

ALTER TABLE DOCTORS DISABLE CONSTRAINT FK_workdepartment;
INSERT INTO DOCTORS VALUES (NULL, 'Marian', 'Kowalski', 'Ortopeda', 'Specjalista', NULL, 1, 8000);
INSERT INTO DOCTORS VALUES (NULL, 'Krzysztof', 'Bosak', 'Ortopeda', 'Sta¿ysta', 1, 1, 2600);
INSERT INTO DOCTORS VALUES (NULL, 'Adam', 'Kowalski', 'Chirurg', 'Specjalista', 1, 1, 9000);
INSERT INTO DOCTORS VALUES (NULL, 'Adam', 'Dziambor', 'Ortopeda', 'Specjalista', 1, 1, 12000);
INSERT INTO DOCTORS VALUES (NULL, 'Jaroslaw', 'Kaczynski', 'Kardiolog', 'Specjalista', NULL, 2, 14000);
INSERT INTO DOCTORS VALUES (NULL, 'Adam', 'Bielan', 'Anestezjolog', 'Sta¿ysta', 5, 2, 8000);
INSERT INTO DOCTORS VALUES (NULL, 'Andrzej', 'Duda', 'Kardiolog', 'Rezydent', 5, 2, 4000);
INSERT INTO DOCTORS VALUES (NULL, 'Mateusz', 'Morawiecki', 'Kardiolog', 'Specjalista', 5, 2, 6000);
INSERT INTO DOCTORS VALUES (NULL, 'Borys', 'Budka', 'Internista', 'Specjalista', NULL, 3, 13500);
INSERT INTO DOCTORS VALUES (NULL, 'Mateusz', 'Panowicz', 'Kardiolog', 'Specjalista', 9, 3, 11000);
INSERT INTO DOCTORS VALUES (NULL, 'Antoni', 'Macierewicz', 'Pulmunolog', 'Rezydent', 9, 3, 10000);
INSERT INTO DOCTORS VALUES (NULL, 'Barbara', 'Nowak', 'Internista', 'Specjalista', NULL, 4, 16000);
commit;

ALTER TABLE NURSES DISABLE CONSTRAINT FK_nurse_workdepartment;

INSERT INTO NURSES VALUES (NULL, 'Alek', 'Kowalski', 'Pielêgniarz', NULL, 1, 6000);
INSERT INTO NURSES VALUES (NULL, 'Alek', 'Kowal', 'Pielêgniarz', 1, 1, 4000);
INSERT INTO NURSES VALUES (NULL, 'Aleksandra', 'Kowa', 'Pielêgniarka', 1, 1, 4500);
INSERT INTO NURSES VALUES (NULL, 'Aneta', 'Klep', 'Pielêgniarka', NULL, 2, 6500);
INSERT INTO NURSES VALUES (NULL, 'Janina', 'Tomkowicz', 'Pielêgniarka', 4, 2, 4000);
INSERT INTO NURSES VALUES (NULL, 'Nina', 'Omkowicz', 'Anestetyczka', 4, 2, 4000);
INSERT INTO NURSES VALUES (NULL, 'Ewa', 'Kowicz', 'Pielêgniarka', 7, 3, 4200);
INSERT INTO NURSES VALUES (NULL, 'Dorota', 'Omko', 'Pielêgniarka', 7, 3, 2400);
INSERT INTO NURSES VALUES (NULL, 'Aleksander', 'Kwasniewski', 'Polo¿ny', NULL, 4, 4500);
INSERT INTO NURSES VALUES (NULL, 'Daria', 'Omkos', 'Polozna', 9, 4, 3500);
commit;



INSERT INTO emergency_center VALUES (NULL, 1, 2);
INSERT INTO emergency_center VALUES (NULL, 2, 1);
INSERT INTO emergency_center VALUES (NULL, 3, 4);
INSERT INTO emergency_center VALUES (NULL, 8, 7);
commit;

INSERT INTO DEPARTMENTS VALUES (NULL, 'Ortopedia', 1, 1, '1000000');
INSERT INTO DEPARTMENTS VALUES (NULL, 'Kardiologia', 5, 5, '2000000');
INSERT INTO DEPARTMENTS VALUES (NULL, 'Interna', 9, 7, '500000');
INSERT INTO DEPARTMENTS VALUES (NULL, 'SOR', 11, 9, '100000');
commit;

INSERT INTO HOSPITAL_ROOMS VALUES (NULL, 1, 'Telewizor');
INSERT INTO HOSPITAL_ROOMS VALUES (NULL, 1, 'Telewizor');
INSERT INTO HOSPITAL_ROOMS VALUES (NULL, 1, NULL);
INSERT INTO HOSPITAL_ROOMS VALUES (NULL, 1, 'Telewizor');
INSERT INTO HOSPITAL_ROOMS VALUES (NULL, 2, 'Telewizor');
INSERT INTO HOSPITAL_ROOMS VALUES (NULL, 2, NULL);
INSERT INTO HOSPITAL_ROOMS VALUES (NULL, 3, 'Klimatyzacja');
INSERT INTO HOSPITAL_ROOMS VALUES (NULL, 3, 'Telewizor');
INSERT INTO HOSPITAL_ROOMS VALUES (NULL, 4, NULL);
commit;

INSERT INTO PATIENTS VALUES (NULL, '04/05/2020', 1, 1, 1, 1);
INSERT INTO PATIENTS VALUES (NULL, '04/09/2020', 2, 1, 2, 2);
INSERT INTO PATIENTS VALUES (NULL, '14/09/2020', 1, 1, 2, 3);
INSERT INTO PATIENTS VALUES (NULL, '24/09/2020', 3, 1, 2, 3);
INSERT INTO PATIENTS VALUES (NULL, '24/11/2020', 4, 1, 2, 4);
INSERT INTO PATIENTS VALUES (NULL, '24/12/2020', 4, 2, 2, 5);
INSERT INTO PATIENTS VALUES (NULL, '04/12/2020', 11, 3, 9, 7);
INSERT INTO PATIENTS VALUES (NULL, '24/12/2020', 11, 2, 9, 6);
commit;

INSERT INTO DOCUMENTATION VALUES (1, 'Marian', 'Wodor', '62081133444', 'Zlamanie piszczela');
INSERT INTO DOCUMENTATION VALUES (2, 'Marian', 'Stal', '63081133444', 'Zlamanie palca');
INSERT INTO DOCUMENTATION VALUES (3, 'Marian', 'Braz', '64081133444', 'Zlamanie zebra');
INSERT INTO DOCUMENTATION VALUES (4, 'Marian', 'Zloto', '65081133444', 'Zlamanie serca');
INSERT INTO DOCUMENTATION VALUES (5, 'Marian', 'Srebro', '66081133444', 'Zlamanie palca');
INSERT INTO DOCUMENTATION VALUES (6, 'Marian', 'Platyna', '67081133444', 'Nadcisnienie');
INSERT INTO DOCUMENTATION VALUES (7, 'Marian', 'Zloto', '65081133444', 'Zapalenie trzustki');
INSERT INTO DOCUMENTATION VALUES (8, 'Marian', 'Srebro', '66081133444', 'Podejrzenie zawalu serca');
commit;

insert into operating_theatre values (NULL, 7, 'Przeszczep trzustki', 9, 'Stabilny','25/01/2021');
insert into operating_theatre values (NULL, 8, 'Wstawienie stentow', 4, 'Stabilny', '22/01/2021');
insert into operating_theatre values (NULL, 3, 'Rekonstrukcja zeber', 2, 'Zagrozony', '21/01/2021');


ALTER TABLE DOCTORS ENABLE CONSTRAINT FK_workdepartment;
ALTER TABLE NURSES ENABLE CONSTRAINT FK_nurse_workdepartment;


commit;


