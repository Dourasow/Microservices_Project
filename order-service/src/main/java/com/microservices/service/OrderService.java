package com.microservices.service;

import com.microservices.dto.InventoryResponse;
import com.microservices.dto.OrderLineItemDto;
import com.microservices.dto.OrderRequest;
import com.microservices.model.Order;
import com.microservices.model.OrderItemList;
import com.microservices.repository.OrderRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.loadbalancer.reactive.LoadBalancerWebClientBuilderBeanPostProcessor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;

    public void placeOrder(OrderRequest orderRequest){
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderItemList> orderItemList = orderRequest.getOrderLineItemDto()
                .stream()
                .map(this::mapToDto)
                .toList();
        order.setOrderItemList(orderItemList);

        List<String> skuCodes = order.getOrderItemList().stream()
                .map(OrderItemList::getSkuCode)
                .toList();

        //Call Inventory service and place order if product is in stock
        InventoryResponse[] inventoryResponsesArray = webClientBuilder.build().get()
                        .uri("http://inventory-service/api/inventory",
                                uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                                .retrieve()
                                        .bodyToMono(InventoryResponse[].class)
                                                .block();
        boolean allProductsInStock = Arrays.stream(inventoryResponsesArray)
                .allMatch(InventoryResponse::isInStock);

        if(allProductsInStock) {
            orderRepository.save(order);
        } else {
            throw new IllegalArgumentException("Product is not in stock, Please try again");
        }

    }

    private OrderItemList mapToDto(OrderLineItemDto orderLineItemDto) {
        OrderItemList orderItemList = new OrderItemList();
        orderItemList.setPrice(orderLineItemDto.getPrice());
        orderItemList.setQuantity(orderLineItemDto.getQuantity());
        orderItemList.setSkuCode(orderLineItemDto.getSkuCode());
        return orderItemList;
    }
}
