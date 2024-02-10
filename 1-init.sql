-- Coloque scripts iniciais aqui

CREATE UNLOGGED TABLE cliente (
	id int4 NOT NULL,
	limite int8 NULL,
	saldo int8 DEFAULT 0,
	CONSTRAINT cliente_pkey PRIMARY KEY (id)
);

CREATE UNLOGGED TABLE transacao (
	id SERIAL NOT NULL,
	descricao varchar(255) NULL,
	id_cliente int4 NOT NULL,
	realizada_em timestamptz(6) NULL,
	tipo varchar(255) NULL,
	valor int8 NULL,
	CONSTRAINT transacao_pkey PRIMARY KEY (id)
);

INSERT INTO cliente (id, limite)
VALUES
(1, 1000 * 100),
(2, 800 * 100),
(3, 10000 * 100),
(4, 100000 * 100),
(5, 5000 * 100);


--select ("cred" - "deb") as "saldo", t1.id_cliente from
--(select sum(valor) as "cred", id_cliente from transacao where tipo='c' group by id_cliente) as t1
--join (select sum(valor) as "deb", id_cliente from transacao where tipo='d' group by id_cliente) as t2
--on t1.id_cliente = t2.id_cliente
--order by t1.id_cliente asc
