package com.truper.sae.commons.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseVO implements Serializable
{
	private static final long serialVersionUID = 1L;
	private String mensaje;
	private String  tipoMensaje;
	private Map<String,Object> data;
	
	public ResponseVO(String tipo, String msg,String nombreObjeto,Object data) {
		this.mensaje = msg;
		this.tipoMensaje =tipo;
		this.data = new HashMap<String, Object>();
		this.data.put(nombreObjeto, data);
	}
	
}
