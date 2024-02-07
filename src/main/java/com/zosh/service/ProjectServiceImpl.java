package com.zosh.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zosh.exception.ChatException;
import com.zosh.exception.ProjectException;
import com.zosh.exception.UserException;
import com.zosh.model.Chat;
import com.zosh.model.Project;
import com.zosh.model.User;
import com.zosh.repository.ProjectRepository;

import jakarta.transaction.Transactional;

@Service
public class ProjectServiceImpl implements ProjectService {

	 @Autowired
	    private ProjectRepository projectRepository;
	 @Autowired
	 private ChatService chatService;
	 
	 @Autowired 
	   private UserService userService;

	    @Override
	    public Project createProject(Project project,Long id) throws UserException  {
	    	User user = userService.findUserById(id);
	    	Project createdProject=new Project();
	    	if(user!=null) {
				createdProject.setOwner(user);
				createdProject.setTags(project.getTags());
				createdProject.setName(project.getName());
				createdProject.setCategory(project.getCategory());
				createdProject.setDescription(project.getDescription());
	    		createdProject=projectRepository.save(project);
	    		
	            Chat chat = new Chat();
	            chat.setProject(createdProject);
	            Chat projectChat = chatService.createChat(chat);
				createdProject.setChat(projectChat);
	    	}
	    
	        return createdProject;
	    }

	    @Override
	    public List<Project> getProjectsByOwner(User owner) throws ProjectException {
	       List<Project> projects = projectRepository.findByOwner(owner);
	       if(projects!=null) {
	    	   return projects;
	       }
	       throw new ProjectException("No Projects found");
	    }

	    @Override
	    public List<Project> getAllProjects() throws ProjectException {
	        List<Project> projects = projectRepository.findAll();
	        if(projects!=null) return projects;
	        throw new ProjectException("No projects found");
	    }

	    @Override
	    public Project getProjectById(Long projectId) throws ProjectException {
	        Optional<Project> project = projectRepository.findById(projectId);
	        if(project.isPresent()) {
	        	return project.get();
	        }
	        throw new ProjectException("No project exists with the id "+projectId);
	    }

	    @Override
	    public String deleteProject(Long projectId,Long id) throws UserException {
	    	User user = userService.findUserById(id);
	    	System.out.println("user ____>"+user);
	    	if(user!=null) {
	              projectRepository.deleteById(projectId);
	              return "project deleted";
	    } 
	    	throw new UserException("User doesnot exists");
	    }

	    @Override
	    public Project updateProject(Project updatedProject, Long id) throws ProjectException {
	        Project project = getProjectById(id);

	        if (project != null) {
	            // Update the existing project with the fields from updatedProject
	            if (updatedProject.getName() != null) {
	                project.setName(updatedProject.getName());
	            }

	            if (updatedProject.getDescription() != null) {
	                project.setDescription(updatedProject.getDescription());
	            }

	            if (updatedProject.getTags() != null) {
	                project.setTags(updatedProject.getTags());
	            }

	            // Save the updated project once
	            return projectRepository.save(project);
	        }

	        throw new ProjectException("Project does not exist");
	    }

	    @Override
	    public List<Project> searchProjects(String keyword, String category, String tag) throws ProjectException {
	        List<Project> list = projectRepository.searchProjects(keyword, category, tag);
	        if(list!=null) {
	        	return list;
	        }
	        throw new ProjectException("No Projects available");
	    }
	    
	    @Override
	    @Transactional
	    public String addUserToProject(Long projectId, Long userId) throws UserException, ProjectException {
	        Project project = projectRepository.findById(projectId).orElseThrow(() -> new ProjectException("project not found"));
	        User user = userService.findUserById(userId);

	        if (project != null && user != null) {
	            project.getChat().getUsers().add(user);
	            projectRepository.save(project);
	            return "user added to the project";
	        }
			return "user not added to project either project does not exist or user";
	    }

	    @Override
	    public Chat getChatByProjectId(Long projectId) throws ProjectException, ChatException {
	        Project project = projectRepository.findById(projectId).orElseThrow(()-> new ProjectException("Project not found"));
	        if( project != null ) return project.getChat() ;
	        
	        
	        	throw new ChatException("no chats found");
	       
	    }
	    @Override
	    public List<User> getUsersByProjectId(Long projectId) throws ProjectException {
	        Project project = projectRepository.findById(projectId).orElse(null);
	        if( project != null) return project.getChat().getUsers();
	        
	        throw new ProjectException("no project found with id "+projectId);
	    }
	    
	    
}
