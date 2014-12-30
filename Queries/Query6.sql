set serveroutput on size unlimited;

CREATE OR REPLACE PROCEDURE show_studinclass(
l_classid in classes.classid%type, prc out sys_refcursor) IS
c_title courses.title%type;
s_sid students.sid%type;
s_firstname students.firstname%type;

invalid_classid exception;
c number;

CURSOR stud_rec IS
SELECT s.sid,s.firstname,c.title
FROM students s,enrollments e,classes l,courses c
WHERE s.sid=e.sid and e.classid=l.classid and l.dept_code=c.dept_code
and l.course#=c.course# and l.classid=l_classid;

BEGIN
OPEN stud_rec;

LOOP
FETCH stud_rec INTO s_sid,s_firstname,c_title;
EXIT when stud_rec%NOTFOUND;
dbms_output.put_line('Sid:' ||s_sid ||'
'||'Firstname:'||s_firstname||'         '||'Classid:'||l_classid||'
'||'Course Title:'||c_title);
END LOOP;

OPEN prc FOR
 SELECT s.sid,s.firstname,c.title
 FROM students s,enrollments e,classes l,courses c
 WHERE s.sid=e.sid and e.classid=l.classid and l.dept_code=c.dept_code and l.course#=c.course# and l.classid=l_classid;

IF l_classid IS NULL then
RAISE invalid_classid;
ELSE
SELECT COUNT(*)
INTO c
FROM classes
WHERE classid=l_classid;
IF c=0 then
RAISE invalid_classid;
END IF;
END IF;

IF stud_rec%ROWCOUNT=0 THEN
  RAISE_APPLICATION_ERROR(-20001, 'No student is enrolled in class');
END IF;

EXCEPTION
when invalid_classid then
 RAISE_APPLICATION_ERROR(-20001, 'Invalid class id');


CLOSE stud_rec;
END;
/
show errors
