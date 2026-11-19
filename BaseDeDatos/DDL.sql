-- public.especies definition

-- Drop table

-- DROP TABLE public.especies;

CREATE TABLE public.especies (
	id serial4 NOT NULL,
	nombre varchar NOT NULL,
	CONSTRAINT especies_pk PRIMARY KEY (id)
);


-- public.sexo definition

-- Drop table

-- DROP TABLE public.sexo;

CREATE TABLE public.sexo (
	nombre varchar NOT NULL,
	id serial4 NOT NULL,
	CONSTRAINT sexo_pk PRIMARY KEY (id)
);


-- public.mascotas definition

-- Drop table

-- DROP TABLE public.mascotas;

CREATE TABLE public.mascotas (
	nombre varchar NOT NULL,
	id serial4 NOT NULL,
	edad int4 NOT NULL,
	raza varchar NOT NULL,
	responsable varchar NOT NULL,
	observaciones varchar NULL,
	sexo int4 NOT NULL,
	especie int4 NOT NULL,
	CONSTRAINT mascotas_pk PRIMARY KEY (id),
	CONSTRAINT mascotas_especies_fk FOREIGN KEY (especie) REFERENCES public.especies(id) ON DELETE CASCADE,
	CONSTRAINT mascotas_sexo_fk FOREIGN KEY (sexo) REFERENCES public.sexo(id) ON DELETE CASCADE
);