package ostro.veda.db;

import jakarta.persistence.EntityManager;
import jakarta.persistence.OptimisticLockException;
import ostro.veda.common.dto.OrderDetailDTO;
import ostro.veda.db.jpa.Order;
import ostro.veda.db.jpa.OrderDetail;
import ostro.veda.db.jpa.Product;

import java.util.ArrayList;
import java.util.List;

public class OrderDetailRepository extends Repository {

    public OrderDetailRepository(EntityManager em) {
        super(em);
    }

    public List<OrderDetailDTO> addOrderDetail(Order order, List<OrderDetail> orderDetailList)
            throws OptimisticLockException {

        boolean isUpdated = updateProductInventory(orderDetailList);
        if (!isUpdated) return null;

        for (OrderDetail orderDetail : orderDetailList) {
            this.em.persist(orderDetail.getProduct());
            this.em.persist(orderDetail);
        }

        return getOrderDetailDTOList(orderDetailList);
    }

    private boolean updateProductInventory(List<OrderDetail> orderDetailList) {
        for (OrderDetail orderDetail : orderDetailList) {

            Product product = orderDetail.getProduct();
            int newStock = product.getStock();
            int quantity = orderDetail.getQuantity();

            if (!hasStock(newStock, quantity)) return false;

            newStock = newStock + quantity;

            product.updateStock(newStock);
        }
        return true;
    }

    private boolean hasStock(int stock, int quantity) {
        return stock >= quantity;
    }

    /**
     * @param orderDetailList OrderDetail entity which was merged it transformed to DTO
     * @return returns the persisted OrderDetailDTO entities
     */
    private static List<OrderDetailDTO> getOrderDetailDTOList(List<OrderDetail> orderDetailList) {
        List<OrderDetailDTO> orderDetailDTOList = new ArrayList<>();
        for (OrderDetail orderDetail : orderDetailList) {
            orderDetailDTOList.add(orderDetail.transformToDto());
        }
        return orderDetailDTOList;
    }
}
