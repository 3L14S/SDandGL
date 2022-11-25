package com.example.prueba;


import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "pagos")
public class Pago {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String idAplicacion;
	private UUID idPagador;
	private String idTransaccionAplicacion;
	private String estadoPago;
	private Boolean notificado;
	private Long fechaEstado;
	private Long fechaCreacion;
	private Long precioTotal;
	private String notificationUrl;
	private String backUrl;
//	private String idPreferencia;
	private Long fechaVencimiento;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getIdAplicacion() {
		return idAplicacion;
	}
	public void setIdAplicacion(String idConsumidor) {
		this.idAplicacion = idConsumidor;
	}
	public String getIdTransaccionAplicacion() {
		return idTransaccionAplicacion;
	}
	public void setIdTransaccionAplicacion(String idTransaccionAplicacion) {
		this.idTransaccionAplicacion = idTransaccionAplicacion;
	}
	public Long getFechaCreacion() {
		return fechaCreacion;
	}
	public void setFechaCreacion(Long fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}
	public Long getPrecioTotal() {
		return precioTotal;
	}
	public void setPrecioTotal(Long precioTotal) {
		this.precioTotal = precioTotal;
	}
	public String getBackUrl() {
		return backUrl;
	}
	public void setBackUrl(String backUrl) {
		this.backUrl = backUrl;
	}
	public UUID getIdPagador() {
		return idPagador;
	}
	public void setIdPagador(UUID idPagador) {
		this.idPagador = idPagador;
	}
	public String getEstadoPago() {
		return estadoPago;
	}
	public void setEstadoPago(String estadoPago) {
		this.estadoPago = estadoPago;
	}
	public Long getFechaEstado() {
		return fechaEstado;
	}
	public void setFechaEstado(Long fechaEstado) {
		this.fechaEstado = fechaEstado;
	}
	public String getNotificationUrl() {
		return notificationUrl;
	}
	public void setNotificationUrl(String notificationUrl) {
		this.notificationUrl = notificationUrl;
	}
	public Boolean getNotificado() {
		return notificado;
	}
	public void setNotificado(Boolean notificado) {
		this.notificado = notificado;
	}
//	public String getIdPreferencia() {
//		return idPreferencia;
//	}
//	public void setIdPreferencia(String idPreferencia) {
//		this.idPreferencia = idPreferencia;
//	}
	public Long getFechaVencimiento() {
		return fechaVencimiento;
	}
	public void setFechaVencimiento(Long fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}
	
}
