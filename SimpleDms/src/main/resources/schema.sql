-- Table , 시퀀스 등 구조 정의

-- 공통코드 테이블은 시퀀스는 사용하지 않음
-- 공통코드 테이블의 등록된 코드는 향후에 않쓰이더라도 삭제/수정하지 않음 : 데이터가 많지않아 오버헤드가 없음
DROP TABLE TB_CODE_CATEGORY CASCADE CONSTRAINT;
DROP TABLE TB_CODE CASCADE CONSTRAINT;


-- 코드성 테이블 : 공통 코드 유형 테이블
CREATE TABLE TB_CODE_CATEGORY
(
    CATEGORY_ID   NUMBER NOT NULL PRIMARY KEY,
    CATEGORY_NAME VARCHAR2(255)
);

-- 코드성 테이블 : 공통 코드 테이블
CREATE TABLE TB_CODE
(
    CODE_ID     NUMBER NOT NULL PRIMARY KEY,
    CODE_NAME   VARCHAR2(255),
    CATEGORY_ID NUMBER NOT NULL
        CONSTRAINT FK_CODE_CATEGORY_CODE REFERENCES TB_CODE_CATEGORY (CATEGORY_ID),
    USE_YN      VARCHAR(1) DEFAULT 'Y'
);

-- TODO: 인증관련 테이블 정의
-- 유저 테이블
-- login table ddl
DROP TABLE TB_MEMBER CASCADE CONSTRAINTS;

CREATE TABLE TB_MEMBER
(
    EMAIL       VARCHAR2(1000) NOT NULL PRIMARY KEY, -- id (email)
    PASSWORD    VARCHAR2(1000),                                         -- 암호
    NAME        VARCHAR2(1000),                                         -- 유저명
    CODE_NAME   VARCHAR2(1000),                                         -- 권한코드명(ROLE_USER, ROLE_ADMIN)
    DELETE_YN   VARCHAR2(1) DEFAULT 'N',
    INSERT_TIME VARCHAR2(255),
    UPDATE_TIME VARCHAR2(255),
    DELETE_TIME VARCHAR2(255)
);