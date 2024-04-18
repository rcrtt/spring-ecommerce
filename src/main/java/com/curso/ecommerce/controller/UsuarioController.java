package com.curso.ecommerce.controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.curso.ecommerce.model.Orden;
import com.curso.ecommerce.model.Usuario;
import com.curso.ecommerce.service.IOrdenService;
import com.curso.ecommerce.service.IUsuarioServicio;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {
	
	private final org.slf4j.Logger LOGGER=LoggerFactory.getLogger(UsuarioController.class);
	
	BCryptPasswordEncoder passEncode = new BCryptPasswordEncoder();
	
	@Autowired
	private IUsuarioServicio usuarioService;
	
	@Autowired
	private IOrdenService ordenService;
	
	@GetMapping("/registro")
	public String create() {
		return "Usuario/registro";
	}
	
	@PostMapping("/save")
	public String save(Usuario usuario) {
		LOGGER.info("Usuario registro: " + usuario);
		usuario.setTipo("USER");
		usuario.setPassword(passEncode.encode(usuario.getPassword()));
		usuarioService.save(usuario);
		return "redirect:/";
	}
	
	@GetMapping("/login")
	public String login() {
		
		return "Usuario/login";
	}
	
	@PostMapping("/acceder")
	public String acceder(Usuario usuario, HttpSession session) {
		
		LOGGER.info("Accesos: {}", usuario);
		
		Optional<Usuario> user=usuarioService.findByEmail(usuario.getEmail());
		
		//LOGGER.info("USuario obtenido {}", user.get());
		
		if(user.isPresent()) {
		
			session.setAttribute("idUsuario", user.get().getId());
			if(user.get().getTipo().equals("ADMIN")){ return "redirect:/administrador";}
			else { return "redirect:/";}
			
		}else {
			LOGGER.info("USer no existe");
		}
		
		return "redirect:/";
	}
	
	@GetMapping("/compras")
	public String obtenerCompras(Model model,HttpSession sesion) {
		
		model.addAttribute("sesion", sesion.getAttribute("idUsuario"));
		Usuario usuario=usuarioService.findById(Integer.parseInt(sesion.getAttribute("idUsuario").toString())).get();
		List<Orden> ordenes=ordenService.findByUsuario(usuario);
		
		model.addAttribute("ordenes", ordenes);
		
		return "Usuario/compras";
	}
	
	@GetMapping("/detalle/{id}")
	public String detalleCompra(@PathVariable Integer id, HttpSession session, Model model) {
		
		LOGGER.info("Id de la orden: {}", id);
		Optional<Orden> orden=ordenService.findByid(id);
		
		model.addAttribute("detalles", orden.get().getDetalle());
		
		model.addAttribute("sesion", session.getAttribute("idUsuario"));
		
		return "Usuario/detallecompra";
	}
	
	@GetMapping("/cerrar")
	public String cerrarSesion( HttpSession session ) {
		session.removeAttribute("idUsuario");
		return "redirect:/";
	}
}
