package com.zosh.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.zosh.model.Project;
import com.zosh.model.User;

public interface ProjectRepository extends JpaRepository<Project, Long> {
	 List<Project> findByOwner(User owner);
	 

	    
	    @Query("SELECT p FROM Project p WHERE p.name LIKE %?1% AND p.category LIKE %?2% AND p.description LIKE %?3%")
	    List<Project> searchProjects(String name, String category, String description);

	@Query("SELECT p FROM Project p JOIN p.team t WHERE t = :user")
	List<Project> findProjectsByTeam(@Param("user") User user);

}
