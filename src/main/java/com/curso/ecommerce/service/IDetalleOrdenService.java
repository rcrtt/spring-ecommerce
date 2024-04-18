package com.curso.ecommerce.service;

import org.springframework.stereotype.Service;

import com.curso.ecommerce.model.DetalleOrden;

@Service
public interface IDetalleOrdenService {
	DetalleOrden save (DetalleOrden detalleOrden);
}
