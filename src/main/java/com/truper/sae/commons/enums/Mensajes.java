package com.truper.sae.commons.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Mensajes 
{
	TIPO_EXITO("S"),
	TIPO_WARNING("W"),
	TIPO_ERROR("E"),
	MSG_ERROR("Hubo un ERROR durante la operación."),
	MSG_TOKEN_WARNING("El token es incorrecto, intente con otro token."),
	MSG_EXITO("Proceso ejecutado correctamente"),
	MSG_TOKEN_EXITO("El token es valido, acceso EXITOSO.");

	private String mensaje;
}