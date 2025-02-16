package ostro.veda.db;

import ostro.veda.common.ProcessDataType;
import ostro.veda.common.dto.OrderDetailDTO;
import ostro.veda.db.helpers.EntityManagerHelper;
import ostro.veda.db.jpa.OrderDetail;
import ostro.veda.db.jpa.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OrderDetailRepository extends Repository {

    public OrderDetailRepository(EntityManagerHelper entityManagerHelper) {
        super(entityManagerHelper);
    }

    public List<OrderDetailDTO> addOrderDetail(int orderId, Map<Product, Integer> productAndQuantity) {
        List<OrderDetail> orderDetailList = new ArrayList<>();

    }
}
