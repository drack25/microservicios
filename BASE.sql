SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

ALTER SCHEMA public OWNER TO postgres;

SET default_tablespace = '';

SET default_table_access_method = heap;

CREATE TABLE public.clientes (
    cliente_id bigint NOT NULL,
    direccion character varying(255) NOT NULL,
    edad integer NOT NULL,
    genero character varying(255) NOT NULL,
    identificacion character varying(255) NOT NULL,
    nombre character varying(255) NOT NULL,
    telefono character varying(255) NOT NULL,
    contrasenia character varying(255) NOT NULL,
    estado boolean NOT NULL
);

ALTER TABLE public.clientes OWNER TO postgres;

ALTER TABLE public.clientes ALTER COLUMN cliente_id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.clientes_cliente_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);

CREATE TABLE public.cuentas (
    numero_cuenta character varying(255) NOT NULL,
    cliente_id bigint NOT NULL,
    estado boolean NOT NULL,
    saldo_inicial numeric(38,2) NOT NULL,
    tipo_cuenta character varying(255) NOT NULL
);

ALTER TABLE public.cuentas OWNER TO postgres;

CREATE TABLE public.movimientos (
    id bigint NOT NULL,
    fecha date NOT NULL,
    numero_cuenta character varying(255) NOT NULL,
    saldo numeric(38,2) NOT NULL,
    tipo_movimiento character varying(255) NOT NULL,
    valor numeric(38,2) NOT NULL
);

ALTER TABLE public.movimientos OWNER TO postgres;

ALTER TABLE public.movimientos ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.movimientos_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);

ALTER TABLE ONLY public.clientes
    ADD CONSTRAINT clientes_pkey PRIMARY KEY (cliente_id);

ALTER TABLE ONLY public.cuentas
    ADD CONSTRAINT cuentas_pkey PRIMARY KEY (numero_cuenta);

ALTER TABLE ONLY public.movimientos
    ADD CONSTRAINT movimientos_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.clientes
    ADD CONSTRAINT ukmvqpaay4xno9pnlalro0awadi UNIQUE (identificacion);

ALTER TABLE ONLY public.movimientos
    ADD CONSTRAINT fko5koqjuegcyto02t8ytlf3y80 FOREIGN KEY (numero_cuenta) REFERENCES public.cuentas(numero_cuenta);

REVOKE USAGE ON SCHEMA public FROM PUBLIC;
GRANT ALL ON SCHEMA public TO PUBLIC;