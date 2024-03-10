package com.curso.ecommerce.controller;

import java.util.Optional;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.curso.ecommerce.model.Producto;
import com.curso.ecommerce.model.Usuario;
import com.curso.ecommerce.service.ProductoServicio;


@Controller
@RequestMapping("/productos")
public class ProductoController {
	
	private final Logger LOGGER=LoggerFactory.getLogger(ProductoController.class);
	
	@Autowired
	private ProductoServicio productoServ;
	
	@GetMapping("")
	public String show(Model model) {
		model.addAttribute("productos", productoServ.findAll());
		return "productos/show";
	}
	@GetMapping("/create")
	public String create() {
		return "productos/create";
	}
	
	Usuario u;
	
	@PostMapping("/save")
	public String save(Producto producto) {
		LOGGER.info("Este es el obj producto {}", producto);
		u=new Usuario(1,"","","","","","","");
		producto.setUsuario(u);
		productoServ.saveProduct(producto);
		return "redirect:/productos";
	}
	
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") Integer id, Model model) {
		Producto producto=new Producto();
		Optional<Producto> optionalProducto=productoServ.getProducto(id);
		producto=optionalProducto.get();
		LOGGER.info("Producto buscado: {}", producto);
		model.addAttribute("p", producto);
		
		return "productos/edit";
	}
	
	@PostMapping("/update")
	public String update(Producto producto) {
		productoServ.saveProduct(producto);
		return "redirect:/productos";
		
	}
	
	@GetMapping("/delete/{id}")
	public String delete(@PathVariable("id") Integer id) {
		
		productoServ.deleteProduct(id);
		return "redirect:/productos";
	}
}
