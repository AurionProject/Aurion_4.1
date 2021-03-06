CREATE TABLE SBYN_SYSTEMS
(
    SYSTEMCODE		VARCHAR(40)	NOT NULL,
    DESCRIPTION		VARCHAR(50)	NOT NULL,
    STATUS		CHAR(1)		NOT NULL,
    ID_LENGTH		INTEGER		NOT NULL,
    FORMAT		VARCHAR(60)	NULL    ,
    INPUT_MASK		VARCHAR(60)	NULL    ,
    VALUE_MASK		VARCHAR(60)	NULL    ,
    CREATE_DATE		DATETIME	NOT NULL,
    CREATE_USERID	VARCHAR(20)	NOT NULL,
    UPDATE_DATE		DATETIME	NULL    ,
    UPDATE_USERID	VARCHAR(20)	NULL    ,
    CONSTRAINT PK_SBYN_SYSTEM PRIMARY KEY (SYSTEMCODE)
);

CREATE TABLE SBYN_USER_CODE
(
    CODE_LIST		VARCHAR(20)	NOT NULL,
    CODE			VARCHAR(20)	NOT NULL,
    DESCR			VARCHAR(50)	NOT NULL,
    FORMAT			VARCHAR(60)	NULL    ,
    INPUT_MASK		VARCHAR(60)	NULL    ,
    VALUE_MASK		VARCHAR(60)	NULL    ,
    CONSTRAINT PK_SBYN_USER_CODE PRIMARY KEY (CODE_LIST, CODE)
        
);

CREATE TABLE SBYN_SYSTEMOBJECT (
	SYSTEMCODE VARCHAR(40),
	LID VARCHAR(25),
	CHILDTYPE VARCHAR(20),
	CREATEUSER VARCHAR(30),
	CREATEFUNCTION VARCHAR(20),
	CREATEDATE DATETIME,
	UPDATEUSER VARCHAR(30),
	UPDATEFUNCTION VARCHAR(20),
	UPDATEDATE DATETIME,
	STATUS VARCHAR(15));

ALTER TABLE SBYN_SYSTEMOBJECT ADD CONSTRAINT PK_SBYNSYSTEMOBJECT PRIMARY KEY (SYSTEMCODE, LID);
ALTER TABLE SBYN_SYSTEMOBJECT ADD CONSTRAINT FK_SYSTEMOBJECT_SYSTEMCODE FOREIGN KEY (SYSTEMCODE) REFERENCES SBYN_SYSTEMS(SYSTEMCODE);

CREATE TABLE SBYN_SYSTEMSBR (
	EUID VARCHAR(20),
	CHILDTYPE VARCHAR(20),
	CREATESYSTEM VARCHAR(20),
	CREATEUSER VARCHAR(30),
	CREATEFUNCTION VARCHAR(20),
	CREATEDATE DATETIME,
	UPDATESYSTEM VARCHAR(20),
	UPDATEUSER VARCHAR(30),
	UPDATEFUNCTION VARCHAR(20),
	UPDATEDATE DATETIME,
	STATUS VARCHAR(15),
    REVISIONNUMBER NUMERIC(38));

ALTER TABLE SBYN_SYSTEMSBR ADD CONSTRAINT PK_SBYNSYSTEMSBR PRIMARY KEY (EUID);

CREATE TABLE SBYN_OVERWRITE (
	EUID VARCHAR(20),
	PATH VARCHAR(200),
	TYPE VARCHAR(20),
	INTEGERDATA NUMERIC(38),
	BOOLEANDATA NUMERIC(38),
	STRINGDATA VARCHAR(200),
	BYTEDATA CHAR(2),
	LONGDATA LONG,
	DATEDATA DATETIME,
	FLOATDATA NUMERIC(38, 4),
	TIMESTAMPDATA DATETIME);

ALTER TABLE SBYN_OVERWRITE ADD CONSTRAINT FK_SYSTEMSBR_EUID FOREIGN KEY (EUID) REFERENCES SBYN_SYSTEMSBR(EUID);
ALTER TABLE SBYN_OVERWRITE ADD CONSTRAINT PK_SBROVERWRITE PRIMARY KEY (EUID, PATH);

CREATE TABLE SBYN_ENTERPRISE (
	SYSTEMCODE VARCHAR(40),
	LID VARCHAR(25),
	EUID VARCHAR(20));

ALTER TABLE SBYN_ENTERPRISE ADD CONSTRAINT FK_ENTERPRISE_SYSTEMCODE_LID FOREIGN KEY (SYSTEMCODE, LID) REFERENCES SBYN_SYSTEMOBJECT(SYSTEMCODE, LID);
ALTER TABLE SBYN_ENTERPRISE ADD CONSTRAINT FK_ENTERPRISE_EUID FOREIGN KEY (EUID) REFERENCES SBYN_SYSTEMSBR(EUID);
ALTER TABLE SBYN_ENTERPRISE ADD CONSTRAINT PK_ENTERPRISE PRIMARY KEY (SYSTEMCODE, LID, EUID);

CREATE TABLE SBYN_TRANSACTION (
	TRANSACTIONNUMBER VARCHAR(20),
	LID1 VARCHAR(25),
	LID2 VARCHAR(25),
	EUID1 VARCHAR(20),
	EUID2 VARCHAR(20),
	FUNCTION VARCHAR(20),
	SYSTEMUSER VARCHAR(30),
	TIMESTAMP DATETIME,
	DELTA BLOB,
	SYSTEMCODE VARCHAR(40),
	LID VARCHAR(25),
	EUID VARCHAR(20));

ALTER TABLE SBYN_TRANSACTION ADD CONSTRAINT PK_TRANSACTION PRIMARY KEY (TRANSACTIONNUMBER);
ALTER TABLE SBYN_TRANSACTION ADD CONSTRAINT AK_TRANSACTION UNIQUE (EUID, EUID2, TRANSACTIONNUMBER);

CREATE TABLE SBYN_ASSUMEDMATCH (
    ASSUMEDMATCHID VARCHAR(20),
    EUID VARCHAR(20),
    SYSTEMCODE VARCHAR(40),
	LID VARCHAR(25),
    WEIGHT VARCHAR(20),
    TRANSACTIONNUMBER VARCHAR(20));
    

ALTER TABLE SBYN_ASSUMEDMATCH ADD CONSTRAINT FK_AM_TRANSACTIONNUMBER FOREIGN KEY (TRANSACTIONNUMBER) REFERENCES SBYN_TRANSACTION(TRANSACTIONNUMBER);

CREATE TABLE SBYN_POTENTIALDUPLICATES (
	POTENTIALDUPLICATEID VARCHAR(20),
	WEIGHT VARCHAR(20),
	TYPE VARCHAR(15),
	DESCRIPTION VARCHAR(120),
	STATUS VARCHAR(15),
	HIGHMATCHFLAG VARCHAR(15),
	RESOLVEDUSER VARCHAR(30),
	RESOLVEDDATE DATETIME,
	RESOLVEDCOMMENT VARCHAR(120),
	EUID2 VARCHAR(20),
	TRANSACTIONNUMBER VARCHAR(20),
	EUID1 VARCHAR(20));

ALTER TABLE SBYN_POTENTIALDUPLICATES ADD CONSTRAINT PK_POTENTIALDUPLICATES PRIMARY KEY (POTENTIALDUPLICATEID);

CREATE TABLE SBYN_AUDIT (
	AUDIT_ID VARCHAR(20) NOT NULL,
	PRIMARY_OBJECT_TYPE VARCHAR(20) ,
	EUID VARCHAR(20) ,
	EUID_AUX VARCHAR(20),
	FUNCTION VARCHAR(32) NOT NULL,
	DETAIL VARCHAR(120),
	CREATE_DATE DATETIME NOT NULL,
	CREATE_BY VARCHAR(20) NOT NULL);

ALTER TABLE SBYN_AUDIT ADD CONSTRAINT PK_SBYN_AUDIT PRIMARY KEY (AUDIT_ID);

CREATE TABLE SBYN_MERGE (
	MERGE_ID VARCHAR(20) NOT NULL,
    KEPT_EUID VARCHAR(20) NOT NULL,
    MERGED_EUID VARCHAR(20),
    MERGE_TRANSACTIONNUM VARCHAR(20) NOT NULL,
    UNMERGE_TRANSACTIONNUM VARCHAR(20));

ALTER TABLE SBYN_MERGE ADD CONSTRAINT FK_SBYN_MERGE FOREIGN KEY (KEPT_EUID, MERGED_EUID, MERGE_TRANSACTIONNUM) REFERENCES SBYN_TRANSACTION(EUID, EUID2, TRANSACTIONNUMBER);
ALTER TABLE SBYN_MERGE ADD CONSTRAINT PK_SBYN_MERGE PRIMARY KEY (MERGE_ID);

CREATE TABLE SBYN_APPL
(
    APPL_ID                  NUMERIC(10)             NOT NULL,
    CODE                     VARCHAR(8)            NOT NULL,
    DESCR                    VARCHAR(30)           NOT NULL,
    READ_ONLY                CHAR(1)                DEFAULT 'N' NOT NULL
        CHECK ('Y','N'),
    CREATE_DATE              DATETIME                   NOT NULL,
    CREATE_USERID            VARCHAR(20)           NOT NULL,
    CONSTRAINT PK_UP_SBYN_APPL PRIMARY KEY (APPL_ID)
)
;
CREATE UNIQUE INDEX SBYN_APPL_IDX1 ON SBYN_APPL (CODE ASC);

CREATE TABLE SBYN_COMMON_HEADER
(
    COMMON_HEADER_ID         NUMERIC(10)             NOT NULL,
    APPL_ID                  NUMERIC(10)             NOT NULL,
    CODE                     VARCHAR(8)            NOT NULL,
    DESCR                    VARCHAR(50)           NOT NULL,
    READ_ONLY                CHAR(1)                DEFAULT 'N' NOT NULL,
    MAX_INPUT_LEN            NUMERIC(10)             DEFAULT 1 NOT NULL,
    TYP_TABLE_CODE           VARCHAR(3)            NOT NULL,
    CREATE_DATE              DATETIME                   NOT NULL,
    CREATE_USERID            VARCHAR(20)           NOT NULL,
    CONSTRAINT PK_SBYN_COMMON_HEADER PRIMARY KEY (COMMON_HEADER_ID)
)
;
CREATE INDEX FK_SBYN_COMMON_HEADER ON SBYN_COMMON_HEADER (APPL_ID ASC);
CREATE UNIQUE INDEX SBYN_COMMOM_HEADER_IDX1 ON SBYN_COMMON_HEADER (APPL_ID ASC, CODE ASC);

CREATE TABLE SBYN_COMMON_DETAIL
(
    COMMON_DETAIL_ID         NUMERIC(10)             NOT NULL
        CHECK (COMMON_DETAIL_ID >= 0),
    COMMON_HEADER_ID         NUMERIC(10)             NOT NULL,
    CODE                     VARCHAR(20)           NOT NULL,
    DESCR                    VARCHAR(50)           NOT NULL,
    READ_ONLY                CHAR(1)                DEFAULT 'N' NOT NULL
        CHECK ('Y','N'),
    CREATE_DATE              DATETIME                   NOT NULL,
    CREATE_USERID            VARCHAR(20)           NOT NULL,
    CONSTRAINT PK_SBYN_COMMON_DETAIL PRIMARY KEY (COMMON_DETAIL_ID)
)
;
CREATE UNIQUE INDEX SBYN_COMMOM_DETAIL_IDX1 ON SBYN_COMMON_DETAIL (COMMON_HEADER_ID ASC, CODE ASC);

ALTER TABLE SBYN_COMMON_DETAIL
    ADD CONSTRAINT FK_COMM_DET_COMM_HEAD FOREIGN KEY  (COMMON_HEADER_ID)
       REFERENCES SBYN_COMMON_HEADER (COMMON_HEADER_ID)
;

CREATE INDEX SBYN_ENTERPRISE1 on SBYN_ENTERPRISE (EUID ASC);
CREATE INDEX SBYN_TRANSACTION1 on SBYN_TRANSACTION (TIMESTAMP ASC);
CREATE INDEX SBYN_TRANSACTION2 on SBYN_TRANSACTION (FUNCTION ASC);
CREATE INDEX SBYN_TRANSACTION4 on SBYN_TRANSACTION (EUID2 ASC, TIMESTAMP ASC);
CREATE INDEX SBYN_TRANSACTION3 on SBYN_TRANSACTION (TIMESTAMP ASC, TRANSACTIONNUMBER ASC);
ALTER TABLE SBYN_ASSUMEDMATCH ADD CONSTRAINT PK_ASSUMEDMATCH PRIMARY KEY (ASSUMEDMATCHID);
CREATE INDEX SBYN_ASSUMEDMATCH1 on SBYN_ASSUMEDMATCH (EUID ASC);
CREATE INDEX SBYN_ASSUMEDMATCH2 ON SBYN_ASSUMEDMATCH (TRANSACTIONNUMBER ASC);
CREATE INDEX SBYN_POTENTIALDUPLICATES1 on SBYN_POTENTIALDUPLICATES (EUID1 ASC);
CREATE INDEX SBYN_POTENTIALDUPLICATES2 on SBYN_POTENTIALDUPLICATES (EUID2 ASC);
CREATE INDEX SBYN_POTENTIALDUPLICATES3 ON SBYN_POTENTIALDUPLICATES (TRANSACTIONNUMBER ASC);
CREATE INDEX SBYN_AUDIT1 on SBYN_AUDIT (EUID ASC);
CREATE INDEX SBYN_AUDIT2 on SBYN_AUDIT (CREATE_DATE ASC);
CREATE INDEX SBYN_MERGE1 on SBYN_MERGE (KEPT_EUID ASC);

CREATE TABLE SBYN_PATIENT (
	SYSTEMCODE VARCHAR(40),
	LID VARCHAR(25),
	PATIENTID VARCHAR(20),
	PERSONCATCODE varchar(8),
	FIRSTNAME varchar(40),
	FIRSTNAME_STD varchar(40),
	FIRSTNAME_PHON varchar(8),
	MIDDLENAME varchar(30),
	LASTNAME varchar(40),
	LASTNAME_STD varchar(40),
	LASTNAME_PHON varchar(8),
	SUFFIX varchar(10),
	TITLE varchar(8),
	SSN varchar(16),
	DOB datetime,
	DEATH varchar(1),
	GENDER varchar(8),
	MSTATUS varchar(8),
	RACE varchar(8),
	ETHNIC varchar(8),
	RELIGION varchar(8),
	LANGUAGE varchar(8),
	SPOUSENAME varchar(100),
	MOTHERNAME varchar(100),
	MOTHERMN varchar(40),
	FATHERNAME varchar(100),
	MAIDEN varchar(40),
	POBCITY varchar(30),
	POBSTATE varchar(30),
	POBCOUNTRY varchar(20),
	VIPFLAG varchar(8),
	VETSTATUS varchar(8),
	STATUS varchar(8),
	DRIVERLICENSE varchar(20),
	DRIVERLICENSEST varchar(10),
	DOD datetime,
	DEATHCERTIFICATE varchar(10),
	NATIONALITY varchar(8),
	CITIZENSHIP varchar(8))
		;

ALTER TABLE SBYN_PATIENT ADD CONSTRAINT FK_PATIENT_SYSTEMCODE_LID FOREIGN KEY (SYSTEMCODE, LID) REFERENCES SBYN_SYSTEMOBJECT(SYSTEMCODE, LID);
ALTER TABLE SBYN_PATIENT ADD CONSTRAINT PK_PATIENT PRIMARY KEY (PATIENTID);
ALTER TABLE SBYN_PATIENT ADD CONSTRAINT U_PATIENT UNIQUE (SYSTEMCODE, LID);

CREATE TABLE SBYN_PATIENTSBR (
	EUID VARCHAR(20),
	PATIENTID VARCHAR(20),
	PERSONCATCODE varchar(8),
	FIRSTNAME varchar(40),
	FIRSTNAME_STD varchar(40),
	FIRSTNAME_PHON varchar(8),
	MIDDLENAME varchar(30),
	LASTNAME varchar(40),
	LASTNAME_STD varchar(40),
	LASTNAME_PHON varchar(8),
	SUFFIX varchar(10),
	TITLE varchar(8),
	SSN varchar(16),
	DOB datetime,
	DEATH varchar(1),
	GENDER varchar(8),
	MSTATUS varchar(8),
	RACE varchar(8),
	ETHNIC varchar(8),
	RELIGION varchar(8),
	LANGUAGE varchar(8),
	SPOUSENAME varchar(100),
	MOTHERNAME varchar(100),
	MOTHERMN varchar(40),
	FATHERNAME varchar(100),
	MAIDEN varchar(40),
	POBCITY varchar(30),
	POBSTATE varchar(30),
	POBCOUNTRY varchar(20),
	VIPFLAG varchar(8),
	VETSTATUS varchar(8),
	STATUS varchar(8),
	DRIVERLICENSE varchar(20),
	DRIVERLICENSEST varchar(10),
	DOD datetime,
	DEATHCERTIFICATE varchar(10),
	NATIONALITY varchar(8),
	CITIZENSHIP varchar(8))
		;
	
ALTER TABLE SBYN_PATIENTSBR ADD CONSTRAINT FK_PATIENTSBR_EUID FOREIGN KEY (EUID) REFERENCES SBYN_SYSTEMSBR(EUID);
ALTER TABLE SBYN_PATIENTSBR ADD CONSTRAINT PK_PATIENTSBR PRIMARY KEY (PATIENTID);
ALTER TABLE SBYN_PATIENTSBR ADD CONSTRAINT U_PATIENTSBR UNIQUE (EUID);

CREATE TABLE SBYN_ALIAS (
	PATIENTID VARCHAR(20),
	ALIASID VARCHAR(20),
	FIRSTNAME VARCHAR(40),
	MIDDLENAME VARCHAR(30),
	LASTNAME VARCHAR(40))
		;

ALTER TABLE SBYN_ALIAS ADD CONSTRAINT FK_ALIAS_PATIENTID FOREIGN KEY (PATIENTID) REFERENCES SBYN_PATIENT(PATIENTID);
ALTER TABLE SBYN_ALIAS ADD CONSTRAINT PK_ALIAS PRIMARY KEY (ALIASID);
ALTER TABLE SBYN_ALIAS ADD CONSTRAINT U_ALIAS UNIQUE (PATIENTID, FIRSTNAME, MIDDLENAME, LASTNAME);

CREATE TABLE SBYN_ALIASSBR (
	PATIENTID VARCHAR(20),
	ALIASID VARCHAR(20),
	FIRSTNAME VARCHAR(40),
	MIDDLENAME VARCHAR(30),
	LASTNAME VARCHAR(40))
		;

ALTER TABLE SBYN_ALIASSBR ADD CONSTRAINT FK_ALIASSBR_PATIENTID FOREIGN KEY (PATIENTID) REFERENCES SBYN_PATIENTSBR(PATIENTID);
ALTER TABLE SBYN_ALIASSBR ADD CONSTRAINT PK_ALIASSBR PRIMARY KEY (ALIASID);
ALTER TABLE SBYN_ALIASSBR ADD CONSTRAINT U_ALIASSBR UNIQUE (PATIENTID, FIRSTNAME, MIDDLENAME, LASTNAME);

CREATE TABLE SBYN_ADDRESS (
	PATIENTID VARCHAR(20),
	ADDRESSID VARCHAR(20),
	ADDRESSTYPE VARCHAR(8),
	ADDRESSLINE1 VARCHAR(40),
	ADDRESSLINE1_HOUSENO VARCHAR(10),
	ADDRESSLINE1_STDIR VARCHAR(5),
	ADDRESSLINE1_STNAME VARCHAR(40),
	ADDRESSLINE1_STPHON VARCHAR(8),
	ADDRESSLINE1_STTYPE VARCHAR(5),
	ADDRESSLINE2 VARCHAR(40),
	CITY VARCHAR(30),
	STATECODE VARCHAR(10),
	POSTALCODE VARCHAR(8),
	POSTALCODEEXT VARCHAR(4),
	COUNTY VARCHAR(20),
	COUNTRYCODE VARCHAR(20))
		;

ALTER TABLE SBYN_ADDRESS ADD CONSTRAINT FK_ADDRESS_PATIENTID FOREIGN KEY (PATIENTID) REFERENCES SBYN_PATIENT(PATIENTID);
ALTER TABLE SBYN_ADDRESS ADD CONSTRAINT PK_ADDRESS PRIMARY KEY (ADDRESSID);
ALTER TABLE SBYN_ADDRESS ADD CONSTRAINT U_ADDRESS UNIQUE (PATIENTID, ADDRESSTYPE);

CREATE TABLE SBYN_ADDRESSSBR (
	PATIENTID VARCHAR(20),
	ADDRESSID VARCHAR(20),
	ADDRESSTYPE VARCHAR(8),
	ADDRESSLINE1 VARCHAR(40),
	ADDRESSLINE1_HOUSENO VARCHAR(10),
	ADDRESSLINE1_STDIR VARCHAR(5),
	ADDRESSLINE1_STNAME VARCHAR(40),
	ADDRESSLINE1_STPHON VARCHAR(8),
	ADDRESSLINE1_STTYPE VARCHAR(5),
	ADDRESSLINE2 VARCHAR(40),
	CITY VARCHAR(30),
	STATECODE VARCHAR(10),
	POSTALCODE VARCHAR(8),
	POSTALCODEEXT VARCHAR(4),
	COUNTY VARCHAR(20),
	COUNTRYCODE VARCHAR(20))
		;

ALTER TABLE SBYN_ADDRESSSBR ADD CONSTRAINT FK_ADDRESSSBR_PATIENTID FOREIGN KEY (PATIENTID) REFERENCES SBYN_PATIENTSBR(PATIENTID);
ALTER TABLE SBYN_ADDRESSSBR ADD CONSTRAINT PK_ADDRESSSBR PRIMARY KEY (ADDRESSID);
ALTER TABLE SBYN_ADDRESSSBR ADD CONSTRAINT U_ADDRESSSBR UNIQUE (PATIENTID, ADDRESSTYPE);

CREATE TABLE SBYN_PHONE (
	PATIENTID VARCHAR(20),
	PHONEID VARCHAR(20),
	PHONETYPE VARCHAR(8),
	PHONE VARCHAR(20),
	PHONEEXT VARCHAR(6))
		;

ALTER TABLE SBYN_PHONE ADD CONSTRAINT FK_PHONE_PATIENTID FOREIGN KEY (PATIENTID) REFERENCES SBYN_PATIENT(PATIENTID);
ALTER TABLE SBYN_PHONE ADD CONSTRAINT PK_PHONE PRIMARY KEY (PHONEID);
ALTER TABLE SBYN_PHONE ADD CONSTRAINT U_PHONE UNIQUE (PATIENTID, PHONETYPE);

CREATE TABLE SBYN_PHONESBR (
	PATIENTID VARCHAR(20),
	PHONEID VARCHAR(20),
	PHONETYPE VARCHAR(8),
	PHONE VARCHAR(20),
	PHONEEXT VARCHAR(6))
		;

ALTER TABLE SBYN_PHONESBR ADD CONSTRAINT FK_PHONESBR_PATIENTID FOREIGN KEY (PATIENTID) REFERENCES SBYN_PATIENTSBR(PATIENTID);
ALTER TABLE SBYN_PHONESBR ADD CONSTRAINT PK_PHONESBR PRIMARY KEY (PHONEID);
ALTER TABLE SBYN_PHONESBR ADD CONSTRAINT U_PHONESBR UNIQUE (PATIENTID, PHONETYPE);

CREATE TABLE SBYN_SEQ_TABLE (
	SEQ_NAME VARCHAR(20),
	SEQ_COUNT NUMERIC(38));

ALTER TABLE SBYN_SEQ_TABLE ADD CONSTRAINT U_SEQ_NAME UNIQUE (SEQ_NAME);

DROP FUNCTION iF EXISTS SEQMGR;
SET @@sql_mode := '';
delimiter $
CREATE FUNCTION SEQMGR (Seq_Name_In VARCHAR(100), Chunk_Size_In INT) RETURNS INT
BEGIN
   DECLARE Count_out INT;
   SET Count_out = 0;

   UPDATE SBYN_SEQ_TABLE SET seq_count = seq_count+Chunk_Size_In WHERE seq_name = Seq_Name_In;
   SELECT seq_count-Chunk_Size_In  INTO Count_out FROM SBYN_SEQ_TABLE WHERE seq_name = Seq_Name_In;

   RETURN Count_out;
END;
$
delimiter ;

INSERT INTO SBYN_SEQ_TABLE VALUES ('EUID', 0);
INSERT INTO SBYN_SEQ_TABLE VALUES ('POTENTIALDUPLICATE', 0);
INSERT INTO SBYN_SEQ_TABLE VALUES ('TRANSACTIONNUMBER', 0);
INSERT INTO SBYN_SEQ_TABLE VALUES ('ASSUMEDMATCH', 0);
INSERT INTO SBYN_SEQ_TABLE VALUES ('AUDIT', 0);
INSERT INTO SBYN_SEQ_TABLE VALUES ('MERGE', 0);
INSERT INTO SBYN_SEQ_TABLE VALUES ('SBYN_APPL', 2);
INSERT INTO SBYN_SEQ_TABLE VALUES ('SBYN_COMMON_HEADER', 1);
INSERT INTO SBYN_SEQ_TABLE VALUES ('SBYN_COMMON_DETAIL', 1);
INSERT INTO SBYN_SEQ_TABLE VALUES ('ALIAS', 0);
INSERT INTO SBYN_SEQ_TABLE VALUES ('ALIASSBR', 0);
INSERT INTO SBYN_SEQ_TABLE VALUES ('ADDRESS', 0);
INSERT INTO SBYN_SEQ_TABLE VALUES ('ADDRESSSBR', 0);
INSERT INTO SBYN_SEQ_TABLE VALUES ('PHONE', 0);
INSERT INTO SBYN_SEQ_TABLE VALUES ('PHONESBR', 0);
INSERT INTO SBYN_SEQ_TABLE VALUES ('PATIENT', 0);
INSERT INTO SBYN_SEQ_TABLE VALUES ('PATIENTSBR', 0);

INSERT INTO SBYN_APPL (APPL_ID, CODE, DESCR, READ_ONLY, CREATE_DATE, CREATE_USERID) 
VALUES (1, 'EV', 'eView5.1', 'Y', now(), user());

COMMIT;
