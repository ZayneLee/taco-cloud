package tacos.data;

import org.springframework.data.repository.Repository;

import tacos.TacoOrder;

public interface OrderRepository extends Repository<TacoOrder, Long> {
    TacoOrder save(TacoOrder order);
}
