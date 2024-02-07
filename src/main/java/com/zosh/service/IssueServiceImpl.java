package com.zosh.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zosh.exception.IssueException;
import com.zosh.exception.ProjectException;
import com.zosh.exception.UserException;
import com.zosh.model.Issue;
import com.zosh.model.Project;
import com.zosh.model.User;
import com.zosh.repository.IssueRepository;
import com.zosh.request.IssueRequest;

@Service
public class IssueServiceImpl implements IssueService {

	@Autowired
	private IssueRepository issueRepository;
//
	@Autowired
	private UserService userService;
	@Autowired
	private ProjectService projectService;
	@Autowired
	private NotificationServiceImpl notificationServiceImpl;

//    @Override
//    public List<Issue> getAllIssues() throws IssueException {
//        List<Issue> issues = issueRepository.findAll();
//        if(issues!=null) {
//        	return issues;
//        }
//        throw new IssueException("No issues found");
//    }

	@Override
	public Optional<Issue> getIssueById(Long issueId) throws IssueException {
		Optional<Issue> issue = issueRepository.findById(issueId);
		if (issue.isPresent()) {
			return issue;
		}
		throw new IssueException("No issues found with issueid" + issueId);
	}

	@Override
	public Issue createIssue(IssueRequest issueRequest, Long userId)
			throws UserException, IssueException, ProjectException {
		User user = getUserOrThrow(userId);

		// Check if the project exists
		Project project = projectService.getProjectById(issueRequest.getProjId());
		System.out.println("projid---------->"+issueRequest.getProjId());
		if (project == null) {
			throw new IssueException("Project not found with ID: " + issueRequest.getProjId());
		}

		// Create a new issue
		Issue issue = new Issue();
		issue.setTitle(issueRequest.getTitle());
		issue.setDescription(issueRequest.getDescription());
		issue.setStatus(issueRequest.getStatus());
		issue.setProjID(issueRequest.getProjId());
		issue.setPriority(issueRequest.getPriority());
		issue.setDueDate(issueRequest.getDueDate());

		// Assign the user as an assignee
		User assignee = userService.findUserById(issueRequest.getUserId());
		if (assignee == null) {
			throw new UserException("Assignee not found with ID: " + issueRequest.getUserId());
		}
		 notifyAssignee(assignee.getEmail(), "Issue Updated", "The issue has been updated.");
		issue.getAssignee().add(assignee);
         
		// Set the project for the issue
		issue.setProject(project);

		// Save the issue
		return issueRepository.save(issue);
	}

	@Override
	public Optional<Issue> updateIssue(Long issueId, IssueRequest updatedIssue, Long userId)
			throws IssueException, UserException, ProjectException {
		User user = getUserOrThrow(userId);
		Optional<Issue> existingIssue = issueRepository.findById(issueId);
                           
		if (existingIssue.isPresent()) {
			// Check if the project exists
			Project project = projectService.getProjectById(updatedIssue.getProjId());
			if (project == null) {
				throw new IssueException("Project not found with ID: " + updatedIssue.getProjId());
			}

			User assignee = userService.findUserById(updatedIssue.getUserId());
			if (assignee == null) {
				throw new UserException("Assignee not found with ID: " + updatedIssue.getUserId());
			}

			Issue issueToUpdate = existingIssue.get();

			// Clear existing assignees and add the new assignee
			issueToUpdate.getAssignee().clear();
			issueToUpdate.getAssignee().add(assignee);

			// Update issue fields
			if (updatedIssue.getDescription() != null) {
				issueToUpdate.setDescription(updatedIssue.getDescription());
			}

			if (updatedIssue.getDueDate() != null) {
				issueToUpdate.setDueDate(updatedIssue.getDueDate());
			}

			if (updatedIssue.getPriority() != null) {
				issueToUpdate.setPriority(updatedIssue.getPriority());
			}

			if (updatedIssue.getStatus() != null) {
				issueToUpdate.setStatus(updatedIssue.getStatus());
			}

			if (updatedIssue.getTitle() != null) {
				issueToUpdate.setTitle(updatedIssue.getTitle());
			}
             for(User u:existingIssue.get().getAssignee()) {
            	 notifyAssignee(u.getEmail(), "Issue Updated", "The issue has been updated.");
             }
			// Save the updated issue
			return Optional.of(issueRepository.save(issueToUpdate));
		}

		throw new IssueException("Issue not found with issueid" + issueId);
	}

	@Override
	public String deleteIssue(Long issueId, Long userId) throws UserException, IssueException {
		getUserOrThrow(userId);
		Optional<Issue> issueById = getIssueById(issueId);
		if (issueById.isPresent()) {
			issueRepository.deleteById(issueId);
			return "issue with the id" + issueId + "deleted";
		}
		throw new IssueException("Issue not found with issueid" + issueId);
	}

	@Override
	public List<Issue> getIssuesByAssigneeId(Long assigneeId) throws IssueException {
		List<Issue> issues = issueRepository.findByAssigneeId(assigneeId);
		if (issues != null) {
			return issues;
		}
		throw new IssueException("Issues not found");
	}

	private User getUserOrThrow(Long userId) throws UserException {
		User user = userService.findUserById(userId);

		if (user != null) {
			return user;
		} else {
			throw new UserException("User not found with id: " + userId);
		}
	}

	@Override
	public List<Issue> searchIssues(String title, String status, String priority, Long assigneeId)
			throws IssueException {
		List<Issue> searchIssues = issueRepository.searchIssues(title, status, priority, assigneeId);
		if (searchIssues != null) {
			return searchIssues;
		}
		throw new IssueException("No Issues found");
	}

	@Override
	public List<User> getAssigneeForIssue(Long issueId) throws IssueException {
		Optional<Issue> issue = getIssueById(issueId);
		if (issue.isPresent()) {
			return issue.get().getAssignee();
		}
		throw new IssueException("Issue not found with issueid" + issueId);
	}

	@Override
	public String addUserToIssue(Long issueId, Long userId) throws UserException, IssueException {
		User user = userService.findUserById(userId);
		if (user == null) {
			throw new UserException("User not found");
		}

		Optional<Issue> issueOptional = issueRepository.findById(issueId);
		if (issueOptional.isPresent()) {
			Issue issue = issueOptional.get();
			List<User> assignees = issue.getAssignee();

			if (!assignees.contains(user)) {
				 notifyAssignee(user.getEmail(), "Issue Updated", "Hello user you have been added to resolve this issue with issue id"+issue.getId());
				assignees.add(user);
				issueRepository.save(issue);
				return "User added to the issue";
			} else {
				return "User is already assigned to the issue";
			}
		} else {
			throw new IssueException("Issue not found");
		}
	}
	 private void notifyAssignee(String email, String subject, String body) {
		 System.out.println("IssueServiceImpl.notifyAssignee()");
	        notificationServiceImpl.sendNotification(email, subject, body);
	    }

}
