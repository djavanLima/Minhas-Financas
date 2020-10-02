package br.com.djavan.minhasfinancas.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.djavan.minhasfinancas.api.dto.UsuarioDto;
import br.com.djavan.minhasfinancas.exception.RegraNegocioException;
import br.com.djavan.minhasfinancas.model.entity.Usuario;
import br.com.djavan.minhasfinancas.service.UsuarioService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/usuario")
@RequiredArgsConstructor
public class UsuarioController {

	private final  UsuarioService service;
	
	
	@PostMapping("/autenticar")
	public ResponseEntity autenticar(@RequestBody UsuarioDto dto) {
		try {
			Usuario usuarioAutenticado = service.autenticar(dto.getEmail(), dto.getSenha());
			return ResponseEntity.ok(usuarioAutenticado);
		}catch(Exception e)
		{
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		
	}
	
	
	@PostMapping
	public ResponseEntity salvar( @RequestBody UsuarioDto dto)
	{
		Usuario usuario = Usuario.builder().nome(dto.getNome()).email(dto.getEmail()).senha(dto.getSenha()).build();
	
	
	try {
		Usuario usuarioSalvo=service.salvarUsuario(usuario);
		return new ResponseEntity(usuarioSalvo, HttpStatus.CREATED);
	}catch(RegraNegocioException e) {
		return ResponseEntity.badRequest().body(e.getMessage());
	}
	
	
	
	}
	
	
}
