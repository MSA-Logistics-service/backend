-- 사용자 생성
CREATE USER hub_db WITH PASSWORD 'hub_db';

-- 모든 데이터베이스와 테이블에 대해 권한 부여
GRANT ALL PRIVILEGES ON DATABASE hub TO hub_db;