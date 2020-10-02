package br.com.djavan.minhasfinancas.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.djavan.minhasfinancas.exception.RegraNegocioException;
import br.com.djavan.minhasfinancas.model.entity.Lancamento;
import br.com.djavan.minhasfinancas.model.enums.StatusLancamento;
import br.com.djavan.minhasfinancas.model.repository.LancamentoRepository;
import br.com.djavan.minhasfinancas.service.LancamentoService;


@Service
public class LancamentoServiceImpl implements LancamentoService {

	
	private LancamentoRepository repository;
	
	public LancamentoServiceImpl (LancamentoRepository repository)
	{
		this.repository=repository;
	}
	
	
	@Override
	@Transactional
	public Lancamento salvar(Lancamento lancamento) {
		validarLancamento(lancamento);
		lancamento.setStatus(StatusLancamento.PENDENTE);
		return repository.save(lancamento);
	}

	@Override
	public Lancamento atualizar(Lancamento lancamento) {
		Objects.requireNonNull(lancamento.getId());// verifica se tem id e se não tem lança notnull
		validarLancamento(lancamento);
		return repository.save(lancamento);
	}

	@Override
	public void deletar(Lancamento lancamento) {
		Objects.requireNonNull(lancamento.getId());
		repository.delete(lancamento);
		
	}

	@Override
	@Transactional(readOnly = true)
	public List<Lancamento> buscar(Lancamento lancamentoFiltro) {
		
		Example example = Example.of(lancamentoFiltro,
				ExampleMatcher.matching()
				.withIgnoreCase()
				.withStringMatcher(StringMatcher.CONTAINING)
				
				);
		
		return repository.findAll(example);
	}

	@Override
	public void atualizarStatus(Lancamento lancamento, StatusLancamento status) {
		
		lancamento.setStatus(status);
		atualizar(lancamento);
	}


	@Override
	public void validarLancamento(Lancamento lancamento) {
		if(lancamento.getDescricao()== null || lancamento.getDescricao().trim().equals(""))
		{
			throw new RegraNegocioException("Informe uma descrição válida.");
		}
		
		if(lancamento.getMes()==null || lancamento.getMes() <1 ||lancamento.getMes()>12)
		{
			throw new RegraNegocioException("Informe um Mês válido");
		}
		
		if(lancamento.getAno()==null || lancamento.getAno().toString().length()!=4 )
		{
			throw new RegraNegocioException("Informe um Ano válido");
		}
		
		if(lancamento.getUsuario()==null || lancamento.getUsuario().getId()==null) {
			throw new RegraNegocioException("Informe um Usuário válido");
		}
		
		if(lancamento.getValor()==null || lancamento.getValor().compareTo(BigDecimal.ZERO) < 1) {
			throw new RegraNegocioException("Informe um Valor válido");
		}
		
		if(lancamento.getTipo()==null) {
			throw new RegraNegocioException("Informe um tipo de Lançamento.");
		}
		
		
	}


	@Override
	public Optional<Lancamento> obterPorId(Long id) {
		// TODO Auto-generated method stub
		return repository.findById(id);
	}

}
