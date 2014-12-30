set serveroutput on size unlimited;
CREATE OR REPLACE PACKAGE display AS
 procedure show_students(p_rc out sys_refcursor);
 procedure show_courses(p_rc out sys_refcursor);
 procedure show_prerequisites(p_rc out sys_refcursor);
 procedure show_classes(p_rc out sys_refcursor);
 procedure show_enrollments(p_rc out sys_refcursor);
 procedure show_logs(p_rc out sys_refcursor);
END;
/

show errors

CREATE OR REPLACE PACKAGE BODY display AS
        --students
        PROCEDURE show_students(p_rc out sys_refcursor) IS
        BEGIN
        OPEN p_rc FOR
        select * from students;
        END;

        --courses
        PROCEDURE show_courses(p_rc out sys_refcursor) IS
        BEGIN
        OPEN p_rc FOR
        select * from courses;
        END;

        --prerequisites
        PROCEDURE show_prerequisites(p_rc out sys_refcursor) IS
        BEGIN
        OPEN p_rc FOR
        select * from prerequisites;
        END;

        -- classes
        PROCEDURE show_classes(p_rc out sys_refcursor) IS
        BEGIN
        OPEN p_rc FOR
        select * from classes;
        END;

        --enrollments
        PROCEDURE show_enrollments(p_rc out sys_refcursor) IS
        BEGIN
        OPEN p_rc FOR
        select * from enrollments;
        END;

        --logs
        PROCEDURE show_logs(p_rc out sys_refcursor) IS
        BEGIN
        OPEN p_rc FOR
        select * from logs;
        END;
END;
/

show errors