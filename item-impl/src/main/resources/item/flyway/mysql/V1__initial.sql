create table itm_item (
	id binary(16) not null,
	attachment_id binary(16),
	base_unit_cost decimal(19,2),
	category_id binary(16),
	code varchar(20),
	created_by_id varchar(50),
	created_by_name varchar(50),
	created_date datetime,
	customer_id varchar(50),
	customer_name varchar(50),
	description varchar(200),
	external_code varchar(100),
	last_modified_by_id varchar(50),
	last_modified_by_name varchar(50),
	last_modified_date datetime,
	name varchar(50),
	purchasable bit,
	salable bit,
	spec_type_id varchar(200),
	status varchar(20),
	type varchar(20),
	unit varchar(20),
	primary key (id)
) engine=InnoDB;

create table itm_item_category (
	id binary(16) not null,
	code varchar(20),
	created_by_id varchar(50),
	created_by_name varchar(50),
	created_date datetime,
	description varchar(200),
	item_count decimal(19,2),
	id_path varchar(500),
	last_modified_by_id varchar(50),
	last_modified_by_name varchar(50),
	last_modified_date datetime,
	name varchar(30),
	parent_id binary(16),
	name_path varchar(500),
	primary key (id)
) engine=InnoDB;

create table itm_item_lot (
	id binary(16) not null,
	code varchar(20),
	created_by_id varchar(50),
	created_by_name varchar(50),
	created_date datetime,
	expiration_date datetime,
	expired bit,
	expired_date datetime,
	item_id binary(16),
	last_modified_by_id varchar(50),
	last_modified_by_name varchar(50),
	last_modified_date datetime,
	primary key (id)
) engine=InnoDB;

create table itm_item_spec (
	id binary(16) not null,
	base_unit_cost decimal(19,2),
	created_by_id varchar(50),
	created_by_name varchar(50),
	created_date datetime,
	item_id binary(16),
	last_modified_by_id varchar(50),
	last_modified_by_name varchar(50),
	last_modified_date datetime,
	summary varchar(100),
	variables longtext,
	primary key (id)
) engine=InnoDB;

create index IDX8sp72d9xqr7qwj79b96inehev
	on itm_item (category_id);

alter table itm_item
	add constraint UK7a7jw9j0i21qdykeeb8d0cteu unique (code);

alter table itm_item_category
	add constraint UKrvgdyhryc82aelc39soqgo66r unique (code);

create index IDXokorm3uurbo6n4oucu5kqlfg5
	on itm_item_lot (item_id);

alter table itm_item_lot
	add constraint UKfh2bswracgcid7iy7mijjxn1d unique (item_id,code);

create index IDXewp0vaxcl5309pjman88w652h
	on itm_item_spec (item_id);
