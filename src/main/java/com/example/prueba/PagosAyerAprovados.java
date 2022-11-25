package com.example.prueba;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class PagosAyerAprovados {

	
	int cantidad;
	String nombre_producto;
	int precio_unitario;
	String estado_pago;
	long fecha_creacion;
	@Id
	int id;
	
	
	public int getCantidad() {
		return cantidad;
	}
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}
	public String getNombre_producto() {
		return nombre_producto;
	}
	public void setNombre_producto(String nombre_producto) {
		this.nombre_producto = nombre_producto;
	}
	public int getPrecio_unitario() {
		return precio_unitario;
	}
	public void setPrecio_unitario(int precio_unitario) {
		this.precio_unitario = precio_unitario;
	}
	public String getEstado_pago() {
		return estado_pago;
	}
	public void setEstado_pago(String estado_pago) {
		this.estado_pago = estado_pago;
	}
	public long getFecha_creacion() {
		return fecha_creacion;
	}
	public void setFecha_creacion(long fecha_creacion) {
		this.fecha_creacion = fecha_creacion;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	
	
	
}
