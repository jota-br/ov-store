package main.java.ostro.veda.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderDetailDTO {

    private final int orderDetailId;
    private final OrderDTO order;
    private final ProductDTO product;
    private final int quantity;
    private final double unitPrice;
    private final int version;
}
