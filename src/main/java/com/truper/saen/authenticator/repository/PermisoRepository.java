package com.truper.saen.authenticator.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.truper.saen.authenticator.projection.PermisoProjection;
import com.truper.saen.commons.entities.Permiso;
@Repository
public interface PermisoRepository extends JpaRepository<Permiso, Long> {

	Optional<Permiso> findByNombrePermiso(String string);

	List<Permiso> findByIdIn(List<Long> ids);
	
	@Query(value="SELECT DISTINCT (p.id), UPPER(p.descripcion) as descripcion, p.icon, p.tooltip, p.url, p.parent, (select rp2.id_rol as tipo  from roles_permisos rp2 where rp2.id_permisos=p.id and id_rol=:idRol) as tipo\r\n"
			+ "FROM roles r \r\n"
			+ "right JOIN roles_permisos rp ON rp.id_rol = r.id \r\n"
			+ "right JOIN permisos p ON p.id = rp.id_permisos \r\n"
			+ "WHERE \r\n"
			+ "p.tipo = 'ACCESS_MENU'\r\n"
			+ "AND p.parent IS NULL\r\n"
			+ "AND p.active = 1 \r\n"
			+ "GROUP by p.id, p.descripcion, p.icon, p.tooltip, p.url, p.parent\r\n"
			+ "union all\r\n"
			+ "SELECT DISTINCT p.id, UPPER(p.descripcion) as descripcion, p.icon, p.tooltip, p.url, p.parent, (select rp2.id_rol  from roles_permisos rp2 where rp2.id_permisos=p.id and id_rol=:idRol) as tipo\r\n"
			+ "FROM roles r  \r\n"
			+ "right JOIN roles_permisos rp ON rp.id_rol = r.id \r\n"
			+ "right JOIN permisos p ON p.id = rp.id_permisos \r\n"
			+ "WHERE \r\n"
			+ "p.active = 1 \r\n"
			+ "AND p.tipo = 'ACCESS_SUBMENU'", nativeQuery = true)
	List<PermisoProjection> pantallas(Long idRol);
	
	@Query(value="SELECT DISTINCT p.id, p.descripcion, p.icon, p.tooltip, p.url, p.parent, p.tipo \r\n"
			+ "FROM roles r  \r\n"
			+ "INNER JOIN roles_permisos rp ON rp.id_rol = r.id \r\n"
			+ "INNER JOIN permisos p ON p.id = rp.id_permisos \r\n"
			+ "WHERE \r\n"
			+ "p.parent = :idPantalla\r\n"
			+ "and rp.id_rol <> :idRol\r\n"
			+ "and p.id not in (SELECT DISTINCT p.id\r\n"
			+ "FROM roles r  \r\n"
			+ "INNER JOIN roles_permisos rp ON rp.id_rol = r.id \r\n"
			+ "INNER JOIN permisos p ON p.id = rp.id_permisos \r\n"
			+ "WHERE \r\n"
			+ "p.parent = :idPantalla\r\n"
			+ "and r.id = :idRol\r\n"
			+ "and p.active = 1 \r\n"
			+ "AND p.tipo = 'MENU_ACCION')\r\n"
			+ "and p.active = 1 \r\n"
			+ "AND p.tipo = 'MENU_ACCION'", nativeQuery = true)
	List<PermisoProjection> findUnassing(Long idRol,Long idPantalla);
	
	@Query(value="SELECT DISTINCT p.id, p.descripcion, p.icon, p.tooltip, p.url, p.parent, p.tipo \r\n"
			+ "FROM roles r  \r\n"
			+ "INNER JOIN roles_permisos rp ON rp.id_rol = r.id \r\n"
			+ "INNER JOIN permisos p ON p.id = rp.id_permisos \r\n"
			+ "WHERE \r\n"
			+ "p.parent = :idPantalla\r\n"
			+ "and r.id = :idRol\r\n"
			+ "and p.active = 1 \r\n"
			+ "AND p.tipo = 'MENU_ACCION'", nativeQuery = true)
	List<PermisoProjection> findAssing(Long idRol,Long idPantalla);
	
	@Query(value=
			"SELECT DISTINCT (p.id), p.descripcion, p.icon, p.tooltip, p.url, p.parent, p.tipo, p.orden \r\n"
			+ "FROM usuarios u \r\n"
			+ "INNER JOIN user_roles ur ON ur.id_user = u.id \r\n"
			+ "INNER JOIN roles r ON r.id = ur.id_rol \r\n"
			+ "INNER JOIN roles_permisos rp ON rp.id_rol = r.id \r\n"
			+ "INNER JOIN permisos p ON p.id = rp.id_permisos \r\n"
			+ "WHERE u.id = :idUser\r\n"
			+ "AND p.tipo = 'ACCESS_MENU'\r\n"
			+ "AND p.parent IS NULL\r\n"
			+ "AND p.active = 1 \r\n"
			+ "GROUP by p.id, p.descripcion, p.icon, p.tooltip, p.url, p.parent, p.tipo, p.orden \r\n"
			+ "ORDER BY p.orden ASC;", nativeQuery = true)
	List<PermisoProjection> permisosMenu(Long idUser);
	
	@Query(value=
			"SELECT p.id, p.descripcion, p.icon, p.tooltip, p.url, p.parent, p.tipo \r\n"
			+ "FROM usuarios u \r\n"
			+ "INNER JOIN user_roles ur ON ur.id_user = u.id \r\n"
			+ "INNER JOIN roles r ON r.id = ur.id_rol \r\n"
			+ "INNER JOIN roles_permisos rp ON rp.id_rol = r.id \r\n"
			+ "INNER JOIN permisos p ON p.id = rp.id_permisos \r\n"
			+ "WHERE u.id = :idUser\r\n"
			+ "AND p.parent = :parent\r\n"
			+ "AND p.active = 1 "
			+ "AND p.tipo = 'ACCESS_SUBMENU' "
			+ "ORDER BY p.orden ASC", nativeQuery = true)
	List<PermisoProjection> permisosSubMenu(Long idUser, Long parent);
	
	
	@Query(value=
			"SELECT p.id, p.descripcion, p.icon, p.tooltip, p.url, p.parent, p.tipo, p.identifier_accion as accion\r\n"
			+ "FROM usuarios u \r\n"
			+ "INNER JOIN user_roles ur ON ur.id_user = u.id \r\n"
			+ "INNER JOIN roles r ON r.id = ur.id_rol \r\n"
			+ "INNER JOIN roles_permisos rp ON rp.id_rol = r.id \r\n"
			+ "INNER JOIN permisos p ON p.id = rp.id_permisos \r\n"
			+ "WHERE u.id = :idUser\r\n"
			+ "AND p.parent = :parent\r\n"
			+ "AND p.active = 1 "
			+ "AND p.tipo = 'MENU_ACCION' "
			+ "ORDER BY p.orden ASC", nativeQuery = true)
	List<PermisoProjection> permisosMenuAccion(Long idUser, Long parent);

}
