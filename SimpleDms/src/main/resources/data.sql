-- 샘플 데이터 입력

-- TODO: 인증 관련 데이터 등록
-- 공통코드/유형 권한 등록
INSERT INTO TB_CODE_CATEGORY VALUES(300, '권한');
INSERT INTO TB_CODE VALUES(30001, 'ROLE_USER', 300, 'Y');
INSERT INTO TB_CODE VALUES(30002, 'ROLE_ADMIN', 300, 'Y');

-- admin user 1명 최초 생성
-- id : forbob
-- password : 123456
-- email : forbob@naver.com
-- roles : ROLE_ADMIN
INSERT INTO TB_MEMBER VALUES('forbob@naver.com','$2a$10$TG1a5ywSrGNgf7/fFH.m0.EdTzHax8AGYNeAr8aIseF3DKyO0lDti','forbob','ROLE_ADMIN', 'N', TO_CHAR(SYSDATE, 'YYYY-MM-DD HH24:MI:SS'),NULL, NULL);

COMMIT;