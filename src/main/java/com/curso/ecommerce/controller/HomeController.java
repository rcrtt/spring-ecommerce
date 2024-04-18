package com.curso.ecommerce.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.yaml.snakeyaml.internal.Logger;

import com.curso.ecommerce.model.DetalleOrden;
import com.curso.ecommerce.model.Orden;
import com.curso.ecommerce.model.Producto;
import com.curso.ecommerce.model.Usuario;
import com.curso.ecommerce.service.ProductoServicio;
import com.curso.ecommerce.service.UsuarioServiceImpl;

import jakarta.servlet.http.HttpSession;

import com.curso.ecommerce.service.IDetalleOrdenService;
import com.curso.ecommerce.service.IOrdenService;
import com.curso.ecommerce.service.IUsuarioServicio;



@Controller
@RequestMapping("/")
public class HomeController {
	
	private final org.slf4j.Logger LOGGER=LoggerFactory.getLogger(HomeController.class);
	
	/*@Autowired
	private IUsuarioServicio usuarioService;*/
	
	@Autowired
	private ProductoServicio pServ;
	
	@Autowired
	private IOrdenService ordenService;
	
	@Autowired
	private IDetalleOrdenService detalleOrdenService;
	
	//para almacenar los detalles de la orden
	List<DetalleOrden> detalles = new ArrayList<DetalleOrden>();
	
	@Autowired
	private IUsuarioServicio usuarioServicio;
	
	//datos de la orden
	Orden orden=new Orden();
	
	@GetMapping("")
	public String home(Model model, HttpSession session) {
		//LOGGER.info("Sesion del usuario: {}", session.getAttribute("idUsuario"));
		model.addAttribute("productos", pServ.findAll());
		//session
		model.addAttribute("sesion", session.getAttribute("idUsuario"));
		return "Usuario/home";
	}
	
	@GetMapping("productohome/{id}")
	public String productohome(@PathVariable Integer id, Model model) {
		LOGGER.info("Id producto enviado como parametro {}", id);
		Producto producto=new Producto();
		Optional<Producto> productoOptional=pServ.getProducto(id);
		producto=productoOptional.get();
		model.addAttribute("producto", producto);
		return "Usuario/productohome";
	}
	
	@PostMapping("/cart")
	public String addCart(@RequestParam Integer id, @RequestParam Integer cantidad, Model model) {
		DetalleOrden detalleOrden = new DetalleOrden();
		Producto producto = new Producto();
		double sumaTotal = 0;

		Optional<Producto> optionalProducto = pServ.getProducto(id);
		LOGGER.info("Producto añadido: {}", optionalProducto.get());
		LOGGER.info("Cantidad: {}", cantidad);
		producto = optionalProducto.get();

		detalleOrden.setCantidad(cantidad);
		detalleOrden.setPrecio(producto.getPrecio());
		detalleOrden.setNombre(producto.getNombre());
		detalleOrden.setTotal(producto.getPrecio() * cantidad);
		detalleOrden.setProducto(producto);
		
		//validar que le producto no se añada 2 veces
		Integer idProducto=producto.getId();
		boolean ingresado=detalles.stream().anyMatch(p -> p.getProducto().getId()==idProducto);
		
		if (!ingresado) {
			detalles.add(detalleOrden);
		}
		
		sumaTotal = detalles.stream().mapToDouble(dt -> dt.getTotal()).sum();

		orden.setTotal(sumaTotal);
		model.addAttribute("cart", detalles);
		model.addAttribute("orden", orden);

		return "usuario/carrito";
	}
	
	@GetMapping("/delete/cart/{id}")
	public String deleteProductCard(@PathVariable Integer id, Model model) {
		
		List<DetalleOrden> ordenesNueva = new ArrayList<DetalleOrden>();
		for(DetalleOrden dOrden: detalles) {
			if(dOrden.getProducto().getId()!=id) {
				
			}
		}
		
		//poner lista con los productos restantes
		detalles = ordenesNueva;
		
		double sumaTotal=0;		
		sumaTotal=detalles.stream().mapToDouble(dt->dt.getTotal()).sum();
		orden.setTotal(sumaTotal);		
		model.addAttribute("cart", detalles);
		model.addAttribute("orden", orden);
		
		return "Usuario/deleteCart";
	}
	
	@GetMapping("/getCart")
	public String getCart(Model model, HttpSession sesion) {
		model.addAttribute("cart", detalles);
		model.addAttribute("orden", orden);
		model.addAttribute("sesion", sesion.getAttribute("idUsuario"));
		return "/Usuario/carrito";
	}
	
	@GetMapping("/order")
	public String order(Usuario user, Model model, HttpSession session) {
		
		Optional<Usuario> optionalUser=usuarioServicio.findByEmail(user.getEmail());
		
		if(optionalUser.isPresent()) {
			
			Usuario usuario=usuarioServicio.findById( Integer.parseInt(session.getAttribute("idUsuario").toString()) ).get();
			
			model.addAttribute("cart", detalles);
			model.addAttribute("orden", orden);
			model.addAttribute("usuario",usuario);
			
			return "Usuario/resumenorden";
			
		}else {
			
			return "Usuario/login";
			
		}
		
	}
	
	// guardar la orden
		@GetMapping("/saveOrder")
		public String saveOrder(HttpSession session ) {
			Date fechaCreacion = new Date();
			orden.setFechaCreacion(fechaCreacion);
			orden.setNumero(ordenService.generarNumeroOrden());
			
			//usuario
			Usuario usuario =usuarioServicio.findById( Integer.parseInt(session.getAttribute("idUsuario").toString())  ).get();
			LOGGER.info("Numero: {}", usuario);
			
			orden.setUsuario(usuario);
			ordenService.save(orden);
			
			//guardar detalles
			for (DetalleOrden dt:detalles) {
				dt.setOrden(orden);
				detalleOrdenService.save(dt);
			}
			
			///limpiar lista y orden
			orden = new Orden();
			detalles.clear();
			
			return "redirect:/";
		}
	
	@PostMapping("search")
	public String searchProduct(@RequestParam String nombre, Model model) {
		
		LOGGER.info("Nombre del producto: {}", nombre);
		List<Producto> producto= pServ.findAll().stream().filter(p -> p.getNombre().contains(nombre)).collect(Collectors.toList());  // obtiene todos los productos, filtrando el nombre de lo que se busca con lo que hay en bd
		model.addAttribute("productos", producto);
		
		return "Usuario/home";
	}
	
	
}
