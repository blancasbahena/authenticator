package com.truper.saen.authenticator;
import static org.assertj.core.api.Assertions.assertThat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.truper.saen.authenticator.service.PermisosService;
import com.truper.saen.authenticator.service.RolService;
import com.truper.saen.authenticator.service.UserService;
import com.truper.saen.commons.dto.PermisoDTO;
import com.truper.saen.commons.dto.RoleDTO;
import com.truper.saen.commons.dto.UserDTO;

import lombok.extern.slf4j.Slf4j;
@SpringBootTest
@Slf4j
class BusquedaConsultaUsuarioTest {
	@Autowired
	private RolService rolService;
	@Autowired
	private UserService userService;
	@Autowired
	private PermisosService permisosService;
	private UserDTO dtoUserExample;	
	private RoleDTO dtoRole=null;
	private PermisoDTO dtoPermission=null;
	private Boolean _true  = true;
	private Boolean _false  = false;
	boolean modificarRoles=false;
	boolean soloPassword=false;
	@BeforeEach
	public void setupUser() {
		dtoUserExample = UserDTO.builder().userName("prueba").name("Prueba"+(new Date()).getTime())
				.password("prueba2022*$%").email("prueba@truper.com").active(_true)
				.userCreated(UserDTO.builder().id(1l).build())
				.userModified(UserDTO.builder().id(1l).build()).build();
	}
	@BeforeEach
	public void setupRole() {
		dtoRole = RoleDTO.builder().descripcion("rol_prueba").active(_true)
				.userCreated(UserDTO.builder().id(1l).build())
				.userModified(UserDTO.builder().id(1l).build()).build(); 
		
	}
	@BeforeEach
	public void setupPermission() {
		dtoPermission = PermisoDTO.builder().descripcion("permission_prueba").active(_true)
				.nombrePermiso("permission_prueba").parent(null).tipo("type").url("www.truper.com")
				.icon("icono").identifierAccion("accion").tooltip("toolTip")
				.userCreated(UserDTO.builder().id(1l).build())
				.userModified(UserDTO.builder().id(1l).build()).build();
	    
	}

	@Test
    @Order(1)
	void orderA_busquedaUsuarioTest() {
		try {
			log.info("Busqueda de Informacion usuario {} ",dtoUserExample.getUserName());
			UserDTO manager = userService.findById(1l);
			if(manager!=null) {
				UserDTO dtoUserNuevo = userService.findByUserName(dtoUserExample.getUserName());
				if(dtoUserNuevo==null) {
					modificarRoles=false;
					soloPassword=false;
					Boolean result =userService.save(dtoUserNuevo);
					assertThat(result.booleanValue()).isEqualTo(_true);				
				}
				if(dtoUserNuevo==null) {
					dtoUserNuevo = userService.findByUserName(dtoUserExample.getUserName());
					assertThat(dtoUserNuevo!=null).isEqualTo(_true);
				}
				if(dtoUserExample!=null) {
					dtoUserExample.setId(dtoUserNuevo.getId());
					modificarRoles=false;
					soloPassword=false;
					dtoUserExample.setActive(_true);
					Boolean result =userService.update(dtoUserExample,modificarRoles,soloPassword);
					assertThat(result.booleanValue()).isEqualTo(_true);
					dtoUserExample = userService.findByUserName(dtoUserExample.getUserName());
					if(dtoUserExample!=null) {
						assertThat(dtoUserExample!=null).isEqualTo(_true);
					}
					soloPassword=true;
					dtoUserExample.setPassword("prueba2022*$%");
					Boolean resultPass =userService.update(dtoUserExample,modificarRoles,soloPassword);
					assertThat(resultPass.booleanValue()).isEqualTo(_true);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Test
    @Order(2)    
	void orderB_busquedaRoleTest() {
		
		try {
			UserDTO manager = userService.findById(1l);
			if(manager!=null) {
				if(dtoRole!=null) {
					log.info("Busqueda de Informacion roles {} ",dtoRole.getDescripcion());
					RoleDTO dtoRoleNuevo = rolService.findByDescripcion(dtoRole.getDescripcion());
					if(dtoRoleNuevo==null) {
						Boolean result =rolService.save(dtoRole);
						assertThat(result.booleanValue()).isEqualTo(_true);				
					}
					dtoRole = rolService.findByDescripcion(dtoRole.getDescripcion());
					if(dtoRole!=null) {
						assertThat(dtoRole!=null).isEqualTo(_true);
						dtoRole.setUserCreated(manager);
						dtoRole.setUserModified(manager);
						Boolean result =rolService.update(dtoRole,_false);
						assertThat(result.booleanValue()).isEqualTo(_true);
						dtoRole = rolService.findByDescripcion(dtoRole.getDescripcion());
						if(dtoRole!=null) {
							assertThat(dtoRole!=null).isEqualTo(_true);
						}
						
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Test
    @Order(3)
	void orderC_busquedaPermissionTest() {
		try {
			UserDTO manager = userService.findById(1l);
			if(manager!=null) {
				if(dtoPermission!=null) {
					log.info("Busqueda de Informacion Permission {} ",dtoPermission.getNombrePermiso());
					PermisoDTO dtoPermissionNuevo = permisosService.findByNombre(dtoPermission.getNombrePermiso());
					if(dtoPermissionNuevo==null) {
						Boolean result =permisosService.save(dtoPermission);
						assertThat(result.booleanValue()).isEqualTo(_true);				
					}
					dtoPermission = permisosService.findByNombre(dtoPermission.getNombrePermiso());
					if(dtoPermission!=null) {
						assertThat(dtoPermission!=null).isEqualTo(_true);
						dtoPermission.setUserCreated(manager);
						dtoPermission.setUserModified(manager);
						Boolean result =permisosService.update(dtoPermission);
						assertThat(result.booleanValue()).isEqualTo(_true);
						dtoPermission = permisosService.findByNombre(dtoPermission.getNombrePermiso());
						if(dtoPermission!=null) {
							assertThat(dtoPermission!=null).isEqualTo(_true);
						}
						
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	@Test
    @Order(4)    
	void orderD_AsignaRoles() {
		try {
			UserDTO manager = userService.findById(1l);
			if(manager!=null) {
				dtoUserExample = userService.findByUserName(dtoUserExample.getUserName());
				if(dtoUserExample!=null) {
					assertThat(dtoUserExample!=null).isEqualTo(_true);
					RoleDTO dtoRoleActual = rolService.findByDescripcion(dtoRole.getDescripcion());
					if(dtoRoleActual!=null) {
						modificarRoles=true;
						soloPassword=false;	
						List<RoleDTO> roles=new ArrayList<RoleDTO>();
						roles.add(dtoRoleActual);
						dtoUserExample.setRoles(roles);
						Boolean resultRoles =userService.update(dtoUserExample,modificarRoles,soloPassword);
						assertThat(resultRoles.booleanValue()).isEqualTo(_true);
						Boolean removeRoles =rolService.removeRoleToUser(dtoUserExample.getId(), dtoRoleActual.getId(),manager);
						assertThat(removeRoles.booleanValue()).isEqualTo(_true);
						Boolean appendRoles =rolService.appendRoleToUser(dtoUserExample.getId(), dtoRoleActual.getId(),manager);
						assertThat(appendRoles.booleanValue()).isEqualTo(_true);
						List<RoleDTO> listaAsignados =   rolService.findByUser(dtoUserExample.getId());
						List<RoleDTO> listaNoAsignados = rolService.findByUserUnassigned(dtoUserExample.getId());
						log.info("Roles Asignados : {}",listaAsignados.stream().map(r-> r.getDescripcion()).collect(Collectors.toList()));
						log.info("Roles NO Asignados : {}",listaNoAsignados.stream().map(r-> r.getDescripcion()).collect(Collectors.toList()));
						Boolean removeRoles2 =rolService.removeRoleToUser(dtoUserExample.getId(), dtoRoleActual.getId(),manager);
						assertThat(removeRoles2.booleanValue()).isEqualTo(_true);
						
						
					}
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Test
    @Order(5)    
	void orderE_AsignaPermissions() {
		try {
			UserDTO manager = userService.findById(1l);
			if(manager!=null) {
				dtoUserExample = userService.findByUserName(dtoUserExample.getUserName());
				if(dtoUserExample!=null) {
					assertThat(dtoUserExample!=null).isEqualTo(_true);
					RoleDTO dtoRoleActual = rolService.findByDescripcion(dtoRole.getDescripcion());
					if(dtoRoleActual!=null) {
						modificarRoles=true;
						soloPassword=false;	
						Boolean appendRoles =rolService.appendRoleToUser(dtoUserExample.getId(), dtoRoleActual.getId(),manager);
						assertThat(appendRoles.booleanValue()).isEqualTo(_true);
						PermisoDTO dtoPermissionNuevo = permisosService.findByNombre(dtoPermission.getNombrePermiso());
						Boolean appendPermission =permisosService.appendPermissionToRol(dtoRoleActual.getId(), dtoPermissionNuevo.getId(),manager);
						assertThat(appendPermission.booleanValue()).isEqualTo(_true);
						List<PermisoDTO> listaAsignados =   permisosService.findByRole(dtoPermissionNuevo.getId());
						List<PermisoDTO> listaNoAsignados = permisosService.findByRoleUnassigned(dtoPermissionNuevo.getId());
						log.info("Permissions Asignados : {}",listaAsignados.stream().map(r-> r.getDescripcion()).collect(Collectors.toList()));
						log.info("Permissions NO Asignados : {}",listaNoAsignados.stream().map(r-> r.getDescripcion()).collect(Collectors.toList()));
						dtoUserExample = userService.findByUserName(dtoUserExample.getUserName());
						log.info("User-Role-Permission {} ",dtoUserExample.toString());
						Boolean removePermission =permisosService.removePermissionToRol(dtoRoleActual.getId(), dtoPermissionNuevo.getId(),manager);
						assertThat(removePermission.booleanValue()).isEqualTo(_true);
						Boolean removeRoles2 =rolService.removeRoleToUser(dtoUserExample.getId(), dtoRoleActual.getId(),manager);
						assertThat(removeRoles2.booleanValue()).isEqualTo(_true);
					}
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}