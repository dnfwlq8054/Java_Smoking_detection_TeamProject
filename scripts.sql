create database smoke;

use smoke;

create table smoke(
	num		smallint	not null 	auto_increment,		-- 서비스 번호
	dong	smallint,						-- 동
	ho		smallint,						-- 호수
	wdate	datetime not null,				-- 감지 날짜
	connect varchar(10),
	primary key(num)
);

insert into smoke(dong, ho,connect, wdate) values ('101','502', '연결끊김',now());
insert into smoke(dong, ho,connect, wdate) values ('101','101',' ',now());
insert into smoke(dong, ho,connect, wdate) values ('201','1002',' ',now());

select * from smoke;

flush privileges;
