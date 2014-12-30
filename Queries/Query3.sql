set serveroutput on size unlimited;
CREATE OR REPLACE PROCEDURE add_stud(
s_sid IN students.sid%type,
s_firstname IN students.firstname%type,
s_lastname IN students.lastname%type,
s_status IN students.status%type,
s_gpa IN students.gpa%type,
s_email IN students.email%type) AS
BEGIN
INSERT INTO
students(sid,firstname,lastname,status,gpa,email)
VALUES (s_sid,s_firstname,s_lastname,s_status,s_gpa,s_email);
END;
/
show errors
