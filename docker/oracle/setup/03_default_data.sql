alter session set container = XEPDB1;

-- Populate Verification
insert into BAM_OWNER.VERIFICATION (VERIFICATION_ID, NAME) values (1, 'Verified');
insert into BAM_OWNER.VERIFICATION (VERIFICATION_ID, NAME) values (50, 'Provisionally Verified');
insert into BAM_OWNER.VERIFICATION (VERIFICATION_ID, NAME) values (100, 'Not Verified');

-- Populate Workgroup
insert into BAM_OWNER.WORKGROUP (WORKGROUP_ID, NAME) values (BAM_OWNER.WORKGROUP_ID.nextval, 'Group 1', 'group1Leaders');
insert into BAM_OWNER.WORKGROUP (WORKGROUP_ID, NAME) values (BAM_OWNER.WORKGROUP_ID.nextval, 'Group 2', 'group2Leaders');
insert into BAM_OWNER.WORKGROUP (WORKGROUP_ID, NAME) values (BAM_OWNER.WORKGROUP_ID.nextval, 'Group 3', 'group3Leaders');

--Populate Credited Controls
insert into BAM_OWNER.CREDITED_CONTROL (CREDITED_CONTROL_ID,NAME,DESCRIPTION,WORKGROUP_ID,WEIGHT,VERIFICATION_FREQUENCY) values (BAM_OWNER.CREDITED_CONTROL_ID.nextval,'Control 1','Control 1 Description',1,1,'1 Year');
insert into BAM_OWNER.CREDITED_CONTROL (CREDITED_CONTROL_ID,NAME,DESCRIPTION,WORKGROUP_ID,WEIGHT,VERIFICATION_FREQUENCY) values (BAM_OWNER.CREDITED_CONTROL_ID.nextval,'Control 2','Control 2 Description',1,1,'1 Year');
insert into BAM_OWNER.CREDITED_CONTROL (CREDITED_CONTROL_ID,NAME,DESCRIPTION,WORKGROUP_ID,WEIGHT,VERIFICATION_FREQUENCY) values (BAM_OWNER.CREDITED_CONTROL_ID.nextval,'Control 3','Control 3 Description',1,1,'1 Year');

--Populate Beam Destinations
--insert into BAM_OWNER.beam_destination values(BAM_OWNER.destination_id.nextval, 'Destination 1', 'CEBAF', 'uA', 'Y', 1);
--insert into BAM_OWNER.beam_destination values(BAM_OWNER.destination_id.nextval, 'Destination 2', 'CEBAF', 'uA', 'Y', 2);
--insert into BAM_OWNER.beam_destination values(BAM_OWNER.destination_id.nextval, 'Destination 3', 'CEBAF', 'uA', 'Y', 3);

