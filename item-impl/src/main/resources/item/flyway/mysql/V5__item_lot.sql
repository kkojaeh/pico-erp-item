create table itm_item_lot (
	id varchar(50) not null,
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
	item_id varchar(50),
	primary key (id)
) engine=InnoDB;

alter table itm_item_lot
	add constraint ITM_ITEM_LOT_CODE_IDX unique (code);

alter table itm_item_lot
	add constraint FKaov84hg84yirrdsx31v60p435 foreign key (item_id)
	references itm_item (id);
