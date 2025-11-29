create table veiculos (
    id bigint not null auto_increment,
    placa varchar(10) not null,
    chassi varchar(20) not null,
    modelo varchar(50) not null,
    marca varchar(50) not null,
    ano int not null,
    kilometragem int not null,
    primary key (id)
);