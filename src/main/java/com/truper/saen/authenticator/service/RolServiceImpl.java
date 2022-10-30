package com.truper.saen.authenticator.service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.truper.sae.commons.dto.RoleDTO;
import com.truper.saen.authenticator.entities.Permiso;
import com.truper.saen.authenticator.entities.Relationships;
import com.truper.saen.authenticator.entities.Role;
import com.truper.saen.authenticator.entities.User;
import com.truper.saen.authenticator.repository.PermisoRepository;
import com.truper.saen.authenticator.repository.RoleRepository;
import com.truper.saen.authenticator.repository.UserRepository;

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
	public Boolean save(RoleDTO dto) {
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
				nuevo.setCreated(new Date());
				nuevo.setModified(new Date());
				nuevo.setUserCreated(userOpt.get());
				nuevo.setUserModified(userOpt.get());
				nuevo.setPermisos(listaPermisos);
				roleRepository.save(nuevo);
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
	public Boolean update(RoleDTO dto) {
		List<Permiso> listaPermisos= null;
		Optional<Role> optPer =   roleRepository.findById(dto.getId());
		Optional<Role> busqueda =   roleRepository.findByDescripcion(dto.getDescripcion());
		User userModified=null;
		Optional<User> userOpt=userRepository.findById(dto.getUserModified().getId());
		if(userOpt.isPresent()) {
			userModified = userOpt.get();
		}
		if(dto.getPermisos()!=null) {
			if(!dto.getPermisos().isEmpty()) {
				listaPermisos = 
						permisoRepository.findByIdIn(dto.getPermisos().stream().map(p->p.getId()).collect(Collectors.toList()));
			}
		}
		if(optPer.isPresent() && userModified!=null) {
			try {
				validate(dto,busqueda,false);
				optPer.get().setModified(new Date());
				optPer.get().setUserModified(userModified);
				optPer.get().setDescripcion(dto.getDescripcion());
				optPer.get().setPermisos(listaPermisos);
				roleRepository.save(optPer.get());
			}catch(Exception e) {
				log.error("Problems in Role : {}",e.getMessage());
			}
		}
		if(userModified==null) {
			log.error("Problems with the modifying user");
		}
		return false;
	}

	@Override
	public Boolean delete(RoleDTO dto) {
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
	public RoleDTO findById(Long idRol) {
		Optional<Role> optPer =   roleRepository.findById(idRol);
		if(optPer.isPresent()) {
			return Relationships.directSelfReferenceRole(modelMapper.map(optPer.get(), RoleDTO.class));
		}
		return null;
	}

	@Override
	public List<RoleDTO> findAll() {
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
