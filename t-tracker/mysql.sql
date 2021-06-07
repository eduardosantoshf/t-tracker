create database t-tracker;
create user 'useraroo'@'%' identified by 'lepwd';
grant all on t-tracker.* to 'useraroo'@'%';
flush privileges;