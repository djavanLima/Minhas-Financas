package br.com.djavan.minhasfinancas.service;

import org.springframework.stereotype.Service;

import br.com.djavan.minhasfinancas.model.entity.Usuario;




public interface UsuarioService {

	Usuario autenticar(String email, String senha);
	
	Usuario salvarUsuario(Usuario usuario);
	
	void validarEmail(String email);
	
}
