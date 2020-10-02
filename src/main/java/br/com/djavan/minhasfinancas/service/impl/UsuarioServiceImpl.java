package br.com.djavan.minhasfinancas.service.impl;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.djavan.minhasfinancas.exception.ErroDeAutenticacaoException;
import br.com.djavan.minhasfinancas.exception.RegraNegocioException;
import br.com.djavan.minhasfinancas.model.entity.Usuario;
import br.com.djavan.minhasfinancas.model.repository.UsuarioRepository;
import br.com.djavan.minhasfinancas.service.UsuarioService;

@Service
public class UsuarioServiceImpl implements UsuarioService {

	
	private UsuarioRepository repository;
	
	@Autowired // nao precisa mais posso deixar devido a implementação do construtor
	public UsuarioServiceImpl(UsuarioRepository repository) {
		super();
		this.repository = repository;
	}


	
	
	
	
	@Override
	public Usuario autenticar(String email, String senha) { // metodo de autenticar usuario por email
		// TODO Auto-generated method stub
		Optional<Usuario> usuario = repository.findByEmail(email);
		
		
		if(!usuario.isPresent()) {
			throw new ErroDeAutenticacaoException("Usuario não encontrado para o email informado");
		}
		
		if(!usuario.get().getSenha().equals(senha)) {
			throw new ErroDeAutenticacaoException("Senha invalida");
		}
		
		return usuario.get();
		
		
	}

	
	
	@Override
	@Transactional
	public Usuario salvarUsuario(Usuario usuario) {
		validarEmail(usuario.getEmail());
		return repository.save(usuario);
	}

	@Override
	public void validarEmail(String email) {
	boolean existe = repository.existsByEmail(email);
	if(existe) {
		throw new RegraNegocioException("Já existe um usuário cadastrado com este email");
	}
		
	}

}
