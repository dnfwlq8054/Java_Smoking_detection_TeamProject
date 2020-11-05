create database smoke;

use smoke;

create table smoke(
	num		smallint	not null 	auto_increment,		-- ���� ��ȣ
	dong	smallint,						-- ��
	ho		smallint,						-- ȣ��
	wdate	datetime not null,				-- ���� ��¥
	connect varchar(10),
	primary key(num)
);

insert into smoke(dong, ho,connect, wdate) values ('101','502', '�������',now());
insert into smoke(dong, ho,connect, wdate) values ('101','101',' ',now());
insert into smoke(dong, ho,connect, wdate) values ('201','1002',' ',now());

select * from smoke;

flush privileges;
