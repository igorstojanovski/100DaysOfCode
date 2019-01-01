package co.igorski.centralcommittee.services;

import co.igorski.centralcommittee.model.Project;
import co.igorski.centralcommittee.repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    @Autowired
    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public Project getProject(String name) {
        return projectRepository.findByName(name);
    }

    public Project createProject(Project project) {
        return projectRepository.save(project);
    }
}
