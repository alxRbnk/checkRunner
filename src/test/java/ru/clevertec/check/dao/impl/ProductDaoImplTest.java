package ru.clevertec.check.dao.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.check.entity.Product;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ProductDaoImplTest {

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private ResultSet resultSet;

    @InjectMocks
    private ProductDaoImpl productDao = ProductDaoImpl.getInstance();

    @BeforeEach
    public void setUp() throws Exception {
        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
    }

    @Test
    public void testGetAllReturnsValidMap() throws SQLException {
        Mockito.when(resultSet.next()).thenReturn(true, true, false); // Эмулируем две записи
        Mockito.when(resultSet.getInt("id")).thenReturn(1, 2);
        Mockito.when(resultSet.getString("description")).thenReturn("Product 1", "Product 2");
        Mockito.when(resultSet.getDouble("price")).thenReturn(100.0, 200.0);
        Mockito.when(resultSet.getInt("quantity_in_stock")).thenReturn(10, 20);
        Mockito.when(resultSet.getBoolean("wholesale_product")).thenReturn(true, false);
        Map<Integer, Product> products = productDao.getAll(connection);
        assertEquals(2, products.size());
        Product product1 = products.get(1);
        assertNotNull(product1);
        assertEquals("Product 1", product1.getDescription());
        assertEquals(BigDecimal.valueOf(100.0), product1.getPrice());
        assertEquals(10, product1.getQuantityInStock());
        assertTrue(product1.isWholesale());
        Product product2 = products.get(2);
        assertNotNull(product2);
        assertEquals("Product 2", product2.getDescription());
        assertEquals(BigDecimal.valueOf(200.0), product2.getPrice());
        assertEquals(20, product2.getQuantityInStock());
        assertFalse(product2.isWholesale());
    }

    @Test
    public void testGetAllReturnsEmptyMap() throws SQLException {
        Mockito.when(resultSet.next()).thenReturn(false);
        Map<Integer, Product> products = productDao.getAll(connection);
        assertTrue(products.isEmpty());
    }

    @Test
    public void testGetAllThrowsRuntimeExceptionOnSQLException() throws SQLException {
        Mockito.when(preparedStatement.executeQuery()).thenThrow(new SQLException());
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            productDao.getAll(connection);
        });
        assertTrue(exception.getCause() instanceof SQLException);
    }
}
