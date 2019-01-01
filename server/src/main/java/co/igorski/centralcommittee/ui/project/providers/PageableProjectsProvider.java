package co.igorski.centralcommittee.ui.project.providers;

import co.igorski.centralcommittee.model.Project;
import co.igorski.centralcommittee.repositories.ProjectRepository;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.provider.QuerySortOrder;
import com.vaadin.flow.data.provider.SortDirection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.vaadin.artur.spring.dataprovider.PageableDataProvider;

import java.util.ArrayList;
import java.util.List;

@Service("ProjectProvider")
public class PageableProjectsProvider<F> extends PageableDataProvider<Project, F> {

    @Autowired
    private ProjectRepository projectRepository;

    @Override
    protected Page<Project> fetchFromBackEnd(Query<Project, F> query, Pageable pageable) {
        return projectRepository.findAll(pageable);
    }

    @Override
    protected List<QuerySortOrder> getDefaultSortOrders() {
        List<QuerySortOrder> sortOrders = new ArrayList<>();
        sortOrders.add(new QuerySortOrder("id", SortDirection.ASCENDING));
        return sortOrders;
    }

    @Override
    protected int sizeInBackEnd(Query<Project, F> query) {
        return (int) (long) projectRepository.count();
    }
}
