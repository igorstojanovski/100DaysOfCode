package co.igorski.centralcommittee.ui.tests.providers;

import co.igorski.centralcommittee.model.CcTest;
import co.igorski.centralcommittee.model.Project;
import co.igorski.centralcommittee.repositories.TestRepository;
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

@Service("TestsProvider")
public class PageableTestsProvider<F> extends PageableDataProvider<CcTest, F> {

    @Autowired
    private TestRepository testRepository;
    private Project project = null;

    @Override
    protected Page<CcTest> fetchFromBackEnd(Query<CcTest, F> query, Pageable pageable) {
        return testRepository.findByProject(project, pageable);
    }

    @Override
    protected List<QuerySortOrder> getDefaultSortOrders() {
        List<QuerySortOrder> sortOrders = new ArrayList<>();
        sortOrders.add(new QuerySortOrder("id", SortDirection.ASCENDING));
        return sortOrders;
    }

    @Override
    protected int sizeInBackEnd(Query<CcTest, F> query) {
        return (int) (long) testRepository.countByProject(project);
    }

    public void setProject(Project selectedProject) {
        this.project = selectedProject;
    }
}
