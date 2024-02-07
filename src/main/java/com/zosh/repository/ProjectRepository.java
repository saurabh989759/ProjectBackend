package com.zosh.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.zosh.model.Project;
import com.zosh.model.User;

public interface ProjectRepository extends JpaRepository<Project, Long> {
	 List<Project> findByOwner(User owner);
	 
//	// Custom query to search by name containing keyword and filter by category
//	    @Query("SELECT p FROM Project p WHERE " +
//	           "(:keyword IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
//	           "AND (:category IS NULL OR p.category = :category) " +
//	           "AND (:tag IS NULL OR :tag IN elements(p.tags))")
//	    List<Project> searchProjects(@Param("keyword") String keyword,
//	                                @Param("category") String category,
//	                                @Param("tag") String tag);
	    
	    @Query("SELECT p FROM Project p WHERE p.name LIKE %?1% AND p.category LIKE %?2% AND p.description LIKE %?3%")
	    List<Project> searchProjects(String name, String category, String description);

		

}
