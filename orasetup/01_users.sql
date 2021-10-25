alter session set container = XEPDB1;

create tablespace BAM;

create user "BAM_OWNER" profile "DEFAULT" identified by "password" default tablespace "BAM" account unlock;

grant connect to BAM_OWNER;
grant unlimited tablespace to BAM_OWNER;

grant create view to BAM_OWNER;
grant create sequence to BAM_OWNER;
grant create table to BAM_OWNER;
grant create procedure to BAM_OWNER;
grant create type to BAM_OWNER;