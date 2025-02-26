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

    enum OrderOperation {
        INCREASE,
        DECREASE;
    }

    public List<OrderDetailDTO> addOrderDetail(Order order, List<OrderDetail> orderDetailList, OrderOperation orderOperation)
            throws OptimisticLockException {

        List<OrderDetail> newOrderDetailList = updateProductInventory(order, orderDetailList, orderOperation);
        if (newOrderDetailList == null) return null;

        for (OrderDetail orderDetail : newOrderDetailList.isEmpty() ? orderDetailList : newOrderDetailList) {
            this.em.persist(orderDetail.getProduct());
            this.em.persist(orderDetail);
        }

        return getOrderDetailDTOList(orderDetailList);
    }

    private List<OrderDetail> updateProductInventory(Order order, List<OrderDetail> orderDetailList, OrderOperation orderOperation) {

        List<OrderDetail> newOrderDetailList = new ArrayList<>();
        for (OrderDetail orderDetail : orderDetailList) {

            Product product = orderDetail.getProduct();
            int newStock = product.getStock();
            int quantity = orderDetail.getQuantity();


            if (orderOperation.equals(OrderOperation.INCREASE)) {
                newStock = newStock + quantity;
                newOrderDetailList.add(new OrderDetail(order, product, -quantity, product.getPrice()));
            } else {
                if (!hasStock(newStock, quantity)) return null;
                newStock = newStock - quantity;
            }

            product.updateStock(newStock);
        }
        return newOrderDetailList;
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
