package co.igorski.centralcommittee.ui.dataproviders;

import co.igorski.centralcommittee.repositories.RunRepository;
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

@Service("RunsProvider")
public class PageableRunsProvider<T, F> extends PageableDataProvider<T, F> {

    @Autowired
    private RunRepository runRepository;

    @Override
    protected Page<T> fetchFromBackEnd(Query query, Pageable pageable) {
        return (Page<T>) runRepository.findByEndTimeNotNull(pageable);
    }

    @Override
    protected List<QuerySortOrder> getDefaultSortOrders() {
        List<QuerySortOrder> sortOrders = new ArrayList<>();
        sortOrders.add(new QuerySortOrder("id", SortDirection.ASCENDING));
        return sortOrders;
    }

    @Override
    protected int sizeInBackEnd(Query<T, F> query) {
        return (int) (long) runRepository.countByEndTimeNotNull();
    }
}
