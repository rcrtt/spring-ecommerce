package com.curso.ecommerce.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.curso.ecommerce.model.DetalleOrden;
import com.curso.ecommerce.model.Orden;
import com.curso.ecommerce.model.Usuario;
import com.curso.ecommerce.repository.IOrdenRepository;

@Service
public class OrdenServiceImpl implements IOrdenService {

	@Autowired
	private IOrdenRepository ordenRepository;
	
	@Override
	public Orden save(Orden orden) {
		// TODO Auto-generated method stub
		return ordenRepository.save(orden);
	}

	@Override
	public List<Orden> findAll() {
		// TODO Auto-generated method stub
		return ordenRepository.findAll();
	}
	
	public String generarNumeroOrden() {
	
		int numero=0;
		String numeroConcatenado="";
		
		List<Orden> ordenes = findAll();
		List<Integer> numeros = new ArrayList<Integer>();
		ordenes.stream().forEach(o -> numeros.add(Integer.parseInt(o.getNumero())));
		
		if(ordenes.isEmpty()) {
			numero=1;
		}else {
			numero=numeros.stream().max(Integer::compare).get();  // compara todos los numeros dentro de la lista y selecciona al mayor
			numero++;
		}
		
		if(numero<10) {
			
			numeroConcatenado="000000000"+String.valueOf(numero);
			
		}else if(numero<100) {
			
			numeroConcatenado="00000000"+String.valueOf(numero);
			
		}else if(numero<1000) {
			
			numeroConcatenado=""+String.valueOf(numero);
			
		}else if(numero<10000) {
			
			numeroConcatenado="000000"+String.valueOf(numero);
			
		}
		
		return numeroConcatenado;
	}

	@Override
	public List<Orden> findByUsuario(Usuario usuario) {
		// TODO Auto-generated method stub
		return ordenRepository.findByUsuario(usuario);
	}

	@Override
	public Optional<Orden> findByid(Integer id) {
		// TODO Auto-generated method stub
		return ordenRepository.findById(id);
	}
	
}
