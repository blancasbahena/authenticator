package com.truper.saen.authenticator.service;

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
import com.truper.saen.authenticator.repository.PermisoRepository;
import com.truper.saen.authenticator.repository.RoleRepository;
import com.truper.saen.authenticator.repository.UserRepository;
import com.truper.saen.commons.dto.PermisoDTO;
import com.truper.saen.commons.dto.RoleDTO;
import com.truper.saen.commons.dto.UserDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class RolServiceImpl implements RolService {
	private final RoleRepository roleRepository;
	private final PermisoRepository permisoRepository;
	private final ModelMapper modelMapper;
	private final UserRepository userRepository;
	@Override
	public Boolean save(RoleDTO dto) throws  Exception{
		log.info("Crea roles {},  {}" , dto.getDescripcion());
		List<Permiso> listaPermisos= null;
		Optional<Role> busqueda =   roleRepository.findByDescripcion(dto.getDescripcion());
		Optional<User> userOpt=userRepository.findById(dto.getUserCreated().getId());
		if(userOpt.isPresent()) {
			if(dto.getPermisos()!=null) {
				if(!dto.getPermisos().isEmpty()) {
					listaPermisos = 
							permisoRepository.findByIdIn(dto.getPermisos().stream().map(p->p.getId()).collect(Collectors.toList()));
				}
			}
			try {
				validate(dto,busqueda,true);
				Role nuevo=modelMapper.map(dto, Role.class);
				nuevo.setActive(true);
				nuevo.setCreated(new Date());
				nuevo.setModified(new Date());
				nuevo.setUserCreated(userOpt.get());
				nuevo.setUserModified(userOpt.get());
				nuevo.setPermisos(listaPermisos);
				roleRepository.save(nuevo);
				return true;
			}catch(Exception e) {
				throw new Exception(e.getMessage());
				
			}
		}else{
			log.error("Problems with user creater in DTO");
			throw new Exception("Problems with user creater in DTO");
		} 
	}
	public RoleDTO findByDescripcion(String descripcion) {
		log.info("Busqueda de rol por {}" , descripcion);
		Optional<Role> optRol =   roleRepository.findByDescripcion(descripcion);
		if(optRol.isPresent()) {
			return modelMapper.map(optRol.get(), RoleDTO.class);
		}
		return null;
	}
	@Override
	public Boolean update(RoleDTO dto,boolean modifyPermission) throws  Exception {
		log.info("Modifica roles {},  {}" , dto.getDescripcion(), modifyPermission);
		List<Permiso> listaPermisos= null;
		Optional<Role> optPer =   roleRepository.findById(dto.getId());
		Optional<Role> busqueda =   roleRepository.findByDescripcion(dto.getDescripcion());
		User userModified=null;
		if(dto.getActive()==null) {
			dto.setActive(true);
		}
		Optional<User> userOpt=userRepository.findById(dto.getUserModified().getId());
		if(userOpt.isPresent()) {
			userModified = userOpt.get();
		}
		if(modifyPermission) {
			if(dto.getPermisos()!=null) {
				if(!dto.getPermisos().isEmpty()) {
					listaPermisos = 
							permisoRepository.findByIdIn(dto.getPermisos().stream().map(p->p.getId()).collect(Collectors.toList()));
				}
			}
		}
		if(optPer.isPresent() && userModified!=null) {
			try {
				validate(dto,busqueda,false);
				if(dto.getActive()==null) {
					dto.setActive(true);
				}
				optPer.get().setModified(new Date());
				optPer.get().setUserModified(userModified);
				optPer.get().setDescripcion(dto.getDescripcion());
				optPer.get().setActive(dto.getActive());
				if(modifyPermission) {
					optPer.get().setPermisos(listaPermisos);
				}
				roleRepository.save(optPer.get());
				return true;
			}catch(Exception e) {
				log.error("Problems in Role : {}",e.getMessage());
				throw new Exception(e.getMessage());
			}
		}
		if(userModified==null) {
			log.error("Problems with user modifier in DTO");
			throw new Exception("Problems with user modifier in DTO");
		}
		return false;
	} 
	@Override
	public Boolean delete(RoleDTO dto) throws  Exception{
		log.info("Borrar de rol por {}" , dto.getId());
		Optional<Role> optPer =   roleRepository.findById(dto.getId());
		if(optPer.isPresent()) {
			optPer.get().setActive(false);
			roleRepository.save(optPer.get());
			return true;
		}
		return false;
	}

	@Override
	public List<RoleDTO> findByUser(Long idUser) {
		log.info("Busca roles de este usuario : {}  ",idUser);
		Optional<User> optRol =   userRepository.findById(idUser);
		if(optRol.isPresent()) {
			if(optRol.get().getRoles()!=null) {
				return Relationships.directSelfReferenceRoles(
						optRol.get().getRoles().stream().map(rol->modelMapper.map(rol, RoleDTO.class)).collect(Collectors.toList()));
			}
		}
		return Arrays.asList();
	}
	
	@Override
	public List<RoleDTO> findByUserUnassigned (Long idUser) {
		log.info("Busca rol asignados a este usuario : {}  ",idUser);
		Optional<User> optRol =   userRepository.findById(idUser);
		List<Role> lista=roleRepository.findAll();
		if(optRol.isPresent()) {
			if(optRol.get().getRoles()!=null) {
				lista.removeAll(optRol.get().getRoles());
			}
			return Relationships.directSelfReferenceRoles(
				lista.stream().map(rol->modelMapper.map(rol, RoleDTO.class)).collect(Collectors.toList()));
		}
		return Arrays.asList();
	}
	
	@Override
	public Boolean removeRoleToUser(Long idUser,Long idRol,UserDTO userDTO) throws  Exception{
		log.info("Remueve rol de usuario: {} -> {}  ",idRol,idUser);
		Optional<Role> optRol =   roleRepository.findById(idRol);
		Optional<User> userOpt=userRepository.findById(idUser);
		Optional<User> optModf =   userRepository.findById(userDTO.getId());
		if(optRol.isPresent() && userOpt.isPresent()) {
			userOpt.get().getRoles().remove(optRol.get());
			userOpt.get().setUserModified(optModf.get());
			userRepository.save(userOpt.get());
			return true;
		}
		return false;
	}
	
	@Override
	public Boolean appendRoleToUser(Long idUser,Long idRol,UserDTO userDTO) throws  Exception{
		log.info("Asigna rol de usuario: {} -> {}  ",idRol,idUser);
		Optional<Role> optRol =   roleRepository.findById(idRol);
		Optional<User> userOpt=userRepository.findById(idUser);
		Optional<User> optModf =   userRepository.findById(userDTO.getId());
		if(optRol.isPresent() && userOpt.isPresent()) {
			userOpt.get().getRoles().add(optRol.get());
			userOpt.get().setUserModified(optModf.get());
			userRepository.save(userOpt.get());
			return true;
		}
		return false;
	}

	@Override
	public RoleDTO findById(Long idRol) {
		log.info("Busca rol : {}  ",idRol);
		Optional<Role> optPer =   roleRepository.findById(idRol);
		if(optPer.isPresent()) {
			return Relationships.directSelfReferenceRole(modelMapper.map(optPer.get(), RoleDTO.class));
		}
		return null;
	}

	@Override
	public List<RoleDTO> findAll() {
		log.info("Busca todos los roles ");
		return Relationships.directSelfReferenceRoles(
				roleRepository.findAll().stream().map(rol->modelMapper.map(rol, RoleDTO.class)).collect(Collectors.toList()));
	}
	private void validate(RoleDTO dto, Optional<Role> busqueda, boolean isNew) throws  Exception{
		if(dto.getDescripcion()==null)
			throw new Exception("Error - Its necesary Descripcion");
		if(dto.getDescripcion().isEmpty() || dto.getDescripcion().equals(""))
			throw new Exception("Error - Its necesary Descripcion");
		if(isNew) {
			if(dto.getId()!=null)
				throw new Exception("Error - Id -Its required null");
			if(busqueda.isPresent())
				throw new Exception("Error - Problems with duplicate ["+busqueda.get().getDescripcion()+"]");
		}
		else{
			if(dto.getId()==null)
				throw new Exception("Error - Id -Its required");
			if(busqueda.isPresent()) {
				if(busqueda.get().getId().longValue() != dto.getId().longValue() )
					throw new Exception("Error - Problems with duplicate ["+busqueda.get().getDescripcion()+"]");
			}
		}
	}

}
