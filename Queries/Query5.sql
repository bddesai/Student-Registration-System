set serveroutput on size unlimited;

-- This procedure displays all the prerequisite courses for a given course

CREATE OR REPLACE PROCEDURE prereq(
c_deptcode in prerequisites.dept_code%type,
c_course# in prerequisites.course#%type,
prc out sys_refcursor) IS
p_deptcode prerequisites.pre_dept_code%type;
p_course# prerequisites.pre_course#%type;
count_course number;
invalid_entry exception;
no_prereq exception;

CURSOR pre IS
SELECT pre_dept_code,pre_course# from prerequisites
WHERE dept_code=c_deptcode and course#=c_course#;

BEGIN
OPEN pre;

LOOP
FETCH pre INTO p_deptcode,p_course#;
EXIT when pre%NOTFOUND;
dbms_output.put_line('Prerequisite course:'|| ' '||p_deptcode||p_course#);
END LOOP;

SELECT COUNT(*) into count_course from COURSES where dept_code=c_deptcode and course#=c_course#;
IF count_course=0 THEN
        RAISE invalid_entry;
END IF;

open prc for
SELECT pre_dept_code,pre_course# from prerequisites WHERE dept_code=c_deptcode and course#=c_course#;


IF pre%ROWCOUNT=0 THEN
        RAISE no_prereq;
--RAISE_APPLICATION_ERROR(-20001, 'There are no prerequisites for the given course');
END IF;

EXCEPTION
WHEN invalid_entry THEN
        RAISE_APPLICATION_ERROR(-20001,'This Course is not valid');
WHEN no_prereq THEN
        RAISE_APPLICATION_ERROR(-20001,'There are no prerequisites for the given course');
--WHEN OTHERS THEN
--      RAISE_APPLICATION_ERROR(-20001,'INVALID ENTRY');

CLOSE pre;

END;
/
show errors
