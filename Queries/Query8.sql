set serveroutput on size unlimited;

-- This procedure is takes sid and classid in input and drops the student from the course after checking various conditions as given in the requirements document.

CREATE OR REPLACE PROCEDURE drop_class(
s_sid in students.sid%type,
c_classid in classes.classid%type) IS
c_classsize classes.class_size%type;
c_deptcode classes.dept_code%type;
c_course# classes.course#%type;
c_sect classes.sect#%type;
p_deptcode prerequisites.dept_code%type;
p_course# prerequisites.course#%type;
c_cid classes.classid%type;
c_year classes.year%type;
c_sem classes.semester%type;
s number;
c number;
e number;
e1 number;
p number;
p1 number;
flag number;
c1 number;
sid_invalid exception;
cid_invalid exception;
drop_invalid exception;
not_enrolled exception;
no_student exception;

CURSOR prereq IS
SELECT dept_code,course# FROM prerequisites where pre_dept_code=c_deptcode and pre_course#=c_course#;

BEGIN

SELECT count(*) INTO s FROM students where sid=s_sid;
SELECT count(*) INTO c FROM classes where classid=c_classid;
SELECT count(*) INTO e FROM enrollments where sid=s_sid and classid=c_classid;

-- To check if a given student is enrolled in the given class
IF c>0 and s>0 THEN
SELECT count(classid) INTO e1 FROM enrollments where sid=s_sid;
END IF;

-- For given classid, deptcode and course#,year and semester is selected
IF c>0 THEN
SELECT dept_code,course#,sect#,year,semester INTO c_deptcode,c_course#,c_sect,c_y                                                                                       ear,c_sem from classes where classid=c_classid;
END IF;

-- Checks for prerequisite courses
SELECT count(*) into p1 from prerequisites where pre_dept_code=c_deptcode and pre                                                                                       _course#=c_course#;
IF p1>0 THEN
OPEN prereq;
LOOP
FETCH prereq INTO p_deptcode,p_course#;
EXIT when prereq%NOTFOUND;

IF p_course# IS NOT NULL THEN
SELECT count(classid) into c1 FROM classes where dept_code=p_deptcode and course#                                                                                       =p_course# and year=c_year and semester=c_sem;
flag:=1;
ELSE
flag:=0;
END IF;

-- Gets the classid of the course for which the given course is a prerequisite
IF c1>0 THEN
SELECT classid into c_cid FROM classes where dept_code=p_deptcode and course#=p_c                                                                                       ourse# and year=c_year and semester=c_sem;
flag:=1;
ELSE
flag:=0;
END IF;

-- Checks if the student is enrolled in the classid obtained in previous step
SELECT count(*) into p FROM enrollments where classid=c_cid and sid=s_sid;
END LOOP;
CLOSE prereq;

END IF;

-- If the student is enrolled in the given course and if the student is not enrolled in any of the courses that uses the given course as a prerequisite then drop course
IF e>0 and p=0 THEN
DELETE FROM enrollments WHERE sid=s_sid and classid=c_classid;
END IF;

IF flag=0 THEN
DELETE FROM enrollments WHERE sid=s_sid and classid=c_classid;
END IF;

-- Checks for invalid sid and classid
IF s=0 THEN
        RAISE sid_invalid;
END IF;
IF c=0 THEN
        RAISE cid_invalid;
END IF;

-- If the student is enrolled in the given course and the student is also enrolled in a course that uses the given course as a prerequisite then drop is not permitted
IF e>0 and p>0 THEN
        RAISE drop_invalid;
END IF;

IF e=0 and c>0 and s>0 THEN
        RAISE not_enrolled;
END IF;

IF e1=0 THEN
        RAISE not_enrolled;
END IF;

-- If the course is not a prerequisite for any course then drop is permitted
IF p1=0 THEN
DELETE FROM enrollments WHERE sid=s_sid and classid=c_classid;
END IF;

IF c>0 THEN
SELECT class_size INTO c_classsize FROM classes where classid=c_classid;
END IF;
IF e>0 and p=0 and c_classsize=0 THEN
        RAISE no_student;
END IF;

EXCEPTION
WHEN sid_invalid THEN
        RAISE_APPLICATION_ERROR(-20001,'SID is invalid');
WHEN cid_invalid THEN
        RAISE_APPLICATION_ERROR(-20001,'CLASSID is invalid');
WHEN drop_invalid THEN
        RAISE_APPLICATION_ERROR(-20001,'drop is not permitted because another class uses it as a prerequisite');
WHEN not_enrolled THEN
        RAISE_APPLICATION_ERROR(-20001,'student is not enrolled in any classes')
WHEN no_student THEN
         RAISE_APPLICATION_ERROR(-20001,'class has now no students');

END;

/
show errors
