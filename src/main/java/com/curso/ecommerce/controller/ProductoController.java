package com.curso.ecommerce.controller;

import java.io.IOException;
import java.util.Optional;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.curso.ecommerce.model.Producto;
import com.curso.ecommerce.model.Usuario;
import com.curso.ecommerce.service.IUsuarioServicio;
import com.curso.ecommerce.service.ProductoServicio;
import com.curso.ecommerce.service.UploadFileService;
import com.curso.ecommerce.service.UsuarioServiceImpl;

import jakarta.servlet.http.HttpSession;


@Controller
@RequestMapping("/productos")
public class ProductoController {
	
	private final Logger LOGGER=LoggerFactory.getLogger(ProductoController.class);
	
	@Autowired
	private UploadFileService upload;
	
	@Autowired
	private IUsuarioServicio usuarioServ;
	
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
	public String save(Producto producto,@RequestParam("img") MultipartFile file, HttpSession session) throws IOException { // el request param toma el atributo del onjeto hmtl con id "img"
		LOGGER.info("Este es el obj producto {}", producto);
		LOGGER.info("Este es el obj producto {}", file);
		
		Usuario u= usuarioServ.findById(Integer.parseInt(session.getAttribute("idUsuario").toString())).get();
		
		producto.setUsuario(u);
		
		//imagen
		if(producto.getId()==null) {//esta validacion es cuando se crea un producto
			String nombreImagen=upload.saveImage(file);
			producto.setImagen(nombreImagen);
		}else {
			if(file.isEmpty()) { // es cuando editamos un producto pero no cambiamo de imagen
				Producto p=new Producto();
				p=productoServ.getProducto(producto.getId()).get();
				producto.setImagen(p.getImagen());
			}else {
				String nombreImagen=upload.saveImage(file);
				producto.setImagen(nombreImagen);
			}
		}
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
	public String update(Producto producto,@RequestParam("img") MultipartFile file) throws IOException{
		
		Producto p=new Producto();
		p=productoServ.getProducto(producto.getId()).get();
			if(file.isEmpty()) { // es cuando editamos un producto pero no cambiamo de imagen
				producto.setImagen(p.getImagen());
			}else {// cuando se edita la imagen
				
				//eliminar cuando la imagen no sea por defecto
				if(!p.getImagen().equals("default.jpg")) {
					upload.deleteImage(p.getImagen());
				}
				producto.setUsuario(u);
				String nombreImagen=upload.saveImage(file);
				producto.setImagen(nombreImagen);
			}
		
		
		productoServ.saveProduct(producto);
		return "redirect:/productos";
		
	}
	
	@GetMapping("/delete/{id}")
	public String delete(@PathVariable("id") Integer id) {
		
		Producto p=new Producto();
		p=productoServ.getProducto(id).get();
		
		if(p.getImagen().equals("default.jpg")) {
			upload.deleteImage(p.getImagen());
		}
		
		productoServ.deleteProduct(id);
		return "redirect:/productos";
	}
}
