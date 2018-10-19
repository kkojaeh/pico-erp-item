create table itm_item (
	id binary(16) not null,
	attachment_id varchar(50),
	base_unit_cost decimal(19,2),
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
	sellable bit,
	spec_type_id varchar(200),
	status varchar(20),
	type varchar(20),
	unit varchar(20),
	category_id binary(16),
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
	parent_id binary(50),
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
	last_modified_by_id varchar(50),
	last_modified_by_name varchar(50),
	last_modified_date datetime,
	item_id binary(16),
	primary key (id)
) engine=InnoDB;

create table itm_item_spec (
	id binary(16) not null,
	base_unit_cost decimal(19,2),
	created_by_id varchar(50),
	created_by_name varchar(50),
	created_date datetime,
	last_modified_by_id varchar(50),
	last_modified_by_name varchar(50),
	last_modified_date datetime,
	summary varchar(100),
	variables longtext,
	item_id binary(16),
	primary key (id)
) engine=InnoDB;

alter table itm_item
	add constraint ITM_ITEM_CODE_IDX unique (code);

alter table itm_item_category
	add constraint ITM_ITEM_CATEGORY_CODE_IDX unique (code);

alter table itm_item_category
	add constraint ITM_ITEM_CATEGORY_KEY_IDX unique (id_path);

alter table itm_item_lot
	add constraint ITM_ITEM_LOT_CODE_IDX unique (code);

alter table itm_item
	add constraint FKb20bckjup95e37ayh16ucvdkn foreign key (category_id)
	references itm_item_category (id);

alter table itm_item_lot
	add constraint FKaov84hg84yirrdsx31v60p435 foreign key (item_id)
	references itm_item (id);

alter table itm_item_spec
	add constraint FKd4ik0l24t1ngb52ogx4h8mtbt foreign key (item_id)
	references itm_item (id);
