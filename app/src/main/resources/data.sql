insert into roles(id, name) values(1, 'admin');
insert into roles(id, name) values(2, 'mol');

insert into users(id, username, password, role_id) values(1, 'admin', '123456', 1);
insert into users(id, username, password, role_id) values(2, 'mol', '123', 2);

insert into product_types(id, name) values(1, 'ДМА');
insert into product_types(id, name) values(2, 'МА');

insert into clients(id, name, family_name) values(1, 'Иван', 'Петров');
insert into clients(id, name, family_name) values(2, 'Димитър', 'Медведев');
insert into clients(id, name, family_name) values(3, 'Петър', 'Стоянов');


insert into products(id, amorthization_index, description, is_deleted, name, year, type_id)
values(1, 1, 'хубаво описание', true, 'Син диван', 2020, 1);

insert into products(id, amorthization_index, description, is_deleted, name, year, type_id)
values(2, 2, 'описание за мебели', false, 'Немски стол', 2022, 2);

insert into products(id, amorthization_index, description, is_deleted, name, year, type_id)
values(3, 1, 'Малък син стол', false, 'Въртящ стол', 2021, 2);

insert into products(id, amorthization_index, description, is_deleted, name, year, type_id)
values(4, 2, 'Маса направена през социализма', false, 'Дървена маса', 1980, 1);

insert into products(id, amorthization_index, description, is_deleted, name, year, type_id)
values(5, 2, 'Поставка за телефон със залепващ механизъм', false, 'Поставка за телефон', 2018, 1);


insert into clients_products(client_id, products_id) values (1, 1);
insert into clients_products(client_id, products_id) values (1, 2);
insert into clients_products(client_id, products_id) values (2, 3);
insert into clients_products(client_id, products_id) values (2, 4);
insert into clients_products(client_id, products_id) values (3, 5);