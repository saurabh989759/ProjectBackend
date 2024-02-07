package com.zosh.service;

import java.util.List;

import com.zosh.exception.ChatException;
import com.zosh.exception.ProjectException;
import com.zosh.exception.UserException;
import com.zosh.model.Chat;
import com.zosh.model.Project;
import com.zosh.model.User;

public interface ProjectService {
	Project createProject(Project project, Long userId) throws UserException;

	List<Project> getProjectsByOwner(User owner) throws ProjectException;

	List<Project> getAllProjects() throws ProjectException;

	Project getProjectById(Long projectId) throws ProjectException;

	String deleteProject(Long projectId,Long userId) throws UserException;

	Project updateProject(Project updatedProject, Long id) throws ProjectException;
	
	List<Project> searchProjects(String keyword, String category, String tag) throws ProjectException;

	String addUserToProject(Long projectId, Long userId) throws UserException, ProjectException;

	Chat getChatByProjectId(Long projectId) throws ProjectException, ChatException;

	List<User> getUsersByProjectId(Long projectId) throws ProjectException;
}
