package com.truper.saen.authenticator.service;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.truper.saen.authenticator.entities.Relationships;
import com.truper.saen.authenticator.entities.Role;
import com.truper.saen.authenticator.entities.User;
import com.truper.saen.authenticator.repository.RoleRepository;
import com.truper.saen.authenticator.repository.UserRepository;
import com.truper.saen.commons.dto.RoleDTO;
import com.truper.saen.commons.dto.UserDTO;

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
	private User userModified=null;
	private User userCreated=null;
	@Override
	public Boolean save(UserDTO dto) throws Exception {
		List<Role> listaRoles= null;
		Optional<User> busquedaUserName =   userRepository.findByUserName(dto.getUserName());
		Optional<User> busquedaCorreo =   userRepository.findByEmail(dto.getEmail());
		if(dto.getUserCreated()!=null) {
			Optional<User> userOpt=userRepository.findById(dto.getUserCreated().getId());
			if(userOpt.isPresent()) {
				userCreated = userOpt.get();
			}
		}
		if(userCreated!=null) {
			try {
				if(dto.getActive()==null) {
					dto.setActive(false);
				}
				validate(dto,busquedaUserName,busquedaCorreo,true);
				User nuevo=modelMapper.map(dto, User.class);
				nuevo.setCreated(new Date());
				nuevo.setModified(new Date());
				nuevo.setUserCreated(userCreated);
				nuevo.setUserModified(userCreated);
				if(dto.getRoles()!=null) {
					if(!dto.getRoles().isEmpty()) {
						List<Long> ids =dto.getRoles().stream().map(p->p.getId()).collect(Collectors.toList());
						listaRoles = roleRepository.findByIdIn(ids);
					}
				}
				nuevo.setRoles(listaRoles);
				userRepository.save(nuevo);
				return true;
			}catch(Exception e) {
				log.error("Problemas en Users : {}",e.getMessage());
				throw new Exception(e.getMessage());
			}
		}else{
			log.error("Problems with the creating user");
		}
		return false;
	}

	@Override
	public Boolean update(UserDTO dto) throws Exception {
		List<Role> listaRoles= null;
		Optional<User> optPer =   userRepository.findById(dto.getId());
		Optional<User> busquedaUserName =   userRepository.findByUserName(dto.getUserName());
		Optional<User> busquedaCorreo =   userRepository.findByEmail(dto.getEmail());
		if(dto.getUserCreated()!=null) {
			if(dto.getUserModified()!=null) {
				Optional<User> userOpt=userRepository.findById(dto.getUserModified().getId());
				if(userOpt.isPresent()) {
					userModified = userOpt.get();
				}
			}
		}
		if(optPer.isPresent() && userModified!=null) {
			if(dto.getRoles()!=null) {
				if(!dto.getRoles().isEmpty()) {
					List<Long> ids =dto.getRoles().stream().map(p->p.getId()).collect(Collectors.toList());
					listaRoles = roleRepository.findByIdIn(ids);
				}
			}
			try {
				validate(dto,busquedaUserName,busquedaCorreo,false);
				optPer.get().setModified(new Date());
				optPer.get().setUserModified(userModified);
				optPer.get().setName(dto.getName());
				optPer.get().setRoles(listaRoles);
				optPer.get().setEmail(dto.getEmail());
				optPer.get().setUserAD(dto.getUserAD());
				optPer.get().setPwdReset(dto.getPwdReset());
				optPer.get().setRoles(listaRoles);
				userRepository.save(optPer.get());
				return true;
			}catch(Exception e) {
				log.error("Problemas en Users : {}",e.getMessage());
				throw new Exception(e.getMessage());
			}
		} 
		return false;
	}
	@Override
	public Boolean updateDetail(UserDTO dto) throws Exception {
		List<Role> listaRoles= null;
		Optional<User> optPer =   userRepository.findById(dto.getId());
		Optional<User> busquedaUserName =   userRepository.findByUserName(dto.getUserName());
		Optional<User> busquedaCorreo =   userRepository.findByEmail(dto.getEmail());
		if(dto.getUserCreated()!=null) {
			if(dto.getUserModified()!=null) {
				Optional<User> userOpt=userRepository.findById(dto.getUserModified().getId());
				if(userOpt.isPresent()) {
					userModified = userOpt.get();
				}
			}
		}
		if(optPer.isPresent() && userModified!=null) {
			try {
				validate(dto,busquedaUserName,busquedaCorreo,false);
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
				log.error("Problemas en Users : {}",e.getMessage());
				throw new Exception(e.getMessage());
			}
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
	public Boolean delete(UserDTO dto) throws  Exception{
		Optional<User> optPer =   userRepository.findById(dto.getId());
		if(optPer.isPresent()) {
			optPer.get().setActive(false);
			userRepository.save(optPer.get());
			return true;
		}
		throw new Exception("Error - Its necesary id from user"); 
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
	private void validate(UserDTO dto, Optional<User> busquedaUserName,Optional<User> busquedaCorreo, boolean isNew) throws  Exception{
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
		
		
		if(busquedaUserName.isPresent()) {
			if(busquedaUserName.get().getUserName().equals(dto.getUserName()))
				throw new Exception("Error - Problems with duplicate ["+busquedaUserName.get().getUserName()+"]");
		}
		if(busquedaCorreo.isPresent()) {
			if(busquedaCorreo.get().getEmail().equals(dto.getEmail()))
				throw new Exception("Error - Problems with duplicate ["+busquedaCorreo.get().getEmail()+"]");
		}
		if(isNew) {
			if(dto.getId()!=null)
				throw new Exception("Error - Id -Its required null");
		}
		else{
			if(dto.getId()==null)
				throw new Exception("Error - Id -Its required");
			
		}		
	}

}
