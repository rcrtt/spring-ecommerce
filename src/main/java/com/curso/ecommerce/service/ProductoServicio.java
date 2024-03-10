package com.curso.ecommerce.service;

import java.util.List;
import java.util.Optional;

import com.curso.ecommerce.model.Producto;

public interface ProductoServicio {
	
	public Producto saveProduct(Producto producto);
	public Optional<Producto> getProducto(Integer id);
	public void updateProduct(Producto producto);
	public void deleteProduct(Integer id);
	public List<Producto> findAll();
}
