-- update_enrollments trigger deletes corresponding entries from enrollments table when a student is deleted from students table.
CREATE OR REPLACE TRIGGER update_enrollments
AFTER DELETE on students
for each row
BEGIN
DELETE from enrollments
WHERE sid=:old.sid;
END;
/
show errors

-- update_class_size trigger increments the class_size column in classes table by 1 when a student gets enrolled into a course
CREATE OR REPLACE TRIGGER update_class_size
AFTER INSERT on enrollments
for each row
BEGIN
UPDATE classes set class_size=class_size + 1
WHERE classid=:new.classid;
END;
/
show errors

-- reduce_class_size trigger reduces the class_size column in classes table by 1 when a student drops a course
CREATE OR REPLACE TRIGGER reduce_class_size
AFTER DELETE on enrollments
for each row
BEGIN
UPDATE classes set class_size=class_size - 1
WHERE classid=:old.classid;
END;
/
show errors

-- tri10_1 trigger adds entries to logs table when a student is added to students table
CREATE OR REPLACE TRIGGER tri10_1
AFTER INSERT ON STUDENTS
FOR EACH ROW
BEGIN
INSERT INTO logs values
(log_sequence.nextVal,user,sysdate,'students','insert',:new.sid);
END;
/
show errors

-- tri10_2 trigger adds entries to logs table when a student is deleted from students table
CREATE OR REPLACE TRIGGER tri10_2
AFTER DELETE ON STUDENTS
FOR EACH ROW
BEGIN
INSERT INTO logs values
(log_sequence.nextVal,user,sysdate,'students','delete',:old.sid);
END;
/
show errors

-- tri10_3 trigger adds entries to logs table when a student is added into enrollments table
CREATE OR REPLACE TRIGGER tri10_3
AFTER INSERT ON ENROLLMENTS
FOR EACH ROW
BEGIN
INSERT INTO logs values
(log_sequence.nextVal,user,sysdate,'enrollments','insert',:new.sid||:new.classid);
END;
/
show errors

-- tri10_4 trigger adds entries to logs table when a student drops a course from enrollments table
CREATE OR REPLACE TRIGGER tri10_4
AFTER DELETE ON ENROLLMENTS
FOR EACH ROW
BEGIN
INSERT INTO logs values
(log_sequence.nextVal,user,sysdate,'enrollments','delete',:old.sid||:old.classid);
END;
/
show errors
