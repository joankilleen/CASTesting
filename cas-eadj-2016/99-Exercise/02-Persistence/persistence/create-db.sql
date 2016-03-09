create table Album (id bigint generated by default as identity, name varchar(255), price double not null, primary key (id))
create table MusicOrder (id bigint generated by default as identity, city varchar(255), country varchar(255), email varchar(255), finalAmount double, firstName varchar(255), lastName varchar(255), street varchar(255), zip varchar(255), primary key (id))
create table OrderItem (id bigint generated by default as identity, album_id bigint, orderItems_id bigint, primary key (id))
alter table OrderItem add constraint FK_t8hxdxv9pmxf02jtrxt7d4xf8 foreign key (album_id) references Album
alter table OrderItem add constraint FK_3wwdijvgaj0j603l45m31q8xj foreign key (orderItems_id) references MusicOrder