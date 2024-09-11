-- 사용자 생성
CREATE USER logistics_db WITH PASSWORD 'logistics_db';

-- 모든 데이터베이스와 테이블에 대해 권한 부여
GRANT ALL PRIVILEGES ON DATABASE logistics TO logistics_db;