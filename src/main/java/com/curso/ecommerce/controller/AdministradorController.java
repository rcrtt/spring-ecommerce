package com.curso.ecommerce.controller;

import java.util.List;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.curso.ecommerce.model.Orden;
import com.curso.ecommerce.model.Producto;
import com.curso.ecommerce.service.IOrdenService;
import com.curso.ecommerce.service.IUsuarioServicio;
import com.curso.ecommerce.service.ProductoServicio;

@Controller
@RequestMapping("/administrador")
public class AdministradorController {
	
	private final org.slf4j.Logger LOGGER=LoggerFactory.getLogger(AdministradorController.class);
	
	@Autowired
	private ProductoServicio pServ;
	
	@Autowired
	private IUsuarioServicio usuarioService;
	
	@Autowired
	private IOrdenService ordenesService;

	@GetMapping("")
	public String home(Model model) {
		
		List<Producto> productos=pServ.findAll();
		model.addAttribute("productos", productos);
		
		return "administrador/home";
	}
	
	@GetMapping("/usuarios")
	public String usuarios(Model model) {
		
		model.addAttribute("usuarios", usuarioService.findAll());
		return "administrador/usuarios";
	}
	
	@GetMapping("/ordenes")
	public String ordenes(Model model) {
		
		model.addAttribute("ordenes", ordenesService.findAll());
		return "administrador/ordenes";
	}
	
	@GetMapping("/detalle/{id}")
	public String detalle(Model model, @PathVariable Integer id) {
		Orden orden=ordenesService.findByid(id).get();
		
		model.addAttribute("detalles", orden.getDetalle());
		
		
		return "administrador/detalleorden";
	}
	
}
