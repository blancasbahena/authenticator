package com.truper.sae.authenticator.service;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import com.truper.sae.authenticator.entities.Permiso;
import com.truper.sae.authenticator.entities.Role;
import com.truper.sae.authenticator.entities.User;
import com.truper.sae.authenticator.repository.PermisoRepository;
import com.truper.sae.authenticator.repository.RoleRepository;
import com.truper.sae.authenticator.repository.UserRepository;
import com.truper.sae.commons.dto.PermisoDTO;
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
		Optional<Permiso> busqueda =   permisoRepository.findByNombrePermiso(dto.getNombrePermiso());
		User userCreated=null;
		Optional<User> userOpt=userRepository.findById(dto.getUsercreated().getId());
		if(userOpt.isPresent()) {
			userCreated = userOpt.get();
			try {
				validate(dto,busqueda,true);
				Permiso nuevo=modelMapper.map(dto, Permiso.class);
				nuevo.setCreated(new Date());
				nuevo.setModified(new Date());
				nuevo.setUsercreated(userCreated);
				nuevo.setUsermodified(userCreated);
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
		Optional<Permiso> optPer =   permisoRepository.findById(dto.getId());
		Optional<Permiso> busqueda =   permisoRepository.findByNombrePermiso(dto.getNombrePermiso());
		User userModified=null;
		Optional<User> userOpt=userRepository.findById(dto.getUsermodified().getId());
		if(userOpt.isPresent()) {
			userModified = userOpt.get();
		}
		if(optPer.isPresent() && userModified!=null) {
			try {
				validate(dto,busqueda,false);
				optPer.get().setModified(new Date());
				optPer.get().setUsermodified(userModified);
				optPer.get().setDescripcion(dto.getDescripcion());
				optPer.get().setNombrePermiso(dto.getNombrePermiso());
				optPer.get().setParent(dto.getParent());
				optPer.get().setTipo(dto.getTipo());
				optPer.get().setUrl(dto.getUrl());
				optPer.get().setIcon(dto.getIcon());
				optPer.get().setIdentifierAccion(dto.getIdentifierAccion());
				optPer.get().setTooltip(dto.getTooltip());				
				permisoRepository.save(optPer.get());
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
		Optional<Permiso> optPer =   permisoRepository.findById(dto.getId());
		if(optPer.isPresent()) {
			optPer.get().setActive(false);
			permisoRepository.save(optPer.get());
			return true;
		}
		return false;
	}

	@Override
	public List<PermisoDTO> findByRole(Long idRol) {
		Optional<Role> optRol =   roleRepository.findById(idRol);
		if(optRol.isPresent()) {
			return optRol.get().getPermisos().stream().map(permiso->modelMapper.map(permiso, PermisoDTO.class)).collect(Collectors.toList());
		}
		return Arrays.asList();
	}

	@Override
	public PermisoDTO findById(Long idPer) {
		Optional<Permiso> optPer =   permisoRepository.findById(idPer);
		if(optPer.isPresent()) {
			return modelMapper.map(optPer.get(), PermisoDTO.class);
		}
		return null;
	}

	@Override
	public List<PermisoDTO> findAll() {
		return permisoRepository.findAll().stream().map(permiso->modelMapper.map(permiso, PermisoDTO.class)).collect(Collectors.toList());
	}


	private void validate(PermisoDTO dto, Optional<Permiso> busqueda, boolean isNew) throws  Exception{
		if(dto.getNombrePermiso()==null)
			throw new Exception("Error - Its necesary NombrePermiso");
		if(dto.getNombrePermiso().isEmpty() || dto.getNombrePermiso().isBlank())
			throw new Exception("Error - Its necesary NombrePermiso");
		
		if(dto.getDescripcion()==null)
			throw new Exception("Error - Its necesary Descripcion");
		if(dto.getDescripcion().isEmpty() || dto.getDescripcion().isBlank())
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
}

