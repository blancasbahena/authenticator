package com.truper.saen.authenticator.entities;

import java.util.List;
import java.util.stream.Collectors;

import com.truper.saen.commons.dto.PermisoDTO;
import com.truper.saen.commons.dto.RoleDTO;
import com.truper.saen.commons.dto.UserDTO;

import lombok.extern.slf4j.Slf4j;
@Slf4j
public class Relationships {

	public static UserDTO directSelfReference(UserDTO u) {
		log.info("[directSelfReference] :: {} ",u.getUserName());
		if(u.getUserCreated()!=null) {
			u.setUserCreated(directSelfReferenceUserWithoutPassword(u.getUserCreated()));
		}
		if(u.getUserModified()!=null) {
			u.setUserModified(directSelfReferenceUserWithoutPassword(u.getUserModified()));
		}
		if(u.getRoles()!=null) {
			u.setRoles(directSelfReferenceRoles(u.getRoles()));
		}
		return u;
	}

	public static List<RoleDTO> directSelfReferenceRoles(List<RoleDTO> roles) {
		log.info("[directSelfReferenceRoles] ");
		if(roles!=null) {
			return roles.stream().map(r->directSelfReferenceRole(r)).collect(Collectors.toList());
		}
		return null;
	}
	public static RoleDTO directSelfReferenceRole(RoleDTO role) {
		log.info("[directSelfReferenceRoles] {}",role.getDescripcion());
		if(role!=null) {
			role.setUserCreated(null);
			role.setUserModified(null);
			if(role.getPermisos()!=null) {
				role.setPermisos(directSelfReferencePermissions(role.getPermisos()));
			}
		}
		return role;
	}

	public static List<PermisoDTO> directSelfReferencePermissions(List<PermisoDTO> permisos) {
		log.info("[directSelfReferenceRoles] ");
		if(permisos!=null) {
			return permisos.stream().map(p->directSelfReferencePermission(p)).collect(Collectors.toList());
		}
		return null;
	}
	public static PermisoDTO directSelfReferencePermission(PermisoDTO parent) {
		log.info("[directSelfReferencePermission] :: {} ",parent.getNombrePermiso());
		if(parent!=null) {
			parent.setUserCreated(null);
			parent.setUserModified(null);
			if(parent.getParent()!=null) {
				parent.setParent(directSelfReferencePermission(parent.getParent()));
			}
		}
		return parent;
	}

	public static UserDTO directSelfReferenceUserWithoutPassword(UserDTO user) {
		log.info("[directSelfReferenceUserWithoutPassword] :: {} ",user.getUserName());
		if(user.getRoles()!=null) {
			user.setRoles(null);
		}
		user.setUserCreated(null);
		user.setUserModified(null);
		return user;
	}
}