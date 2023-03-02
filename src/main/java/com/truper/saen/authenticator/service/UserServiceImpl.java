package com.truper.saen.authenticator.service;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.truper.saen.authenticator.client.EnvioCorreoReseteoPassClient;
import com.truper.saen.authenticator.dto.ResetPasswordDTO;
import com.truper.saen.authenticator.repository.RoleRepository;
import com.truper.saen.authenticator.repository.UserRepository;
import com.truper.saen.commons.dto.UserDTO;
import com.truper.saen.commons.entities.Relationships;
import com.truper.saen.commons.entities.Role;
import com.truper.saen.commons.entities.User;

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
	private final EnvioCorreoReseteoPassClient reseteoPassClient;
	private final Integer PLANTILLA_RESETEO_EXITO=27;
	private final Integer PLANTILLA_RESETEO_ERROR=28;
	private User userModified=null;
	private User userCreated=null;
	@Value("${sae-batch.subject}")
	private String subject;
	@Value("${sae-batch.cc}")
	private String copia;
	@Override
	public Boolean save(UserDTO dto) throws Exception {
		if(dto!=null) {
			if(dto.getUserName()!=null) {
				log.info("Creando usuario  {}",dto.getUserName());
				List<Role> listaRoles= null;
				Optional<User> busquedaUserName =   userRepository.findByUserNameAndActive(dto.getUserName(),true);
				Optional<User> busquedaCorreo =   userRepository.findByEmailAndActive(dto.getEmail(),true);
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
						validateNuevo(dto,busquedaUserName,busquedaCorreo);
						User nuevo=modelMapper.map(dto, User.class);
						nuevo.setCreated(new Date());
						nuevo.setModified(new Date());
						nuevo.setUserCreated(userCreated);
						nuevo.setUserModified(userCreated);
						if(dto.getRoles()!=null) {
							if(!dto.getRoles().isEmpty()) {
								List<Long> ids =dto.getRoles().stream().map(p->p.getId()).collect(Collectors.toList());
								listaRoles = roleRepository.findByIdIn(ids);
								nuevo.setRoles(listaRoles);
							}
						}
						userRepository.save(nuevo);
						return true;
					}catch(Exception e) {
						log.error("Problemas en Users : {}",e.getMessage());
						throw new Exception(e.getMessage());
					}
				}else{
					log.error("Problems with the creating user");
				}
			}
		}
		return false;
	}

	@Override
	public Boolean update(UserDTO dto,boolean modificarRoles,boolean soloPassword) throws Exception {
		log.info("Modificando usuario  {}",dto.getUserName());
		List<Role> listaRoles= null;
		User  usuarioAmodificar =  null;
		Optional<User> optPer =   userRepository.findById(dto.getId());
		Optional<User> busquedaUserName =   userRepository.findByUserNameAndActive(dto.getUserName(),true);
		if(dto.getUserModified()!=null) {
			Optional<User> userOpt=userRepository.findById(dto.getUserModified().getId());
			if(userOpt.isPresent()) {
				userModified = userOpt.get();
			}
		}
		if(optPer.isPresent()) {
			usuarioAmodificar = optPer.get();
		}
		else if(busquedaUserName.isPresent()) {
			usuarioAmodificar = busquedaUserName.get();
		}
		if(usuarioAmodificar!=null && userModified!=null) {
			if(usuarioAmodificar.getActive()) {
				if(modificarRoles) {
					if(dto.getRoles()!=null) {
						if(!dto.getRoles().isEmpty()) {
							List<Long> ids =dto.getRoles().stream().map(p->p.getId()).collect(Collectors.toList());
							listaRoles = roleRepository.findByIdIn(ids);
						}
					}
				}
				try {
					validateModificado(dto,userRepository.findAll());
					usuarioAmodificar.setModified(new Date());
					usuarioAmodificar.setUserModified(userModified);
					if(!soloPassword) {
						usuarioAmodificar.setName(dto.getName());
						usuarioAmodificar.setEmail(dto.getEmail());
						usuarioAmodificar.setUserAD(dto.getUserAD());
						usuarioAmodificar.setPwdreset(dto.getPwdreset());
					}
					if(modificarRoles) {
						usuarioAmodificar.setRoles(listaRoles);
					}
					if(soloPassword) {
						usuarioAmodificar.setPassword(dto.getPassword());
					}
					userRepository.save(usuarioAmodificar);
					return true;
				}catch(Exception e) {
					log.error("Problemas en Users : {}",e.getMessage());
					throw new Exception(e.getMessage());
				}
			}else {
				throw new Exception("Error - User is disable");
			}
		} 
		return false;
	} 
	@Override
	public Boolean delete(UserDTO dto) throws  Exception{
		log.info("Borrando usuario  {}",dto.getId());
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
		log.info("Consultando usuario  {}",idUser);
		Optional<User> optPer =   userRepository.findByIdAndActive(idUser,true);
		if(optPer.isPresent()) {
			log.info("Consultando usuario-ID  {}",optPer.get().getId());
			return Relationships.directSelfReference(modelMapper.map(optPer.get(), UserDTO.class));
		}
		return null;
	}
	@Override
	public ResetPasswordDTO prepareReset(String userName,String authorization) throws  Exception{
		log.info("prepareReset usuario -prepareReset  {}",userName);
		Optional<User> optPer =   userRepository.findByUserName(userName);
		if(optPer.isPresent()) {
			log.info("Consultando usuario-ID  {}",optPer.get().getId());
			//optPer.get().setPwdreset(true);
			///optPer.get().setActive(false);
			//optPer.get().setResetExpira(sumarTiempo(new Date(),1));
			//User user = userRepository.save(optPer.get());
			boolean activeDirectory = optPer.get().getUserAD()==null?false:optPer.get().getUserAD();
			log.info("Guardando informacion de usuario -prepareReset  y enviamos Datos  {}",userName);
			ResetPasswordDTO dto = ResetPasswordDTO.builder()
					.correo(optPer.get().getEmail())
					.nombre(optPer.get().getName())
					.usuario(optPer.get().getUserName())
					.copia(copia)
					.subject(subject)
					.numeroPlantilla(activeDirectory?PLANTILLA_RESETEO_ERROR:PLANTILLA_RESETEO_EXITO)
					.build();
			log.info("DTO: "+dto.toString());
			reseteoPassClient.envioCorreoResetPassword(authorization, dto);
			return dto;
		}
		log.info("Problemas con el reseteo de password -prepareReset  {}",userName);
		return null;
	}
	@Override
	public UserDTO reset(String userName,String password) throws  Exception{
		log.info("reset usuario -reset  {}",userName);
		log.info("reset pasasword -reset  {}",password);
		Optional<User> optPer =   userRepository.findByUserName(userName);
		if(optPer.isPresent()) {
			log.info("Consultando usuario-ID  {}",optPer.get().getId());
			optPer.get().setPwdreset(false);
			optPer.get().setActive(true);
			optPer.get().setResetExpira(null);
			optPer.get().setPassword(password);
			User user = userRepository.save(optPer.get());
			if(user!=null) {
				log.info("Guardando informacion de usuario- reset  {}",userName);
				UserDTO dto= Relationships.directSelfReference(modelMapper.map(user, UserDTO.class));
				dto.setUserModified(null);
				dto.setUserCreated(null);
				dto.setRoles(null);
				return  dto;
			}
		}
		log.info("Problemas con el reseteo de password  -reset {}",userName);
		return null;
	}
	public Date sumarTiempo(Date fecha, int hora){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(fecha); // Configuramos la fecha que se recibe
		calendar.set(Calendar.HOUR, calendar.get(Calendar.HOUR)+ hora);
		return calendar.getTime(); // Devuelve el objeto Date con los nuevos días añadidos	
	}
	@Override
	public UserDTO findByUserName(String userName) {
		log.info("Consultando usuario  {}",userName);
		Optional<User> optPer =   userRepository.findByUserNameAndActive(userName,true);
		if(optPer.isPresent()) {
			log.info("Consultando usuario-USERNAME  {}",userName);
			return  Relationships.directSelfReference(modelMapper.map(optPer.get(), UserDTO.class));
		}
		return null;
	}
	
	private void validateModificado(UserDTO dto,List<User> lista) throws  Exception{
		validate(dto);
		if(dto.getId()==null) {
			throw new Exception("Error - Id -Its required");
		}
		List<User> listaBusquedaUserName = lista.stream().filter(u-> u.getUserName().equals(dto.getUserName())).collect(Collectors.toList());
		List<User> listaBusquedaCorreo = lista.stream().filter(u-> u.getEmail().equals(dto.getEmail())).collect(Collectors.toList());
		if(!listaBusquedaUserName.isEmpty()) {
			if(listaBusquedaUserName.size()==1) {
				Optional<User> userM=listaBusquedaUserName.stream().filter(u-> u.getId().longValue() ==  dto.getId().longValue()).findFirst();
				if(!userM.isPresent()) {
					throw new Exception("Error - Problems with duplicate ["+dto.getUserName()+"]");
				}
			}else {
				throw new Exception("Error - Problems with duplicate ["+dto.getUserName()+"]");
			}
		}
		if(!listaBusquedaCorreo.isEmpty()) {
			if(listaBusquedaCorreo.size()==1) {
				Optional<User> userM=listaBusquedaCorreo.stream().filter(u-> u.getId().longValue() ==  dto.getId().longValue()).findFirst();
				if(!userM.isPresent()) {
					throw new Exception("Error - Problems with duplicate ["+dto.getEmail()+"]");
				}
			}else {
				throw new Exception("Error - Problems with duplicate ["+dto.getEmail()+"]");
			}
		}
	}
	private void validateNuevo(UserDTO dto, Optional<User> busquedaUserName,Optional<User> busquedaCorreo) throws  Exception{
		validate(dto);
		if(busquedaUserName.isPresent()) {
			if(busquedaUserName.get().getUserName().equals(dto.getUserName()))
				throw new Exception("Error - Problems with duplicate ["+busquedaUserName.get().getUserName()+"]");
		}
		if(busquedaCorreo.isPresent()) {
			if(busquedaCorreo.get().getEmail().equals(dto.getEmail()))
				throw new Exception("Error - Problems with duplicate ["+busquedaCorreo.get().getEmail()+"]");
		}
		if(dto.getId()!=null)
			throw new Exception("Error - Id -Its required null");		
	}
	private void validate(UserDTO dto) throws  Exception{
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
	}
	@Override
	public List<UserDTO> findall(){
		log.info("Inicia proceso para consultar muchos usuarios");
		List<User> usuarios  =userRepository.findAll();
		if(usuarios!=null && !usuarios.isEmpty()) {
			return usuarios.stream().map(user->
			Relationships.directSelfReference(modelMapper.map(user, UserDTO.class))
			).collect(Collectors.toList());
		}
		return Arrays.asList();
	}
}
