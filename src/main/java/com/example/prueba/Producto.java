package com.example.prueba;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "productos")
public class Producto {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private Long pago;
	private String nombre_producto;
	private Long cantidad;
	private Long precio_unitario;
	private String codigo_sap;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getPago() {
		return pago;
	}
	public void setPago(Long id_pago) {
		this.pago = id_pago;
	}
	public String getNombre_producto() {
		return nombre_producto;
	}
	public void setNombre_producto(String nombre_producto) {
		this.nombre_producto = nombre_producto;
	}
	public Long getCantidad() {
		return cantidad;
	}
	public void setCantidad(Long cantidad) {
		this.cantidad = cantidad;
	}
	public Long getPrecio_unitario() {
		return precio_unitario;
	}
	public void setPrecio_unitario(Long precio_unitario) {
		this.precio_unitario = precio_unitario;
	}
	public String getNumero_sap() {
		return codigo_sap;
	}
	public void setNumero_sap(String numero_sap) {
		this.codigo_sap = numero_sap;
	}
	
	
}