CREATE TABLE ingredients(
	iid int, 
	name text, 
	price real,
	constraint pk_iid primary key(iid)
);

INSERT INTO ingredients VALUES (1,'pomme de terre',1);
INSERT INTO ingredients VALUES (2,'poivrons',1);
INSERT INTO ingredients VALUES (3,'chorizo',1);
INSERT INTO ingredients VALUES (4,'lardons',1);
INSERT INTO ingredients VALUES (5,'aubergines',1);
INSERT INTO ingredients VALUES (6,'champignons',1);
INSERT INTO ingredients VALUES (7,'ananas',1);
INSERT INTO ingredients VALUES (8,'fromage',1);
INSERT INTO ingredients VALUES (9,'tomates',1);
INSERT INTO ingredients VALUES (10,'olives',1);
INSERT INTO ingredients VALUES (11,'oeufs',1);

create table pizzas( 
	pid int, 
	name text, 
	type text, 
	price real,
	constraint pk_pid primary key(pid)
);

insert into pizzas values (1,'reine','brisé',9);
insert into pizzas values (2,'margarita','brisé',8);
insert into pizzas values (3,'napolitaine','brisé',10);

CREATE TABLE confection(
	pizza int, 
	ings int, 
	finalPrice real,
	constraint pk_confection primary key(pizza, ings),
	constraint fk_pizza foreign key(pizza) references pizzas(pid) on update cascade on delete cascade,
	constraint fk_ings foreign key(ings) references ingredients(iid) on update cascade on delete cascade
);

INSERT INTO confection VALUES (1,4);
INSERT INTO confection VALUES (1,9);
INSERT INTO confection VALUES (1,8);
INSERT INTO confection VALUES (1,6);
INSERT INTO confection VALUES (2,8);
INSERT INTO confection VALUES (2,9);

CREATE TABLE utilisateur(
	uid int,
	login text,
	pwd text,
	primary key(uid)
);

INSERT INTO utilisateur VALUES (1,'maxime','1234');
INSERT INTO utilisateur VALUES (2,'loic','loic');

CREATE TABLE commande(
	cid int,
	uid int,
	date test,
	primary key(cid),
	foreign key(uid) references utilisateur(uid) on update cascade on delete cascade
);

INSERT INTO commande VALUES (1,1,'2023-02-17');
INSERT INTO commande VALUES (2,2,'2023-02-18');

CREATE TABLE commandefini(
	cid int, 
	pid int, 
	finalPrice real,
	primary key(cid, pid),
	foreign key(cid) references commande(cid) on update cascade on delete cascade,
	foreign key(pid) references pizzas(pid) on update cascade on delete cascade
);

INSERT INTO commandefini VALUES (1,1);
INSERT INTO commandefini VALUES (1,2);
INSERT INTO commandefini VALUES (2,2);
