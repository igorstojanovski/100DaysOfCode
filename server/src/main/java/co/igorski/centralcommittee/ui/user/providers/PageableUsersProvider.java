package co.igorski.centralcommittee.ui.user.providers;

import co.igorski.centralcommittee.model.User;
import co.igorski.centralcommittee.repositories.UserRepository;
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

@Service("UserProvider")
public class PageableUsersProvider<F> extends PageableDataProvider<User, F> {

    @Autowired
    private UserRepository usersRepository;

    @Override
    protected Page<User> fetchFromBackEnd(Query<User, F> query, Pageable pageable) {
        return usersRepository.findByEnabledTrue(pageable);
    }

    @Override
    protected List<QuerySortOrder> getDefaultSortOrders() {
        List<QuerySortOrder> sortOrders = new ArrayList<>();
        sortOrders.add(new QuerySortOrder("id", SortDirection.ASCENDING));
        return sortOrders;
    }

    @Override
    protected int sizeInBackEnd(Query<User, F> query) {
        return (int) (long) usersRepository.countByEnabledTrue();
    }
}
