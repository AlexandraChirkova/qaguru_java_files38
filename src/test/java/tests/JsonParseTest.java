package tests;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import pojo.Item;
import pojo.Order;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class JsonParseTest {
    @Test
    void testParseOrderJson() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Order order = mapper.readValue(new File("src/test/resources/order.json"), Order.class);

        assertEquals(12345, order.getOrderId());
        assertEquals("Ivan Petrov", order.getCustomerName());
        assertEquals(2, order.getItems().size());
        assertEquals(670000.0, order.getTotalPrice());

        Item firstItem = order.getItems().get(0);
        assertEquals("iPhone 15", firstItem.getName());
        assertEquals(450000.0, firstItem.getPrice());
        assertEquals(1, firstItem.getQty());

    }
}
