-- Coloque scripts iniciais aqui

CREATE TABLE cliente (
	id int4 NOT NULL,
	limite int8 NULL,
	saldo int8 DEFAULT 0,
	CONSTRAINT cliente_pkey PRIMARY KEY (id)
);

CREATE TABLE transacao (
	id int4 NOT NULL,
	descricao varchar(255) NULL,
	id_cliente int4 NOT NULL,
	realizada_em timestamptz(6) NULL,
	tipo varchar(255) NULL,
	valor int8 NULL,
	CONSTRAINT transacao_pkey PRIMARY KEY (id)
);

CREATE SEQUENCE transacao_seq INCREMENT BY 50;

INSERT INTO cliente (id, limite)
VALUES
(1, 1000 * 100),
(2, 800 * 100),
(3, 10000 * 100),
(4, 100000 * 100),
(5, 5000 * 100);
