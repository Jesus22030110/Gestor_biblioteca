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

-- Insercciones

-- INSERCCIONES

INSERT INTO autores (nombre, primer_apellido, segundo_apellido) VALUES
                                                                    ('Gabriel', 'García', 'Márquez'),
                                                                    ('Julio', 'Cortázar', NULL),
                                                                    ('Isabel', 'Allende', 'Llona'),
                                                                    ('Mario', 'Vargas', 'Llosa'),
                                                                    ('Pablo', 'Neruda', NULL);

INSERT INTO categorias (categoria, descripcion) VALUES
                                                    ('Realismo Mágico', 'Fusión de realidad y elementos fantásticos'),
                                                    ('Ciencia Ficción', 'Ambientados en futuros imaginarios o tecnología avanzada'),
                                                    ('Poesía', 'Obras en verso y lenguaje lírico'),
                                                    ('Novela Histórica', 'Ficción basada en eventos históricos'),
                                                    ('Cuento', 'Narrativa breve de ficción');

INSERT INTO editoriales (editorial) VALUES
                                        ('Alfaguara'),
                                        ('Sudamericana'),
                                        ('Planeta'),
                                        ('Seix Barral'),
                                        ('Cátedra');

INSERT INTO libros (titulo_libro, ano_publicacion, id_editorial, id_autor, id_categoria) VALUES
                                                                                             ('Cien años de soledad', 1967,
                                                                                              (SELECT id_editorial FROM editoriales WHERE editorial = 'Sudamericana'),
                                                                                              (SELECT id_autor FROM autores WHERE nombre = 'Gabriel' AND primer_apellido = 'García'),
                                                                                              (SELECT id_categoria FROM categorias WHERE categoria = 'Realismo Mágico')),

                                                                                             ('Rayuela', 1963,
                                                                                              (SELECT id_editorial FROM editoriales WHERE editorial = 'Sudamericana'),
                                                                                              (SELECT id_autor FROM autores WHERE nombre = 'Julio' AND primer_apellido = 'Cortázar'),
                                                                                              (SELECT id_categoria FROM categorias WHERE categoria = 'Novela Histórica')),

                                                                                             ('Veinte poemas de amor', 1924,
                                                                                              (SELECT id_editorial FROM editoriales WHERE editorial = 'Cátedra'),
                                                                                              (SELECT id_autor FROM autores WHERE nombre = 'Pablo' AND primer_apellido = 'Neruda'),
                                                                                              (SELECT id_categoria FROM categorias WHERE categoria = 'Poesía')),

                                                                                             ('La casa de los espíritus', 1982,
                                                                                              (SELECT id_editorial FROM editoriales WHERE editorial = 'Planeta'),
                                                                                              (SELECT id_autor FROM autores WHERE nombre = 'Isabel' AND primer_apellido = 'Allende'),
                                                                                              (SELECT id_categoria FROM categorias WHERE categoria = 'Realismo Mágico'));


INSERT INTO usuarios (nombre, primer_apellido, segundo_apellido, email, telefono, rol, contrasena) VALUES
    ('Jesus', 'Ramirez', 'Sillero', '22030110@itcelaya.edu.com', '4121605021', 2, 'AquilaVictis02@')


ALTER TABLE usuarios MODIFY COLUMN contrasena VARCHAR(255);
