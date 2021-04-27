CREATE OR REPLACE FUNCTION NET_SALARY_NURSES (searched_id NUMBER)
RETURN NUMBER
AS
    v_salary NUMBER; v_social_contributions NUMBER; v_tax_contributions NUMBER; 
    v_health_insurance_contribution NUMBER; v_health_insurance_contribution_available_for_tax_deduction NUMBER;
    c_pension_contribution CONSTANT NUMBER := 0.0976;
    c_social_security_contribution CONSTANT NUMBER := 0.015;
    c_sick_leave_contribution CONSTANT NUMBER := 0.0245;
    c_health_insurance_contribution CONSTANT NUMBER := 0.09;
    c_health_insurance_contribution_available_for_tax_deduction CONSTANT NUMBER := 0.075;
    c_first_tax_rate CONSTANT NUMBER:= 0.17;
    c_second_tax_rate CONSTANT NUMBER:= 0.32;
    c_second_tax_bracket CONSTANT NUMBER:= 85528;
    tax_free_bracket CONSTANT NUMBER := 667;
BEGIN
    select salary into v_salary from nurses where nurse_id = searched_id;
    
    v_social_contributions :=  (c_pension_contribution + c_social_security_contribution + c_sick_leave_contribution) * v_salary;
    v_salary := v_salary - v_social_contributions;
    v_health_insurance_contribution := v_salary * c_health_insurance_contribution;
    v_health_insurance_contribution_available_for_tax_deduction := v_salary * c_health_insurance_contribution_available_for_tax_deduction;
    IF v_salary * 12 <= c_second_tax_bracket THEN
    v_tax_contributions := (v_salary - (tax_free_bracket)) * c_first_tax_rate -  v_health_insurance_contribution_available_for_tax_deduction;
    ELSE
    v_tax_contributions := (v_salary - c_second_tax_bracket/12 - tax_free_bracket) * c_first_tax_rate + c_first_tax_rate * c_second_tax_bracket - v_health_insurance_contribution_available_for_tax_deduction;
    END IF;
    
    v_salary := v_salary - v_tax_contributions - v_health_insurance_contribution;
    
    RETURN v_salary;
END;
/

CREATE OR REPLACE FUNCTION COVID_BONUS_DOCTORS (searched_id NUMBER)
RETURN NUMBER
AS
    v_salary NUMBER; v_specialization VARCHAR2 (20);
    v_bonus NUMBER := 0;
    c_max_salary CONSTANT NUMBER := 6000;
    c_bonus_multiplier CONSTANT NUMBER := 0.5;
    c_over_max_salary CONSTANT NUMBER := 0.1;
    c_bonus_for_right_specialization CONSTANT NUMBER := 0.25;
BEGIN
    select salary, med_specialisation into v_salary, v_specialization from doctors where doctor_id = searched_id;
    
    IF v_salary <= c_max_salary THEN
    v_bonus := c_bonus_multiplier * v_salary;
    ELSE
    v_bonus := c_bonus_multiplier * c_max_salary + (v_salary-c_max_salary)*c_over_max_salary;
    END IF;
    
    IF v_specialization = 'Anestezjolog' THEN
    v_bonus := v_bonus * (1+c_bonus_for_right_specialization);
    END IF;
    
    RETURN v_bonus;
END;
/

SET SERVEROUTPUT ON;
CREATE OR REPLACE PROCEDURE ADD_PATIENT ( p_acceptance_date DATE, 
p_main_doctor_id NUMBER, p_department_id NUMBER, p_nurse_id NUMBER, p_room_id NUMBER, d_name VARCHAR2, d_surname VARCHAR2, d_PESEL VARCHAR2, d_disease VARCHAR2)
AS
    v_patient_id NUMBER; v_room_id NUMBER; v_number_of_people NUMBER; v_dep_name VARCHAR2 (20);
    c_max_in_room NUMBER := 4;
BEGIN
    SELECT name into v_dep_name from departments where p_department_id = department_id;
    SELECT count(patient_id) INTO v_number_of_people from patients where p_room_id = room_id;
    select max(patient_id)+1 into v_patient_id from patients;
    IF v_number_of_people < c_max_in_room THEN
        INSERT INTO patients values (v_patient_id, p_acceptance_date, p_main_doctor_id,  p_department_id, p_nurse_id, p_room_id);
        INSERT INTO documentation values (v_patient_id, d_name, d_surname, d_PESEL, d_disease);
        commit;
        dbms_output.put_line ('Dodano pacjenta do pokoju ' || p_room_id || ' na oddziale ' || v_dep_name);
    ELSE
        dbms_output.put_line('Za du¿o pacjentów w pokoju ' || p_room_id);
    END IF;
    
    EXCEPTION
    WHEN NO_DATA_FOUND THEN
    dbms_output.put_line('Nie znaleziono danych');
    RAISE;
END;
/

CREATE OR REPLACE PROCEDURE INTERNSHIP_END
AS 
v_position  VARCHAR2 (20);
v_name VARCHAR2 (20);
v_surname VARCHAR2 (20);
v_id NUMBER;
CURSOR CR IS SELECT doctor_id from doctors;
BEGIN
    OPEN CR;
    LOOP
    EXIT WHEN CR%NOTFOUND;
    FETCH CR into v_id;
    dbms_output.put_line(v_id);
    SELECT position, name, surname into v_position,v_name, v_surname from doctors where doctor_id = v_id;
    IF v_position = 'Sta¿ysta' THEN
    UPDATE doctors SET position = 'Rezydent' where doctor_id = v_id;
    dbms_output.put_line('Koniec sta¿u ' || v_name || ' ' || v_surname || '. Zostaje on specjalista');
    ELSE 
    dbms_output.put_line(v_name ||' '|| v_surname ||' ju¿ jest specjalista!');
    END IF;
    END LOOP;
    CLOSE CR;
END;
/


CREATE OR REPLACE TRIGGER MINIMAL_SALARY
BEFORE INSERT OR UPDATE ON NURSES 
DECLARE 
    v_min_sal nurses.salary%TYPE;
    c_minimal_salary NUMBER := 2800;
    CURSOR H_CURSOR_NURSES IS
    SELECT SALARY FROM NURSES;
BEGIN
    OPEN H_CURSOR_NURSES;
    LOOP
        EXIT WHEN H_CURSOR_NURSES%NOTFOUND;
        FETCH H_CURSOR_NURSES INTO v_min_sal;
        IF v_min_sal < c_minimal_salary THEN
        dbms_output.put_line('Zarobki pracownika sa mniejsze od placy minimalnej wynoszacej: ' || v_min_sal );
        raise_application_error(-20001, 'Zarobki poni¿ej placy minimalnej');
        END IF;
        END LOOP;
        CLOSE H_CURSOR_NURSES;
END;
/

CREATE OR REPLACE TRIGGER HOSPITAL_OVERLOADED
BEFORE INSERT ON PATIENTS
DECLARE 
 v_hospital_capacity NUMBER;
 max_hospital_capacity NUMBER := 15;
BEGIN
        select count(*) into v_hospital_capacity from patients;
        IF max_hospital_capacity < v_hospital_capacity THEN
        dbms_output.put_line('Szpital przepelniony, pacjent nie moze zostac przyjety');
        raise_application_error(-20001, 'Brak miejsc w szpitalu');
        END IF;
END;
/





