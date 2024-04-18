package com.curso.ecommerce.service;

import java.util.List;
import java.util.Optional;

import com.curso.ecommerce.model.Usuario;

public interface IUsuarioServicio {
	Optional<Usuario> findById(Integer id);
	List<Usuario> findAll();
	Usuario save(Usuario usuario);
	Optional<Usuario> findByEmail(String email);
}
