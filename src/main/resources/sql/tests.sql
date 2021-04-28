select n.*, net_salary_nurses(nurse_id) from nurses n;

select d.*, covid_bonus_doctors(doctor_id) from doctors d;

exec add_patient('04/09/2020', 1, 1, 1, 1, 'Andrzej', 'Oleksy', '67081021234', 'Rak pluca');
exec add_patient('04/09/2020', 1, 1, 1, 1, 'Andrzej', 'Oleksy', '67081021234', 'Rak pluca');
exec add_patient('04/09/2020', 1, 1, 1, 1, 'Andrzej', 'Oleksy', '67081021234', 'Rak pluca');
exec add_patient('04/09/2020', 1, 1, 1, 1, 'Andrzej', 'Oleksy', '67081021234', 'Rak pluca');
exec add_patient('04/09/2020', 1, 1, 1, 1, 'Andrzej', 'Oleksy', '67081021234', 'Rak pluca');
exec add_patient('04/09/2020', 3, 3, 3, 3, 'Andrzej', 'Oleksy', '67081021234', 'Rak pluca');
exec add_patient('04/09/2020', 3, 3, 3, 3, 'Andrzej', 'Oleksy', '67081021234', 'Rak pluca');
exec add_patient('04/09/2020', 3, 3, 3, 3, 'Andrzej', 'Oleksy', '67081021234', 'Rak pluca');
exec add_patient('04/09/2020', 2, 2, 2, 2, 'Andrzej', 'Oleksy', '67081021234', 'Rak pluca');
exec add_patient('04/09/2020', 2, 2, 2, 2, 'Andrzej', 'Oleksy', '67081021234', 'Rak pluca');
exec add_patient('04/09/2020', 2, 2, 2, 2, 'Andrzej', 'Oleksy', '67081021234', 'Rak pluca');

exec INTERNSHIP_END;

INSERT INTO PATIENTS VALUES (NULL, '24/12/2020', 11, 2, 9, 6);

INSERT INTO NURSES VALUES (NULL, 'Dawid', 'Omkos', 'Ratownik', 9, 4, 1500);

select count(DISTINCT d.doctor_id) as Liczba_lekarzy, position from doctors d join patients p on d.doctor_id = p.main_doctor_id where med_specialisation = 'Ortopeda' group by position;

select d.name, d.surname, d.med_specialisation, d.position, o.name as imie_ordynatora, o.surname as nazwisko_ordynatora, dep.name as nazwa_oddzialu from doctors d join doctors o on d.chief_doctor_id = o.doctor_id 
join departments dep on d.department_id = dep.department_id;

select * from  doctors d left outer join nurses n on d.department_id = n.department_id where d.chief_doctor_id IS NULL;

select * from nurses where salary > 5000 UNION select * from nurses where department_id = 4;

select * from doctors d left join operating_theatre o on d.doctor_id = o.doctor_id where o.doctor_id is NULL;

