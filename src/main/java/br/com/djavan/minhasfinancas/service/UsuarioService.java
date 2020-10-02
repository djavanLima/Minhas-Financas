package br.com.djavan.minhasfinancas.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import br.com.djavan.minhasfinancas.model.entity.Usuario;




public interface UsuarioService {

	Usuario autenticar(String email, String senha);
	
	Usuario salvarUsuario(Usuario usuario);
	
	void validarEmail(String email);
	
	Optional<Usuario> obterPorId(Long id) ;
		
	
}
