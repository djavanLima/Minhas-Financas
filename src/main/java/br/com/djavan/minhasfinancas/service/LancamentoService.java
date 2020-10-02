package br.com.djavan.minhasfinancas.service;

import java.util.List;

import br.com.djavan.minhasfinancas.model.entity.Lancamento;
import br.com.djavan.minhasfinancas.model.enums.StatusLancamento;

public interface LancamentoService {

	
	Lancamento salvar (Lancamento lancamento);
	
	Lancamento atualizar(Lancamento lancamento);
	
	void deletar(Lancamento lancamento);
	
	List<Lancamento> buscar(Lancamento lancamentoFiltro);
	
	void atualizarStatus(Lancamento lancamento, StatusLancamento status);
	
	void validarLancamento(Lancamento lancamento);
}