package br.com.djavan.minhasfinancas.model.repository;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.djavan.minhasfinancas.model.entity.Usuario;



@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace=Replace.NONE)
public class UsuarioRepositoryTest {

	
	@Autowired
	UsuarioRepository repository;
	
	@Autowired
	TestEntityManager entityManager;
	
	@Test
	public void deveVerificarAExistenciaDeUmEmail()
	{
		//cenario
		Usuario usuario =criarUsuario();
		entityManager.persist(usuario);
		//acao
		boolean result = repository.existsByEmail("usuario@email.com");
		
		//verificacao
		Assertions.assertThat(result).isTrue();
		
	}
	
	@Test
	public void deveRetornarFalsoQuandoNaoHouverUsuarioCadastradoComOEmail()
	{
		//Cenario
		// a base de dados ja está vazia
		//Acao
		boolean result = repository.existsByEmail("usuario@email.com");
		
		//verificacao deve ser true
		
		Assertions.assertThat(result).isFalse();
		
	}
	
	
	
	@Test
	public void devePersistirUmUsuarioNaBaseDeDados()
	{
		
		//cenario
		Usuario usuario = criarUsuario();
		//acao
		Usuario usuarioSalvo = repository.save(usuario);
		
		Assertions.assertThat(usuario.getId()).isNotNull();
	}
	
	@Test
	public void deveBuscarUmUsuarioPorEmail()
	{
		Usuario usuario=criarUsuario();
		entityManager.persist(usuario);
		
		Optional<Usuario> result = repository.findByEmail("usuario@email.com");
		Assertions.assertThat(result.isPresent()).isTrue();
	}
	
	public void deveRetornarVazioAoBuscarUsuarioPorEmailQuandoNaoExisteNaBase()
	{
		
		//verificacao
		Optional<Usuario> result = repository.findByEmail("usuario@email.com");
		Assertions.assertThat(result.isPresent()).isFalse();// quando o usuario nao esta na base de dados		
	}
	
	public static Usuario  criarUsuario() {
		return Usuario.builder()
				.nome("usuario")
				.email("usuario@email.com")
				.senha("senha")
				.build();
	}
	
}
