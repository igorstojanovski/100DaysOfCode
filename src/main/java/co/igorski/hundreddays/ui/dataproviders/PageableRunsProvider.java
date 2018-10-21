package co.igorski.hundreddays.ui.dataproviders;

import co.igorski.hundreddays.model.Run;
import co.igorski.hundreddays.repositories.RunRepository;
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

@Service
public class PageableRunsProvider<T, F> extends PageableDataProvider<T, F> {

    @Autowired
    RunRepository runRepository;

    @Override
    protected Page<Run> fetchFromBackEnd(Query query, Pageable pageable) {
        return runRepository.findByEndTimeNotNull(pageable);
    }

    @Override
    protected List<QuerySortOrder> getDefaultSortOrders() {
        List<QuerySortOrder> sortOrders = new ArrayList<>();
        sortOrders.add(new QuerySortOrder("id", SortDirection.ASCENDING));
        return sortOrders;
    }

    @Override
    protected int sizeInBackEnd(Query<T, F> query) {
        return (int) (long) runRepository.countByStartTimeNotNull();
    }
}
