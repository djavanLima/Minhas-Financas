package br.com.djavan.minhasfinancas.model.repository;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.djavan.minhasfinancas.model.entity.Lancamento;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long>{

	
	@Query(value=" select sum(l.valor) from lancamento l join l.usuario u where u.id =:idUsuario and l.tipo :=tipo")
	BigDecimal obterSaldoPorTipoLancamentoEUsuario();
	
	
	
	
}
