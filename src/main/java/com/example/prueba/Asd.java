package com.example.prueba;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;



@RestController
@RequestMapping("/asd")
public class Asd {


	
	@Autowired
    private JdbcTemplate jdbcTemplate;
	
	
	 private long getAyerInTimestamp() {	     	 	
		 
		 Date defaultDate =DateUtils.ceiling(new Date(), Calendar.DATE);
		 Date date = DateUtils.addDays(defaultDate, -2);
		 System.out.println("fecha de ayer "+ date);
		 System.out.println("fecha con 0 horas "+ defaultDate);
		 long lel= date.getTime();
		 System.out.println("fecha de ayer en timestamp "+lel);
		 long lel2 = 1667325621048L;
		 System.out.println(lel);
		return lel2;		
	 }
	 private long getAnteAyerInTimestamp() {		 	 
		 Date defaultDate =DateUtils.ceiling(new Date(), Calendar.DATE);
		 Date date = DateUtils.addDays(defaultDate, -3);			
		 System.out.println("fecha de anteayer"+ date);
		 System.out.println("fecha con 0 horas "+ defaultDate);
		 long lel= date.getTime();
		 System.out.println("fecha de anteayer en timestamp"+ lel);
		 long lel1 = 1667315958474L;
		 System.out.println(lel);
		return lel1;	
	 } 
	
	 
	 
	@Scheduled(fixedDelay = 50000)
	public void archivoSD()throws JsonMappingException, JsonProcessingException {

		
		 long ayer = getAyerInTimestamp();
		 long anteAyer = getAnteAyerInTimestamp();
		 
		 
		//System.out.println("hola");
//		List<PagosAyerAprovados>pagos = repo.findPagos(anteAyer,ayer);
		
		String sql = "SELECT productos.cantidad, productos.nombre_producto, productos.precio_unitario, pagos.estado_pago, pagos.fecha_creacion, pagos.id FROM dbo.productos INNER JOIN dbo.pagos ON pagos.id = productos.pago WHERE pagos.fecha_creacion > "+anteAyer+" AND pagos.fecha_creacion < "+ayer+" AND pagos.estado_pago = 'approved'";
        List<PagosAyerAprovados> pagos = jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(PagosAyerAprovados.class));
        
		//System.out.println(pagos.size());
		
		//List tiene en indice 0 la cantidad de producto y en el 1 el precio acumulado
		Map<String, ArrayList<Integer>> prodCont = new HashMap<String, ArrayList<Integer>>();
		ArrayList<String> aux = new ArrayList<String>();
		
		for(PagosAyerAprovados pago :pagos) {
			
			if(!prodCont.containsKey(pago.getNombre_producto())) {
				
				aux.add(pago.getNombre_producto());
				
				ArrayList<Integer> list = new ArrayList<Integer>();
				list.add(0);
				list.add(0);
				prodCont.put(pago.getNombre_producto(), list);
				
			}
			
			ArrayList<Integer> cont = prodCont.get(pago.getNombre_producto());
			cont.set(0, pago.getCantidad() + cont.get(0));
			cont.set(1, (pago.getPrecio_unitario()*pago.getCantidad()) + cont.get(1));
			
		}
		
		//System.out.println(prodCont.toString());
		
		
		String identificadorDeMovimiento = "X";
		
		String oficinaSucursal ="XXXX";
		int maxLengthOficinaSucursal = 4;
		String auxOficinaSucursal = StringUtils.leftPad(oficinaSucursal, maxLengthOficinaSucursal);
		
		String canal = "06";
		int maxLengthCanal = 2;
		String auxCanal = StringUtils.leftPad(canal, maxLengthCanal);
		
		//este formato se puede modificar https://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html
		String pattern = "dd.MM.yyyy";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		//Asi obtengo el dia de ayer
		Date date = DateUtils.addDays(new Date(), -1);
		String dateYesterday = simpleDateFormat.format(date);
		Long lel= date.getTime();
		System.out.println("fecha de ayer en timestamp "+ lel);
		
		String fechaDeVenta = dateYesterday;
		int maxLengthFechaDeVenta = 10;
		String auxFechaDeVenta = StringUtils.leftPad(fechaDeVenta, maxLengthFechaDeVenta);
		
		
		String material = "100411001";
		int maxLengthMaterial = 40;
		String auxMaterial = StringUtils.leftPad(material, maxLengthMaterial);
		
		
		try{
			// Crea el archivo 
	        
	        String path="C:\\Users\\elias\\Desktop\\"+"SD "+ fechaDeVenta+".txt";
	        File file = new File(path);
	
	        // Si no existe lo creo
	        if (!file.exists()) {
	            file.createNewFile();
	        }
	
	        FileWriter fw = new FileWriter(file.getAbsoluteFile());
	        BufferedWriter bw = new BufferedWriter(fw);
	
	        // escribo el string en el archivo
	        String content = "";
	        for(String producto :aux) {
	        	
	    		String cantidad= "" + prodCont.get(producto).get(0);
	    		int maxLengthCantidad = 15;
	    		String auxCantidad = StringUtils.leftPad(cantidad, maxLengthCantidad);
	    		
	    		
	    		String importe = "" + prodCont.get(producto).get(1);
	    		int maxLengthImporte = 13;
	    		String auxImporte = StringUtils.leftPad(importe, maxLengthImporte);
	        	
	        	content += identificadorDeMovimiento + auxOficinaSucursal + auxCanal + auxFechaDeVenta + auxMaterial + auxCantidad + auxImporte + "\n";
	        	
	        }
	        bw.write(content);
	
	        // cierro el archivo(importante)
	        bw.close();
	        
	      
	      
		}catch(IOException e){
			
			
	        System.out.println("error al crear o escribir en el archivo");
	       
		}
	
	}

	
	@Scheduled(fixedDelay = 50000)
	public void archivoGL()throws JsonMappingException, JsonProcessingException{
		
		//1667325621048
		long ayer = getAyerInTimestamp();
		//1667315958474
		long anteAyer = getAnteAyerInTimestamp();
		
        
		//este formato se puede modificar https://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html
		String pattern = "dd.MM.yy";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		//Asi obtengo el dia de ayer
		Date date = DateUtils.addDays(new Date(), -1);
		String dateYesterday = simpleDateFormat.format(date);
		String fechaDeDocumento = dateYesterday;
		//System.out.println("esta es la fecha de ayer para el archivo GL"+fechaDeDocumento);
		
		
		String tipoDeDocumento = "MP";
		
		
		
		String sociedad = "COAR";
		
		
		
		String moneda = "ARS ";
	
		
		
		String numeroDeReferencia = "MEPAGOS "+ fechaDeDocumento;
		int maxLengthNumeroReferencia = 16;
		String auxNumeroDeReferencia = StringUtils.leftPad(numeroDeReferencia,maxLengthNumeroReferencia);
		
		
		String textoCabeceraDocumento = "Oficina: 1805";
		int maxLengthTextoCabecera = 25;
		String auxTextCabecera = StringUtils.leftPad(textoCabeceraDocumento, maxLengthTextoCabecera);
		
		
		String claveContabilizacion = "40";
		
		
		
		String numeroCuenta ="410201";
		int maxLengthNumeroCuenta = 17;
		String auxNumeroCuenta = StringUtils.leftPad(numeroCuenta, maxLengthNumeroCuenta);
		
		//Linea 1 cabecera
		
		String linea1 = fechaDeDocumento + fechaDeDocumento + tipoDeDocumento + sociedad + moneda + auxNumeroDeReferencia + auxTextCabecera + claveContabilizacion + auxNumeroCuenta;
		
		//Linea 2 total
		
		
		
		String sql = "SELECT SUM(precio_total) FROM dbo.pagos WHERE pagos.fecha_creacion > "+anteAyer+" AND pagos.fecha_creacion < "+ayer+" AND pagos.estado_pago = 'approved'";
        List<Map<String, Object>> pagos = jdbcTemplate.queryForList(sql);
        //System.out.println("aca es el total: "+pagos.get(0).get(""));
		
        
        String importeTotal;
		
		if (pagos.get(0).containsValue(null)) {
			importeTotal = "0";
			//System.out.println("importe total: "+ importeTotal);
		}else {
			importeTotal = pagos.get(0).get("").toString();
			 //System.out.println("importe total: "+ importeTotal);
		}
        
        
		importeTotal= pagos.get(0).get("").toString();
		int maxLengthImporteTotal = 13;
		String auxImporteTotal = StringUtils.leftPad(importeTotal, maxLengthImporteTotal);
		
		
		String division = "1805";
		
		String centroDeCosto = "XXXXXXXXXX";
		int maxLengthCentroDeCosto = 10;
		String auxCentroDeCosto = StringUtils.leftPad(centroDeCosto, maxLengthCentroDeCosto);
		
		String centroDeBeneficio = "XXXXXXXXXX";
		int maxLengthCentroDeBeneficio = 10;
		String auxCentroDeBeneficio = StringUtils.leftPad(centroDeBeneficio, maxLengthCentroDeBeneficio);
		
		String claveDeReferencia = " ";
		int maxLengthClaveDeReferencia = 20;
		String auxClaveDeReferencia = StringUtils.leftPad(claveDeReferencia,maxLengthClaveDeReferencia);
		
		String asignacion = "1805";
		int maxLengthAsignacion = 18;
		String auxAsignacion = StringUtils.leftPad(asignacion, maxLengthAsignacion);
		
		String textoPosicion ="MEPAGOS "+ fechaDeDocumento;
		int maxLengthTextoPosicion = 50;
		String auxTextoPosicion = StringUtils.leftPad(textoPosicion, maxLengthTextoPosicion);
		
		String claveContabilizacionSiguientePos = "21";
		
		String cuentaSiguentePos = "TJ130404";
		int maxLengthCuentaSiguentePos = 17;
		String auxCuentaSiguientePos = StringUtils.leftPad(cuentaSiguentePos,maxLengthCuentaSiguentePos);
		
		String campoCME = " ";
		
		String fechaVencimientoChequeDiferido =" ";
		
		String linea2 = auxImporteTotal + division + auxCentroDeCosto + auxCentroDeBeneficio + auxClaveDeReferencia + auxAsignacion + auxTextoPosicion + claveContabilizacionSiguientePos +auxCuentaSiguientePos + campoCME + fechaVencimientoChequeDiferido;
		
		//Linea 3 decidir visa
		
		
		String sqlDecidirVisa = "SELECT SUM(pagos.precio_total) FROM dbo.transacciones INNER JOIN dbo.pagos ON pagos.id = transacciones.id_pago WHERE pagos.fecha_creacion > "+anteAyer+" AND pagos.fecha_creacion < "+ayer+" AND pagos.estado_pago = 'approved' AND transacciones.estado = 'approved' AND transacciones.id_medio_pago = 2";
        List<Map<String, Object>> pagosDecidirVisa = jdbcTemplate.queryForList(sqlDecidirVisa);
        //System.out.println("aca es decidir visa: "+pagosDecidirVisa.get(0).get(""));
		
		
        String totalVisa;
		
		if (pagosDecidirVisa.get(0).containsValue(null)) {
			 totalVisa = "0";
			//System.out.println("totalvisa: "+ totalVisa);
		}else {
			 totalVisa = pagosDecidirVisa.get(0).get("").toString();
			// System.out.println("totalvisa: "+ totalVisa);
		}
		
		
		totalVisa = pagosDecidirVisa.get(0).get("").toString();
		int maxLengthTotalVisa = 13;
		String auxTotalVisa = StringUtils.leftPad(totalVisa, maxLengthTotalVisa);
		
		String linea3 = auxTotalVisa + division + auxCentroDeCosto + auxCentroDeBeneficio + auxClaveDeReferencia + auxAsignacion + auxTextoPosicion + claveContabilizacionSiguientePos + auxCuentaSiguientePos + campoCME + fechaVencimientoChequeDiferido;
		
		//Linea 4 decidir mastercard
		
	
		String sqlDecidirMastercard = "SELECT SUM(pagos.precio_total) FROM dbo.transacciones INNER JOIN dbo.pagos ON pagos.id = transacciones.id_pago WHERE pagos.fecha_creacion > "+anteAyer+" AND pagos.fecha_creacion < "+ayer+" AND pagos.estado_pago = 'approved' AND transacciones.estado = 'approved' AND transacciones.id_medio_pago = 3";
        List<Map<String, Object>> pagosDecidirMastercard = jdbcTemplate.queryForList(sqlDecidirMastercard);
        //System.out.println("aca es decidir mastercard: "+pagosDecidirMastercard.get(0).get(""));
		//System.out.println("------------->"+pagosDecidirMastercard.get(0));
		
		
		String totalMaster;
		
		if (pagosDecidirMastercard.get(0).containsValue(null)) {
			 totalMaster = "0";
			//System.out.println("totalmaster: "+ totalMaster);
		}else {
			 totalMaster = pagosDecidirMastercard.get(0).get("").toString();
			 //System.out.println("totalmaster: "+ totalMaster);
		}
		
		
		int maxLengthTotalMaster = 13;
		String auxTotalmaster = StringUtils.leftPad(totalMaster, maxLengthTotalMaster);
		
		
		
		String cuentaSiguientePosMaster ="a definir";
		int maxLengthCuentaSiguientePosmaster = 17;
		String auxCuentaSiguientePosMaster = StringUtils.leftPad(cuentaSiguientePosMaster, maxLengthCuentaSiguientePosmaster);
		
		
		String linea4 = auxTotalmaster  + division + auxCentroDeCosto + auxCentroDeBeneficio + auxClaveDeReferencia + auxAsignacion +  auxTextoPosicion +  claveContabilizacionSiguientePos + auxCuentaSiguientePosMaster + campoCME + fechaVencimientoChequeDiferido;
		
		
		// linea 5 mercadopago
		
		
		
		String sqlMercadopago = "SELECT SUM(pagos.precio_total) FROM dbo.transacciones INNER JOIN dbo.pagos ON pagos.id = transacciones.id_pago WHERE pagos.fecha_creacion > "+anteAyer+" AND pagos.fecha_creacion < "+ayer+" AND pagos.estado_pago = 'approved' AND transacciones.estado = 'approved' AND transacciones.id_medio_pago = 1";
        List<Map<String, Object>> pagosMercadopago = jdbcTemplate.queryForList(sqlMercadopago);
        //System.out.println("aca es mercadpoago: "+pagosMercadopago.get(0).get(""));
		
		
        String totalMercadopago;
		
		if (pagosMercadopago.get(0).containsValue(null)) {
			totalMercadopago = "0";
			//System.out.println("totalMercadopago: "+ totalMercadopago);
		}else {
			totalMercadopago = pagosMercadopago.get(0).get("").toString();
			 //System.out.println("totalMercadopago: "+ totalMercadopago);
		}
		
		
		
		totalMercadopago = pagosMercadopago.get(0).get("").toString();
		int maxLengthTotalMercadopago = 13;
		String auxTotalMercadopago = StringUtils.leftPad(totalMercadopago, maxLengthTotalMercadopago);
		
		String asignacionMercadopago ="a definir";
		int maxLengthAsignacionMercadopago = 18;
		String auxAsignacionMercadopago = StringUtils.leftPad(asignacionMercadopago, maxLengthAsignacionMercadopago);
		
		
		
		String linea5 = auxTotalMercadopago + division + auxCentroDeCosto + auxCentroDeBeneficio + auxClaveDeReferencia + auxAsignacionMercadopago + auxTextoPosicion;
		
		try{
			// Crea el archivo 
	        
	        String path="C:\\Users\\elias\\Desktop\\"+"GL "+ fechaDeDocumento+".txt";
	        File file = new File(path);
	
	        // Si no existe lo creo
	        if (!file.exists()) {
	            file.createNewFile();
	        }
	
	        FileWriter fw = new FileWriter(file.getAbsoluteFile());
	        BufferedWriter bw = new BufferedWriter(fw);
	
	        // escribo el string en el archivo
	       
	        
	        String content = linea1+"\n"+ linea2+"\n"+linea3+"\n"+linea4+"\n"+linea5+ " \n F";
	        	
	        
	        bw.write(content);
	
	        // cierro el archivo(importante)
	        bw.close();
	        
	      
	      
		}catch(IOException e){
			
			
	        System.out.println("error al crear o escribir en el archivo");
	       
		}
		
		
		
		}
		
		}
	

