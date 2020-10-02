package br.com.djavan.minhasfinancas.service;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.djavan.minhasfinancas.exception.ErroDeAutenticacaoException;
import br.com.djavan.minhasfinancas.exception.RegraNegocioException;
import br.com.djavan.minhasfinancas.model.entity.Usuario;
import br.com.djavan.minhasfinancas.model.repository.UsuarioRepository;
import br.com.djavan.minhasfinancas.service.impl.UsuarioServiceImpl;


@RunWith(SpringRunner.class)
@ActiveProfiles("test")

public class UsuarioServiceTest {
	
	@SpyBean
	UsuarioServiceImpl service;
	
	@MockBean
	UsuarioRepository repository;
	

	
	
	@Test
	public void deveSalvarUmUsuario()
	{
		//cenário
		Mockito.doNothing().when(service).validarEmail(Mockito.anyString());
		Usuario usuario = Usuario.builder()
								 .id(1l)
								 .nome("nome")
								 .email("email@email.com")
								 .senha("senha").build();
		
			Mockito.when(repository.save(Mockito.any(Usuario.class))).thenReturn(usuario);
			//ação
			Usuario usuarioSalvo = service.salvarUsuario(new Usuario());
		//verificacao
			Assertions.assertThat(usuarioSalvo).isNotNull();
			Assertions.assertThat(usuarioSalvo.getId()).isEqualTo(1l);
			Assertions.assertThat(usuarioSalvo.getNome()).isEqualTo("nome");
			Assertions.assertThat(usuarioSalvo.getEmail()).isEqualTo("email@email.com");
			Assertions.assertThat(usuarioSalvo.getSenha()).isEqualTo("senha");
	}
	
	@Test(expected = RegraNegocioException.class)
	public void naoDeveSalvarUmUsuarioComEmailJaCadastrado()
	{
		//cenario
		String email="email@email.com";
		Usuario usuario = Usuario.builder().email(email).build();
		Mockito.doThrow(RegraNegocioException.class).when(service).validarEmail(email);
		//acao
		service.salvarUsuario(usuario);
		//verificar
		Mockito.verify(repository,Mockito.never()).save(usuario);
	}
	
	@Test(expected = Test.None.class)
	public void deveAutenticarUmUsuarioComSucesso()
	{
		//cenario
		String email="email@email.com";
		String senha = "senha";
		
		
		Usuario usuario = Usuario.builder().email(email).senha(senha).id(1l).build();
		Mockito.when(repository.findByEmail(email)).thenReturn(Optional.of(usuario));
		//acao
		Usuario result = service.autenticar(email, senha);
		
		//verificacao
		Assertions.assertThat(result).isNotNull();
		
	}
	
	
	
	@Test
	public void deveLancarErroQuandoNaoEncontrarUsuarioCadastradoComOemailInformado()
	{
		//cenario
		Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());
		
		//acao
		Throwable exception =	Assertions.catchThrowable(() -> service.autenticar("email@email.com", "senha"));
		
		//verificacao 
		Assertions.assertThat(exception).isInstanceOf(ErroDeAutenticacaoException.class)
		.hasMessage("Usuario não encontrado para o email informado");
		
	}
	
	@Test
	public void develancarErroQuandoASenhaNaoBater() {
		//cenario
		String senha="senha";
		Usuario usuario = Usuario.builder().email("email@email.com").senha(senha).build();
		Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(usuario));
		//acao
		Throwable  exception = Assertions.catchThrowable( () -> service.autenticar("email@email.com", "123"));
		Assertions.assertThat(exception).isInstanceOf(ErroDeAutenticacaoException.class).hasMessage("Senha invalida");
		
	}
	
	
	
	
	@Test(expected = Test.None.class)
	public void deveValidarEmail() {

		//cenario
		Mockito.when(repository
				.existsByEmail(Mockito.anyString()))
				.thenReturn(false);
		
		
		//acao
		
		service.validarEmail("email@email.com");
		
	}
	
	public void deveLancarErroAoValidarEmailQuandoExixtirEmailCadastrado()
	{
		//cenario
		Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(true);
		
	}
	
	
	
	
}
