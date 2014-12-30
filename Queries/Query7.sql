set serveroutput on size unlimited;

-- This procedure takes studentid and classid as input parameters.Different conditions are checked and the students are then enrolled into the class and entry
is made to
-- enrollments table.

CREATE OR REPLACE PROCEDURE stud_enroll(
s_sid in students.sid%type,
c_classid in classes.classid%type) IS
c_limit classes.limit%type;
c_classsize classes.class_size%type;
e_sid enrollments.sid%type;
invalid_sid exception;
invalid_cid exception;
limit exception;
already exception;
overload exception;
cannot_enroll exception;
s number;
c number;
flag number;
e_count number;
e_count1 number;
flag1 number;

BEGIN
flag1:=1;
-- e_count gives the count of classes the student is enrolled in
SELECT count(sid) INTO e_count FROM enrollments where classid=c_classid and sid=s_sid;

-- e_count1 gives the number of classes enrolled in the same year and same semester
SELECT count(*) INTO e_count1 FROM enrollments e
WHERE classid in(select classid FROM classes where (year,semester) in(select year,semester from classes group by year,semester having
count(*)=(select max(count(*))FROM classes group by year,semester))) and e.sid=s_sid;

IF s_sid IS NULL
THEN RAISE invalid_sid;
ELSE SELECT count(*) INTO s FROM students where sid=s_sid;
IF s=0 THEN RAISE invalid_sid;
END IF;
END IF;

IF c_classid IS NULL THEN
RAISE invalid_cid;
ELSE SELECT count(*) INTO c FROM classes where classid=c_classid;
IF c=0 THEN
RAISE invalid_cid;
END IF;
END IF;


IF e_count!=0 THEN
flag:=1;
ELSE
flag:=0;
END IF;

IF e_count1>=4 THEN
        RAISE cannot_enroll;
flag1:=0;
ELSIF e_count1=3 THEN
        RAISE overload;
flag:=0;
END IF;

SELECT limit,class_size
INTO c_limit,c_classsize FROM classes where classid=c_classid;


IF flag=1 THEN
RAISE already;
ELSIF flag=0 AND flag1=1 AND c_classsize < c_limit THEN
INSERT INTO enrollments (sid,classid,lgrade)
VALUES (s_sid,c_classid,null);
END IF;
IF c_classsize=c_limit THEN
RAISE limit;
END IF;

EXCEPTION
when invalid_sid then
        RAISE_APPLICATION_ERROR(-20001,'The sid is invalid');
when invalid_cid then
        RAISE_APPLICATION_ERROR(-20001,'The classid is invalid');
when limit then
        RAISE_APPLICATION_ERROR(-20001,'The class is closed');
when already then
        RAISE_APPLICATION_ERROR(-20001,'The student is already in class');
when overload then
        RAISE_APPLICATION_ERROR(-20001,'Youare Overloaded');
when cannot_enroll then
         RAISE_APPLICATION_ERROR(-20001,'Cannot enroll in more than 4 classes in the same year and same semester');

END;
/
show errors
