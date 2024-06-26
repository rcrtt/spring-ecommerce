package com.curso.ecommerce.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.curso.ecommerce.model.Producto;
import com.curso.ecommerce.repository.IProductoDAO;

@Service
public class ProductoServiceImpl implements ProductoServicio {

	@Autowired
	private IProductoDAO dao;
	
	@Override
	public Producto saveProduct(Producto producto) {
		// TODO Auto-generated method stub
		return dao.save(producto);
	}

	@Override
	public Optional<Producto> getProducto(Integer id) {
		// TODO Auto-generated method stub
		return dao.findById(id);
	}

	@Override
	public void updateProduct(Producto producto) {
		// TODO Auto-generated method stub
		dao.save(producto);
	}

	@Override
	public void deleteProduct(Integer id) {
		// TODO Auto-generated method stub
		dao.deleteById(id);
	}

	@Override
	public List<Producto> findAll() {
		// TODO Auto-generated method stub
		return dao.findAll();
	}

}
