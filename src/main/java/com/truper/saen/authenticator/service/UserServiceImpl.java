package com.truper.saen.authenticator.service;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.truper.sae.commons.dto.PermisoDTO;
import com.truper.sae.commons.dto.RoleDTO;
import com.truper.sae.commons.dto.UserDTO;
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
@Transactional
@Service
@Slf4j
public class UserServiceImpl implements UserService {
	private final ModelMapper modelMapper;
	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final PermisoRepository permisoRepository;
	private User userModified=null;
	private User userCreated=null;
	@Override
	public Boolean save(UserDTO dto) {
		List<Role> listaRoles= null;
		Optional<User> busqueda =   userRepository.findByUserName(dto.getUserName());
		if(dto.getUserCreated()!=null) {
			Optional<User> userOpt=userRepository.findById(dto.getUserCreated().getId());
			if(userOpt.isPresent()) {
				userCreated = userOpt.get();
			}
		}
		if(userCreated!=null) {
			try {
				validate(dto,busqueda,true);
				User nuevo=modelMapper.map(dto, User.class);
				nuevo.setCreated(new Date());
				nuevo.setModified(new Date());
				nuevo.setUserCreated(userCreated);
				nuevo.setUserModified(userCreated);
				if(dto.getRoles()!=null) {
					if(!dto.getRoles().isEmpty()) {
						List<Long> ids =dto.getRoles().stream().map(p->p.getId()).collect(Collectors.toList());
						listaRoles = roleRepository.findByIdIn(ids);
						List<Role>  listaConPermisos=new ArrayList<Role>();
						listaRoles.stream().forEach(r->{
							List<Permiso> permisos= null;
							List<Long> idsPermisos = consultaPermisos(r.getId(),dto.getRoles());
							if(idsPermisos!=null) {
								permisos =permisoRepository.findByIdIn(idsPermisos);
								permisos.stream().forEach(m-> log.info("Permiso encontrado --> {} {} ",m.getId(),m.getNombrePermiso()));
							}
							r.setPermisos(permisos);
							listaConPermisos.add(r);
							r.setUserModified(userCreated);
							roleRepository.save(r);
							log.info("Role Modificado {} , {}",r.getId(),r.getDescripcion());
						});
						nuevo.setRoles(listaConPermisos);
						
					}
				}
				userRepository.save(nuevo);
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
	public Boolean update(UserDTO dto) {
		List<Role> listaRoles= null;
		Optional<User> optPer =   userRepository.findById(dto.getId());
		Optional<User> busqueda =   userRepository.findByUserName(dto.getUserName());
		if(dto.getUserModified()!=null) {
			Optional<User> userOpt=userRepository.findById(dto.getUserModified().getId());
			if(userOpt.isPresent()) {
				userModified = userOpt.get();
			}
		}
		if(optPer.isPresent() && userModified!=null) {
			if(dto.getRoles()!=null) {
				if(!dto.getRoles().isEmpty()) {
					List<Long> ids =dto.getRoles().stream().map(p->p.getId()).collect(Collectors.toList());
					listaRoles = roleRepository.findByIdIn(ids);
					List<Role>  listaConPermisos=new ArrayList<Role>();
						listaRoles.stream().forEach(r->{
							List<Permiso> permisos= null;
							List<Long> idsPermisos = consultaPermisos(r.getId(),dto.getRoles());
							if(idsPermisos!=null) {
								permisos =permisoRepository.findByIdIn(idsPermisos);
								permisos.stream().forEach(m-> log.info("Permiso encontrado--> {} {} ",m.getId(),m.getNombrePermiso()));
							}
							r.setPermisos(permisos);
							listaConPermisos.add(r);
							r.setUserModified(userModified);
							roleRepository.save(r);
							log.info("Role Modificado {} , {}",r.getId(),r.getDescripcion());
						});
						optPer.get().setRoles(listaConPermisos);
					
				}
			}
			try {
				validate(dto,busqueda,false);
				optPer.get().setModified(new Date());
				optPer.get().setUserModified(userModified);
				optPer.get().setName(dto.getName());
				optPer.get().setRoles(listaRoles);
				optPer.get().setEmail(dto.getEmail());
				optPer.get().setUserAD(dto.getUserAD());
				optPer.get().setPwdReset(dto.getPwdReset());				
				userRepository.save(optPer.get());
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

	private List<Long> consultaPermisos(Long id, List<RoleDTO> roles) {
		List<Long> idsPermisos = Arrays.asList();
		Optional<RoleDTO> role=
				roles.stream().filter(r -> r.getId().longValue() == id.longValue()).findAny();
		if(role.isPresent()) {
			if(role.get().getPermisos()!=null) {
				idsPermisos=
					role.get().getPermisos().stream()
						.map(p->p.getId()).collect(Collectors.toList());
			}
		}
		return idsPermisos;
	}

	@Override
	public Boolean delete(UserDTO dto) {
		Optional<User> optPer =   userRepository.findById(dto.getId());
		if(optPer.isPresent()) {
			optPer.get().setActive(false);
			userRepository.save(optPer.get());
			return true;
		}
		return false;
	}

	@Override
	public UserDTO findById(Long idUser) {
		Optional<User> optPer =   userRepository.findById(idUser);
		if(optPer.isPresent()) {
			return Relationships.directSelfReference(modelMapper.map(optPer.get(), UserDTO.class));
		}
		return null;
	}

	@Override
	public UserDTO findByUserName(String userName) {
		Optional<User> optPer =   userRepository.findByUserName(userName);
		if(optPer.isPresent()) {
			return  Relationships.directSelfReference(modelMapper.map(optPer.get(), UserDTO.class));
		}
		return null;
	}
	private void validate(UserDTO dto, Optional<User> busqueda, boolean isNew) throws  Exception{
		if(dto.getUserName()==null)
			throw new Exception("Error - Its necesary UserName");
		if(dto.getUserName().isEmpty() || dto.getUserName().equals(""))
			throw new Exception("Error - Its necesary UserName");
		
		if(dto.getPassword()==null)
			throw new Exception("Error - Its necesary Password");
		if(dto.getPassword().isEmpty() || dto.getPassword().equals(""))
			throw new Exception("Error - Its necesary Password");
		
		if(dto.getEmail()==null)
			throw new Exception("Error - Its necesary Email");
		if(dto.getEmail().isEmpty() || dto.getEmail().equals(""))
			throw new Exception("Error - Its necesary Email");
		
		if(dto.getName()==null)
			throw new Exception("Error - Its necesary Name");
		if(dto.getName().isEmpty() || dto.getName().equals(""))
			throw new Exception("Error - Its necesary Name");
		
		
		
		if(isNew) {
			if(dto.getId()!=null)
				throw new Exception("Error - Id -Its required null");
			if(busqueda.isPresent())
				throw new Exception("Error - Problems with duplicate ["+busqueda.get().getUserName()+"]");
		}
		else{
			if(dto.getId()==null)
				throw new Exception("Error - Id -Its required");
			if(busqueda.isPresent()) {
				if(busqueda.get().getId().longValue() != dto.getId().longValue() )
					throw new Exception("Error - Problems with duplicate ["+busqueda.get().getUserName()+"]");
			}
		}
	}

}
