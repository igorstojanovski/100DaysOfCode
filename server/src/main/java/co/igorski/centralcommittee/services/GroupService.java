package co.igorski.centralcommittee.services;

import co.igorski.centralcommittee.model.Group;
import co.igorski.centralcommittee.model.Project;
import co.igorski.centralcommittee.repositories.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupService {

    @Autowired
    private GroupRepository groupRepository;

    public List<Group> getGroups(Project project) {
        return groupRepository.findByProject(project);
    }

    public Group save(Group group) {
        return groupRepository.save(group);
    }
}
