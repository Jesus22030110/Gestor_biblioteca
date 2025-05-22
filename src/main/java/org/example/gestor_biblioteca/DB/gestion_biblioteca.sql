create table autores(
    id_autor int auto_increment primary key ,
    nombre varchar(50) not null ,
    primer_apellido varchar(50) ,
    segundo_apellido varchar(50)
);

create table categorias(
    id_categoria int auto_increment primary key ,
    categoria varchar(50) not null ,
    descripcion varchar(100)
);

create table editoriales(
    id_editorial int auto_increment primary key ,
    editorial varchar(50) not null
);

create table libros(
    id_libro int auto_increment primary key ,
    titulo_libro varchar(100) not null ,
    ano_publicacion int not null ,
    id_editorial int references editoriales(id_editorial),
    id_autor int references autores(id_autor),
    id_categoria int references categorias(id_categoria)
);

create table usuarios(
    id_usuario int auto_increment primary key ,
    nombre varchar(50) not null ,
    primer_apellido varchar(50) not null ,
    segundo_apellido varchar(50),
    email varchar(100) not null ,
    telefono varchar(13) not null,
    rol int not null ,
    contrasena varchar(32) not null
);

create table prestamos(
    id_prestamos int auto_increment primary key ,
    id_usuario int references usuarios(id_usuario),
    id_libro int references libros(id_libro),
    fecha_prestamo date not null ,
    fecha_limite_prestamo date not null ,
    fecha_devolucion date
);