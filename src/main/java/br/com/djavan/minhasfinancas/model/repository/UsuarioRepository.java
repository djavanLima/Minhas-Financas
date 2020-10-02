package br.com.djavan.minhasfinancas.model.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.djavan.minhasfinancas.model.entity.Usuario;
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario,Long>{

	
	boolean existsByEmail(String email); // vai retornar verdadeiro ou falso se o email exixstir ou nao
	
	Optional<Usuario> findByEmail(String email);// procura usuario por email diminui o erro nullpoint exception
}
