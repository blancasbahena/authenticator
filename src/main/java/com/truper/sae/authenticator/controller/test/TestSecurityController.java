package com.truper.sae.authenticator.controller.test;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.ServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.truper.sae.authenticator.entities.Permiso;
import com.truper.sae.authenticator.entities.Role;
import com.truper.sae.authenticator.entities.User;
import com.truper.sae.authenticator.repository.PermisoRepository;
import com.truper.sae.authenticator.repository.RoleRepository;
import com.truper.sae.authenticator.repository.UserRepository;
import com.truper.sae.commons.dto.ResponseVO;
import com.truper.sae.commons.enums.Mensajes;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;
@RequiredArgsConstructor
@Controller
@RequestMapping("/test/request")
public class TestSecurityController {
	private final UserRepository userRepository;
	private final PermisoRepository permisoRepository;
	private final RoleRepository roleRepository;
	@GetMapping
	@ApiOperation(value = "Url de prueba validacion de token", authorizations = @Authorization(value = "Bearer"))
	public ResponseEntity<ResponseVO> decipherToken(ServletRequest request) 
	{
		List<User> lista =null;
		User u= new User();
		u.setPwdReset(false);
		u.setUserName("lablancasb");
		u.setName("Luis Adrian Blancas");
		u.setEmail("lablancasb@truper.com");
		u.setActive(true);
		u.setPwdReset(false);
		u.setCreated(new Date());
		u.setModified(new Date());
		u.setPassword("$2a$10$II/94soxhwMwlaIXV1cDquyXcrYtiypu2NsWz8ukbrwwAMkom3SeW");
		u=userRepository.save(u); 
		
		User u2= new User();
		u2.setPwdReset(false);
		u2.setUserName("ezapata");
		u2.setName("Emiliano Zapata");
		u2.setEmail("ezapata@truper.com");
		u2.setActive(true);
		u2.setPwdReset(false);
		u2.setCreated(new Date());
		u2.setModified(new Date());
		u2.setPassword("$2a$10$II/94soxhwMwlaIXV1cDquyXcrYtiypu2NsWz8ukbrwwAMkom3SeW");
		userRepository.save(u2);
 
		
		User u3= new User();
		u3.setPwdReset(false);
		u3.setUserName("mhidalgo");
		u3.setName("Miguel Hidalgo");
		u3.setEmail("mhidalgo@truper.com");
		u3.setActive(true);
		u3.setPwdReset(false);
		u3.setCreated(new Date());
		u3.setModified(new Date());
		u3.setPassword("$2a$10$II/94soxhwMwlaIXV1cDquyXcrYtiypu2NsWz8ukbrwwAMkom3SeW");
		userRepository.save(u3);
		
		Role rol1= new Role(); 
		rol1.setDescripcion("ROL_USER");
		rol1.setActive(true);
		rol1.setCreated(new Date());
		rol1.setUsercreated(u);
		rol1.setModified(new Date());
		rol1.setUsermodified(u);
		roleRepository.save(rol1);
		 
		Role rol2= new Role(); 
		rol2.setDescripcion("ROL_ADMIN");
		rol2.setActive(true);
		rol2.setCreated(new Date());
		rol2.setUsercreated(u);
		rol2.setModified(new Date());
		rol2.setUsermodified(u);
		roleRepository.save(rol2);
		
		Role rol3= new Role();
		rol3.setDescripcion("ROL_CONSULTAR");
		rol3.setActive(true);
		rol3.setCreated(new Date());
		rol3.setUsercreated(u);
		rol3.setModified(new Date());
		rol3.setUsermodified(u);
		roleRepository.save(rol3);
		
		Role rol4= new Role();
		rol4.setDescripcion("ROL_SOPORTE");
		rol4.setActive(true);
		rol4.setCreated(new Date());
		rol4.setUsercreated(u);
		rol4.setModified(new Date());
		rol4.setUsermodified(u);
		rol4.setModified(new Date());
		
		roleRepository.save(rol4);
		
		 
		Permiso permiso1= new Permiso();
		permiso1.setNombrePermiso("PERMISO_EDITA1");
		permiso1.setDescripcion("PERMISO PARA EDITA1");
		permiso1.setCreated(new Date());
		permiso1.setUsercreated(u);
		permiso1.setActive(true);
		permiso1.setModified(new Date());
		permiso1.setUsermodified(u);
		permiso1=permisoRepository.save(permiso1);
		
		Permiso permiso2= new Permiso();
		permiso2.setNombrePermiso("PERMISO_EDITA2");
		permiso2.setDescripcion("PERMISO PARA EDITA2");
		permiso2.setCreated(new Date());
		permiso2.setUsercreated(u);
		permiso2.setModified(new Date());
		permiso2.setActive(true);
		permiso2.setUsermodified(u);
		permiso2=permisoRepository.save(permiso2);
		
		Permiso permiso3= new Permiso();
		permiso3.setNombrePermiso("PERMISO_CONSULTAR3");
		permiso3.setDescripcion("PERMISO PARA CONSULTAR3");
		permiso3.setCreated(new Date());
		permiso3.setUsercreated(u);
		permiso3.setModified(new Date());
		permiso3.setActive(true);
		permiso3.setUsermodified(u);
		permiso3=permisoRepository.save(permiso3);
		
		Permiso permiso4= new Permiso();
		permiso4.setNombrePermiso("PERMISO_CONSULTAR4");
		permiso4.setDescripcion("PERMISO PARA CONSULTAR4");
		permiso4.setCreated(new Date());
		permiso4.setUsercreated(u);
		permiso4.setModified(new Date());
		permiso4.setActive(true);
		permiso4.setUsermodified(u);
		permiso4=permisoRepository.save(permiso4);
		
		Permiso permiso5= new Permiso();
		permiso5.setNombrePermiso("PERMISO_BORRA5");
		permiso5.setDescripcion("PERMISO PARA BORRA5");
		permiso5.setCreated(new Date());
		permiso5.setUsercreated(u);
		permiso5.setActive(true);
		permiso5.setModified(new Date());
		permiso5.setUsermodified(u);
		permiso5=permisoRepository.save(permiso5);
		
		Permiso permiso6= new Permiso();
		permiso6.setNombrePermiso("PERMISO_BORRA6");
		permiso6.setDescripcion("PERMISO PARA BORRA6");
		permiso6.setCreated(new Date());
		permiso6.setUsercreated(u);
		permiso6.setActive(true);
		permiso6.setModified(new Date());
		permiso6.setUsermodified(u);
		permiso6=permisoRepository.save(permiso6);
		
		Permiso permiso7= new Permiso();
		permiso7.setNombrePermiso("PERMISO_MODIFICA7");
		permiso7.setDescripcion("PERMISO PARA MODIFICA7");
		permiso7.setCreated(new Date());
		permiso7.setUsercreated(u);
		permiso7.setActive(true);
		permiso7.setModified(new Date());
		permiso7.setUsermodified(u);
		permiso7=permisoRepository.save(permiso7);
		
		Permiso permiso8= new Permiso();
		permiso8.setNombrePermiso("PERMISO_MODIFICA8");
		permiso8.setDescripcion("PERMISO PARA MODIFICA8");
		permiso8.setCreated(new Date());
		permiso8.setActive(true);
		permiso8.setUsercreated(u);
		permiso8.setModified(new Date());
		permiso8.setUsermodified(u);
		permiso8=permisoRepository.save(permiso8);
		 
		
		Optional<Role> o1= roleRepository.findByDescripcion("ROL_USER");
		Optional<Role> o2= roleRepository.findByDescripcion("ROL_ADMIN");
		Optional<Role> o3= roleRepository.findByDescripcion("ROL_CONSULTAR");
		Optional<Role> o4= roleRepository.findByDescripcion("ROL_SOPORTE");
		
		
		Optional<Permiso> p1= permisoRepository.findByNombrePermiso("PERMISO_EDITA1");
		Optional<Permiso> p2= permisoRepository.findByNombrePermiso("PERMISO_EDITA2");
		Optional<Permiso> p3= permisoRepository.findByNombrePermiso("PERMISO_CONSULTAR3");
		Optional<Permiso> p4= permisoRepository.findByNombrePermiso("PERMISO_CONSULTAR4");
		Optional<Permiso> p5= permisoRepository.findByNombrePermiso("PERMISO_BORRA5");
		Optional<Permiso> p6= permisoRepository.findByNombrePermiso("PERMISO_BORRA6");
		Optional<Permiso> p7= permisoRepository.findByNombrePermiso("PERMISO_MODIFICA7");
		Optional<Permiso> p8= permisoRepository.findByNombrePermiso("PERMISO_MODIFICA8");

		if(p1.isPresent() && p2.isPresent() && p3.isPresent() && p4.isPresent() && 
				p5.isPresent() && p6.isPresent() && p7.isPresent() && p8.isPresent()) {
			
			List<Permiso> collectionP1 = Arrays.asList(p1.get()).stream().collect(Collectors.toList());
			List<Permiso> collectionP2 = Arrays.asList(p1.get(),p2.get()).stream().collect(Collectors.toList());
			List<Permiso> collectionP3 = Arrays.asList(p3.get(),p4.get(),p5.get()).stream().collect(Collectors.toList());
			List<Permiso> collectionP4 = Arrays.asList(p1.get(),p2.get(),p3.get(),p4.get(),p5.get(),p6.get(),p7.get(),p8.get()).stream().collect(Collectors.toList());
			if(o1.isPresent() && o2.isPresent() && o3.isPresent() && o4.isPresent() ) {
				o1.get().setPermisos(collectionP1);
				o2.get().setPermisos(collectionP2);
				o3.get().setPermisos(collectionP3);
				o4.get().setPermisos(collectionP4);
				
				Set<Role> collectionR1 = Arrays.asList(o1.get()).stream().collect(Collectors.toSet());
				Set<Role> collectionR2 = Arrays.asList(o1.get(),o2.get()).stream().collect(Collectors.toSet()); 
				Set<Role> collectionR3 = Arrays.asList(o1.get(),o2.get(),o3.get(),o4.get()).stream().collect(Collectors.toSet());
				
				
				Optional<User> uno= userRepository.findByUserName("lablancasb");
				if(uno.isPresent()) {
					uno.get().setRoles(collectionR1);
					userRepository.save(uno.get());
				}
				Optional<User> dos= userRepository.findByUserName("ezapata");
				if(dos.isPresent()) {
					dos.get().setRoles(collectionR2);
					userRepository.save(dos.get());
				}
				Optional<User> tres= userRepository.findByUserName("mhidalgo");
				if(tres.isPresent()) {
					tres.get().setRoles(collectionR3);
					userRepository.save(tres.get());
				}
				
				lista = userRepository.findAll(); 
			}
		}
		Map<String, Object> formData = new HashMap<>();
		formData.put("usuarios", lista);
		return ResponseEntity.ok(ResponseVO.builder()
				.tipoMensaje(Mensajes.TIPO_EXITO.getMensaje())
				.mensaje(Mensajes.MSG_TOKEN_EXITO.getMensaje())
				.data(formData)
				.build());	
	}
}