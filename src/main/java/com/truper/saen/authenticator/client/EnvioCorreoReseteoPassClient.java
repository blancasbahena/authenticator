package com.truper.saen.authenticator.client;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.truper.saen.authenticator.dto.ResetPasswordDTO;
import com.truper.saen.commons.dto.ResponseVO;
@FeignClient(name = "envioCorreoReseteoPassClient", url = "${sae-batch.host}")
public interface EnvioCorreoReseteoPassClient {
	@PostMapping("${sae-batch.method}")
	public ResponseEntity<ResponseVO> envioCorreoResetPassword(@RequestHeader(value = "Authorization", required = true) String authorization,
			@RequestBody(required = true)  ResetPasswordDTO dto);

}
 