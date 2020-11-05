create table smoke(
	num		smallint	not null 	auto_increment,		-- 서비스 번호
	dong	smallint,						-- 동
	ho		smallint,						-- 호수
	wdate	datetime not null,				-- 감지 날짜
	connect varchar(10),
	primary key(num)
	

);
-- -------------테이블 생성 --------------

drop table smoke;


insert into smoke(dong, ho,connect, wdate) values ('1','502', '연결끊김',now());
insert into smoke(dong, ho,connect, wdate) values ('1','101',' ',now());
insert into smoke(dong, ho,connect, wdate) values ('2','1002',' ',now());


select * from smoke;

select * from smoke where dong=101;