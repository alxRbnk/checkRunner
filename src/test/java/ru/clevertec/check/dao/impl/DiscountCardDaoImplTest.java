package ru.clevertec.check.dao.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.check.entity.DiscountCard;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class DiscountCardDaoImplTest {

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private ResultSet resultSet;

    @InjectMocks
    private DiscountCardDaoImpl discountCardDao = DiscountCardDaoImpl.getInstance();

    @BeforeEach
    public void setUp() throws Exception {
        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
    }

    @Test
    public void testGetAllReturnsValidMap() throws SQLException {
        Mockito.when(resultSet.next()).thenReturn(true, true, false);
        Mockito.when(resultSet.getInt("id")).thenReturn(1, 2);
        Mockito.when(resultSet.getString("number")).thenReturn("12345", "67890");
        Mockito.when(resultSet.getDouble("amount")).thenReturn(10.0, 15.0);
        Map<String, DiscountCard> cards = discountCardDao.getAll(connection);
        assertEquals(2, cards.size());
        assertTrue(cards.containsKey("12345"));
        assertTrue(cards.containsKey("67890"));
        assertEquals(10.0, cards.get("12345").getDiscountAmount(), 0.01);
        assertEquals(15.0, cards.get("67890").getDiscountAmount(), 0.01);
    }

    @Test
    public void testGetAllReturnsEmptyMap() throws SQLException {
        Mockito.when(resultSet.next()).thenReturn(false);
        Map<String, DiscountCard> cards = discountCardDao.getAll(connection);
        assertTrue(cards.isEmpty());
    }

    @Test
    public void testGetAllThrowsRuntimeExceptionOnSQLException() throws SQLException {
        Mockito.when(preparedStatement.executeQuery()).thenThrow(new SQLException());
        RuntimeException exception = org.junit.jupiter.api.Assertions.assertThrows(RuntimeException.class, () -> {
            discountCardDao.getAll(connection);
        });
        assertTrue(exception.getCause() instanceof SQLException);
    }
}
