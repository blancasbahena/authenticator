package com.truper.sae_nacionales.dto;

import java.io.Serializable;
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
	private String tipoMensaje;
}
