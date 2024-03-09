create or replace function cria_transacao(id_cliente int, t_valor int, t_tipo varchar, t_descricao varchar) returns table(l int, s int)
as $$
declare
	novoSaldo int;
	novoValor int;
	client cliente%rowtype;
begin
	select * into client from cliente c where c.id = id_cliente for update;
	if not found then
		return query select -1, -1;
	end if;
	if t_tipo = 'd' then
		novoValor := t_valor * -1;
	else
		novoValor := t_valor;
	end if;
	novoSaldo := client.saldo + novoValor;
	if (abs(novoSaldo) - client.limite) > 0 and t_tipo = 'd' then
        return query select -2, -2;
	else
		insert into transacao(id,valor, tipo, descricao, realizada_em, id_cliente) values(default,t_valor, t_tipo, t_descricao, now(), id_cliente);
		return query update cliente c set saldo = novoSaldo where id=id_cliente returning c.limite, c.saldo;
	end if;
end
$$
language plpgsql;
