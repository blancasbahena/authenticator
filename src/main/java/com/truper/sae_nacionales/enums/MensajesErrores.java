package com.truper.sae_nacionales.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MensajesErrores
{
	EXPIRED_JWT_EXCEPTION("Request to parse expired JWT FAILED."),
	UNSUPPORTED_JWT_EXCEPTION("Request to parse unsupported JWT FAILED."),
	MAL_FORMED_JWT_EXCEPTION("Request to parse invalid JWT FAILED."),
	SIGNATURE_JWT_EXCEPTION("Request to parse JWT with invalid signature FAILED."),
	ILLEGAL_ARGUMENT_EXCEPTION("Request to parse empty or null JWT FAILED."),
	GENERAL_EXCEPTION("Request to parse JWT FAILED.");
	private String value;
}
