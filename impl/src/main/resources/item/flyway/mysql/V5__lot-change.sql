DROP INDEX UKfh2bswracgcid7iy7mijjxn1d ON itm_item_lot;
DROP INDEX IDXokorm3uurbo6n4oucu5kqlfg5 ON itm_item_lot;
ALTER TABLE itm_item_lot CHANGE code lot_code varchar(20);
ALTER TABLE itm_item_lot ADD spec_code varchar(20) DEFAULT 'N/A' NOT NULL;

alter table itm_item_lot
	add constraint UKdgsel3wt309ro3dykitalyayr unique (item_id,spec_code,lot_code);
