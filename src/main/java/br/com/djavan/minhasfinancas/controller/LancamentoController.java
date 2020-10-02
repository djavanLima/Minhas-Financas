package br.com.djavan.minhasfinancas.controller;


import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import br.com.djavan.minhasfinancas.api.dto.LancamentoDto;
import br.com.djavan.minhasfinancas.exception.RegraNegocioException;
import br.com.djavan.minhasfinancas.model.entity.Lancamento;
import br.com.djavan.minhasfinancas.model.entity.Usuario;
import br.com.djavan.minhasfinancas.model.enums.StatusLancamento;
import br.com.djavan.minhasfinancas.model.enums.TipoLancamento;
import br.com.djavan.minhasfinancas.service.LancamentoService;
import br.com.djavan.minhasfinancas.service.UsuarioService;

@RestController
@RequestMapping("/api/lancamento")
public class LancamentoController {

//	@Autowired
	private LancamentoService service;
	//ou adiciono construtor
	
	private UsuarioService usuarioService;
	
	public LancamentoController(LancamentoService service)
	{
		this.service=service;
	}
	
	@PostMapping
	public ResponseEntity salvar(@RequestBody LancamentoDto dto)
	{
		
		try {
		Lancamento entidade = converter(dto);
		entidade = service.salvar(entidade);
		return new ResponseEntity(entidade,HttpStatus.CREATED);
		}catch(RegraNegocioException e) {
		return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	
	@GetMapping
	public ResponseEntity buscar(
								@RequestParam(value="descricao", required=false) String descricao,
								@RequestParam(value="mes", required=false) Integer mes,
								@RequestParam(value="ano", required=false) Integer ano,
								@RequestParam("usuario") Long idUsuario
								){
		Lancamento lancamentoFiltro= new Lancamento();
		lancamentoFiltro.setDescricao(descricao);
		lancamentoFiltro.setMes(mes);
		lancamentoFiltro.setAno(ano);
		lancamentoFiltro.setAno(ano);
		
		Optional<Usuario> usuario = usuarioService.obterPorId(idUsuario);
		if(usuario.isPresent()) {
			return ResponseEntity.badRequest().body("Não foi possivel realizar a consulta. Usuário não encontrado para o id informado.");
		}
		else
		{
			lancamentoFiltro.setUsuario(usuario.get());
		}
		List<Lancamento> lancamentos =service.buscar(lancamentoFiltro);
		return ResponseEntity.ok(lancamentos);
	}
	
	
	
	
	
	
	@PutMapping("{id}")
	public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody LancamentoDto dto)
	{
		
		return service.obterPorId(id).map(entity-> {
			try {
			Lancamento lancamento = converter(dto);
					lancamento.setId(entity.getId());
					service.atualizar(lancamento);
					return ResponseEntity.ok(lancamento);
			}catch(RegraNegocioException e) {
				
			return ResponseEntity.badRequest().body(e.getMessage());
				
			}
		}).orElseGet(()->
		new ResponseEntity("Lancamento não encontrado na base de dados", HttpStatus.BAD_REQUEST));
	}
	
	
	
	@DeleteMapping("{id}")
	public ResponseEntity deletar(@PathVariable("id") Long id) {
		
		return service.obterPorId(id).map(entidade->
		{
			service.deletar(entidade);
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}).orElseGet(()-> new ResponseEntity("Lancamento não encontrado na base de dados.", HttpStatus.BAD_REQUEST));
		
		
	}
	
	
	
	
	
	
	private Lancamento converter(LancamentoDto dto) {

		Lancamento lancamento = new Lancamento();
		lancamento.setDescricao(dto.getDescricao());
		lancamento.setAno(dto.getAno());
		lancamento.setMes(dto.getMes());
		lancamento.setValor(dto.getValor());
		usuarioService.obterPorId(dto.getUsuario());
		
		Usuario usuario = usuarioService
				.obterPorId(dto.getUsuario())
				.orElseThrow(() -> new RegraNegocioException("Usuário não encontrado para o Id informado."));
		
		
		lancamento.setUsuario(usuario);
		lancamento.setTipo(TipoLancamento.valueOf(dto.getTipo()));
		lancamento.setStatus(StatusLancamento.valueOf(dto.getStatus()));
		
		return lancamento;
				
	}
}
