create database deliveries-engine;
create user 'useraroo'@'%' identified by 'lepwd';
grant all on deliveries-engine.* to 'useraroo'@'%';
flush privileges;