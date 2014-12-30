set serveroutput on size unlimited;
CREATE OR REPLACE PROCEDURE show_details(
s_sid in students.sid%type, prc out sys_refcursor) IS
s_firstname students.firstname%type;
c_deptcode courses.dept_code%type;
c_course# courses.course#%type;
c_title courses.title%type;
invalid_sid exception;
s number;

CURSOR cur_rec
IS
SELECT s.firstname,c.dept_code,c.course#,c.title
FROM students s, courses c, enrollments e, classes l
WHERE s.sid=e.sid and e.classid=l.classid and l.dept_code=c.dept_code
and l.course#=c.course# and s.sid=s_sid ;
BEGIN
OPEN cur_rec;
LOOP
FETCH cur_rec INTO s_firstname,c_deptcode,c_course#,c_title;
EXIT WHEN cur_rec%NOTFOUND;
dbms_output.put_line('Sid:' || s_sid ||'        '|| 'Firstname:' ||
s_firstname ||' '|| 'Dept code and course#:' || c_deptcode || c_course#
||'             '||
'Title:'|| c_title);
END LOOP;

open prc for
        SELECT s.firstname,c.dept_code,c.course#,c.title
        FROM students s, courses c, enrollments e, classes l
        WHERE s.sid=e.sid and e.classid=l.classid and
l.dept_code=c.dept_code and l.course#=c.course# and s.sid=s_sid ;

IF s_sid IS NULL THEN
RAISE invalid_sid;
ELSE
SELECT count(*) INTO s FROM students where sid=s_sid;
IF s=0 THEN
RAISE invalid_sid;
END IF;
END IF;

IF cur_rec%ROWCOUNT=0 THEN
  RAISE_APPLICATION_ERROR(-20001,'The student has not taken any course');
--dbms_output.put_line('The student has not taken any course');
END IF;

EXCEPTION
when invalid_sid then
   RAISE_APPLICATION_ERROR(-20001, 'The sid is invalid');
--dbms_output.put_line('The sid in Invalid');

CLOSE cur_rec;
END;
/
show errors
