set serveroutput on size unlimited;

CREATE OR REPLACE PROCEDURE del_stud(
s_sid in students.sid%type)IS
invalid_sid exception;
s number;

BEGIN
SELECT count(*) INTO s from students WHERE sid=s_sid;

DELETE FROM students WHERE sid=s_sid;

IF s_sid IS NULL THEN
RAISE invalid_sid;
ELSE
IF s=0 THEN
RAISE invalid_sid;
END IF;
END IF;

EXCEPTION
when invalid_sid then
 RAISE_APPLICATION_ERROR(-20001,'The SID is invalid');

END;
/
show errors
