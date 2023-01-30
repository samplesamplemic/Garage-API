insert into car (brand, vehicle_year, engine_capacity, doors, fuel) values ( 'Alfa Romeo', 2011, 1300, 3, 'DIESEL');
--the enum with @Enumerated(EnumType.STRING) need to be insert in UpperCase, i.e. 'DIESEL', to be get;
insert into moto (brand, vehicle_year, engine_capacity, times) values ('Kawasaki', 2013, 300, 4);
insert into van (brand, vehicle_year, engine_capacity, cargo_capacity) values ('Mercedes', 2010, 3000, 700);