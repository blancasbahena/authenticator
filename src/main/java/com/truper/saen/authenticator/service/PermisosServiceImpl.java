package com.truper.saen.authenticator.service;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.truper.saen.commons.entities.Permiso;
import com.truper.saen.commons.entities.Relationships;
import com.truper.saen.commons.entities.Role;
import com.truper.saen.commons.entities.User;
import com.truper.saen.authenticator.projection.PermisoProjection;
import com.truper.saen.authenticator.repository.PermisoRepository;
import com.truper.saen.authenticator.repository.RoleRepository;
import com.truper.saen.authenticator.repository.UserRepository;
import com.truper.saen.commons.dto.MenuDTO;
import com.truper.saen.commons.dto.PermisoDTO;
import com.truper.saen.commons.dto.UserDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@RequiredArgsConstructor
@Transactional
@Service
@Slf4j
public class PermisosServiceImpl implements PermisosService {
	private final PermisoRepository permisoRepository;
	private final RoleRepository roleRepository;
	private final ModelMapper modelMapper;
	private final UserRepository userRepository;
	@Override
	public Boolean save(PermisoDTO dto) {
		log.info("Se crea permission : {} ",dto.getNombrePermiso());
		Permiso parent=null;
		if(dto.getParent()!=null) {
			if(dto.getParent().getId()!=null) {
				Optional<Permiso> parentOpt =   permisoRepository.findById(dto.getParent().getId());
				if(parentOpt.isPresent()) {
					parent=parentOpt.get();
				}
			}
		}
		Optional<Permiso> busqueda =   permisoRepository.findByNombrePermiso(dto.getNombrePermiso());
		Optional<User> userOpt=userRepository.findById(dto.getUserCreated().getId());
		if(userOpt.isPresent()) {
			try {
				validate(dto,busqueda,true);
				Permiso nuevo=modelMapper.map(dto, Permiso.class);
				nuevo.setCreated(new Date());
				nuevo.setModified(new Date());
				nuevo.setParent(parent);
				nuevo.setUserCreated(userOpt.get());
				nuevo.setUserModified(userOpt.get());
				permisoRepository.save(nuevo);
				return true;
			}catch(Exception e) {
				log.error("Problemas en Permiso : {}",e.getMessage());
			}
		}else{
			log.error("Problems with the creating user");
		}
		return false;
	}

	@Override
	public Boolean update(PermisoDTO dto) {
		log.info("Se modifica permission : {} ",dto.getNombrePermiso());
		Permiso parent=null;
		Optional<Permiso> optPer =   permisoRepository.findById(dto.getId());
		Optional<Permiso> busqueda =   permisoRepository.findByNombrePermiso(dto.getNombrePermiso());
		if(dto.getParent()!=null) {
			if(dto.getParent().getId()!=null) {
				Optional<Permiso> parentOpt =   permisoRepository.findById(dto.getParent().getId());
				if(parentOpt.isPresent()) {
					parent=parentOpt.get();
				}
			}
		}
		
		User userModified=null;
		Optional<User> userOpt=userRepository.findById(dto.getUserModified().getId());
		if(userOpt.isPresent()) {
			userModified = userOpt.get();
		}
		if(optPer.isPresent() && userModified!=null) {
			try {
				validate(dto,busqueda,false);
				optPer.get().setModified(new Date());
				optPer.get().setUserModified(userModified);
				optPer.get().setDescripcion(dto.getDescripcion());
				optPer.get().setNombrePermiso(dto.getNombrePermiso());
				optPer.get().setParent(parent);
				optPer.get().setTipo(dto.getTipo());
				optPer.get().setUrl(dto.getUrl());
				optPer.get().setIcon(dto.getIcon());
				optPer.get().setIdentifierAccion(dto.getIdentifierAccion());
				optPer.get().setTooltip(dto.getTooltip());
				permisoRepository.save(optPer.get());
				return true;
			}catch(Exception e) {
				log.error("Problems in Permission : {}",e.getMessage());
			}
		}
		if(userModified==null) {
			log.error("Problems with the modifying user");
		}
		return false;
	}

	@Override
	public Boolean delete(PermisoDTO dto) {
		log.info("Se borra permission : {} ",dto.getNombrePermiso());
		Optional<Permiso> optPer =   permisoRepository.findById(dto.getId());
		if(optPer.isPresent()) {
			optPer.get().setActive(false);
			permisoRepository.save(optPer.get());
			return true;
		}
		return false;
	}
	@Override
	public Boolean appendPermissionToRol(Long idRol, Long idPermission,UserDTO userDTO) {
		log.info("Asignar Permission a Usuario {} -> {} ",idRol,idPermission);
		Optional<Role> optRol =   roleRepository.findById(idRol);
		Optional<Permiso> optPer =   permisoRepository.findById(idPermission);
		Optional<User> optModf =   userRepository.findById(userDTO.getId());
		if(optRol.isPresent() && optPer.isPresent()) {
			optRol.get().getPermisos().add(optPer.get());
			optRol.get().setUserModified(optModf.get());
			roleRepository.save(optRol.get());
			return true;
		}
		return false;
	}
	@Override
	public Boolean removePermissionToRol(Long idRol, Long idPermission,UserDTO userDTO) {
		log.info("Remover Permission a Usuario {} -> {} ",idRol,idPermission);
		Optional<Role> optRol =   roleRepository.findById(idRol);
		Optional<Permiso> optPer =   permisoRepository.findById(idPermission);
		Optional<User> optModf =   userRepository.findById(userDTO.getId());
		if(optRol.isPresent() && optPer.isPresent()) {
			optRol.get().getPermisos().remove(optPer.get());
			optRol.get().setUserModified(optModf.get());
			roleRepository.save(optRol.get());
			return true;
		}
		return false;
	}
	@Override
	public List<PermisoDTO> findByRole(Long idRol) {
		log.info("Buscar Permiso : {} ",idRol);
		Optional<Role> optRol =   roleRepository.findById(idRol);
		if(optRol.isPresent()) {
			return Relationships.directSelfReferencePermissions(
					optRol.get().getPermisos().stream().map(permiso->modelMapper.map(permiso, PermisoDTO.class)).collect(Collectors.toList()));
		}
		return Arrays.asList();
	}
	@Override
	public List<PermisoDTO> findByRoleUnassigned(Long idRol) {
		log.info("Buscar Permiso : {} ",idRol);
		List<Permiso> todosPermis=permisoRepository.findAll();
		Optional<Role> optRol =   roleRepository.findById(idRol);
		if(optRol.isPresent()) {
			todosPermis.removeAll(optRol.get().getPermisos());
			return Relationships.directSelfReferencePermissions(
			todosPermis.stream().map(permiso->modelMapper.map(permiso, PermisoDTO.class)).collect(Collectors.toList()));
		}
		return Arrays.asList();
	}

	@Override
	public PermisoDTO findById(Long idPer) {
		log.info("Buscar Permiso : {} ",idPer);
		Optional<Permiso> optPer =   permisoRepository.findById(idPer);
		if(optPer.isPresent()) {
			return Relationships.directSelfReferencePermission(modelMapper.map(optPer.get(), PermisoDTO.class));
		}
		return null;
	}
	@Override
	public PermisoDTO findByNombre(String nombre) {
		log.info("Buscar Permiso : {} ",nombre);
		Optional<Permiso> optPer =   permisoRepository.findByNombrePermiso (nombre);
		if(optPer.isPresent()) {
			return Relationships.directSelfReferencePermission(modelMapper.map(optPer.get(), PermisoDTO.class));
		}
		return null;
	}

	@Override
	public List<PermisoDTO> findAll() {
		log.info("buscar PermissionALL ");
		return Relationships.directSelfReferencePermissions(
				permisoRepository.findAll().stream().map(permiso->modelMapper.map(permiso, PermisoDTO.class)).collect(Collectors.toList()));
	}


	private void validate(PermisoDTO dto, Optional<Permiso> busqueda, boolean isNew) throws  Exception{
		if(dto.getNombrePermiso()==null)
			throw new Exception("Error - Its necesary NombrePermiso");
		if(dto.getNombrePermiso().isEmpty() || dto.getNombrePermiso().equals(""))
			throw new Exception("Error - Its necesary NombrePermiso");
		
		if(dto.getDescripcion()==null)
			throw new Exception("Error - Its necesary Descripcion");
		if(dto.getDescripcion().isEmpty() || dto.getDescripcion().equals(""))
			throw new Exception("Error - Its necesary Descripcion");
		if(isNew) {
			if(dto.getId()!=null)
				throw new Exception("Error - Id -Its required null");
			if(busqueda.isPresent())
				throw new Exception("Error - Problems with duplicate ["+busqueda.get().getNombrePermiso()+"]");
		}
		else{
			if(dto.getId()==null)
				throw new Exception("Error - Id -Its required");
			if(busqueda.isPresent()) {
				if(busqueda.get().getId().longValue() != dto.getId().longValue() )
					throw new Exception("Error - Problems with duplicate ["+busqueda.get().getNombrePermiso()+"]");
			}
		}
	}

	@Override
	public List<MenuDTO> findPermisosMenu(Long idUser) {
		try {
			List<PermisoProjection> pMenus = permisoRepository.permisosMenu(idUser);
			List<MenuDTO> menus = pMenus.stream().map(pMenu->modelMapper.map(pMenu, MenuDTO.class)).collect(Collectors.toList());
			buscaSubMenus(menus, idUser);
			return menus;
		} catch (Exception e) {
			return new ArrayList<MenuDTO>();
		}
	}
	
	public List<MenuDTO> buscaSubMenus(List<MenuDTO> menus, Long idUser){
		for(MenuDTO menu: menus) {
			List<PermisoProjection> pSubMenus;
			pSubMenus = permisoRepository.permisosSubMenu(idUser, menu.getId());
			if(pSubMenus.isEmpty()) {
				//Se detiene la busqueda recursiva
			}else {
				List<MenuDTO> subMenus = new ArrayList<>();
				subMenus = pSubMenus.stream().map(pSubMenu->modelMapper.map(pSubMenu, MenuDTO.class)).collect(Collectors.toList());
				menu.setSubMenus(subMenus);
				buscaSubMenus(subMenus, idUser);
			}
		}
		
		return menus;
	}
	
	
}

