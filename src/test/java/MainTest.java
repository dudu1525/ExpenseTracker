import javafiles.managers.MainManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


public class MainTest {
    private MainManager manager;
    private Connection mockConnection;
    private PreparedStatement mockSelectStatement;
    private PreparedStatement mockInsertStatement;
    private ResultSet mockResultSet;

    @BeforeEach
    public void setup() throws SQLException {
        // Initialize mocks
        mockConnection = mock(Connection.class);
        mockSelectStatement = mock(PreparedStatement.class);
        mockInsertStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);

        // Set up the manager and inject the mocks
        manager = new MainManager();
        manager.setUser_id(1);  // Set a user ID for the test
        manager.setConnection(mockConnection);  // Inject mock connection

        // Mocking the behavior for the select statement
        when(mockConnection.prepareStatement(contains("select id from expenses where date=? and user_id=?"))).thenReturn(mockSelectStatement);
        when(mockSelectStatement.executeQuery()).thenReturn(mockResultSet);

        // Mocking the behavior for the insert statement
        when(mockConnection.prepareStatement(contains("INSERT INTO expenses"))).thenReturn(mockInsertStatement);
    }

    @Test
    public void testInit_NoExistingRecord_InsertsNewRecord() throws SQLException {
        // Simulate no existing record in the ResultSet (date + user_id combo doesn't exist)
        when(mockResultSet.next()).thenReturn(false);  // ResultSet is empty

        // Call the init() method
        manager.init();

        // Verify the select query execution
        verify(mockSelectStatement, times(1)).setObject(1, manager.getDate()); // for the date
        verify(mockSelectStatement, times(1)).setInt(2, manager.getUser_id()); // for the user_id
        verify(mockSelectStatement, times(1)).executeQuery(); // Select query executed

        // Verify the insert query execution (since no existing record was found)
        verify(mockInsertStatement, times(1)).setInt(1, 2); // maxId+1 = 2
        verify(mockInsertStatement, times(1)).setObject(2, manager.getDate()); // Insert the new date
        verify(mockInsertStatement, times(1)).setInt(3, manager.getUser_id()); // Insert the user_id
        verify(mockInsertStatement, times(1)).executeUpdate(); // Insert query executed
    }

    @Test
    public void testInit_ExistingRecord_NoInsert() throws SQLException {
        // Simulate an existing record in the ResultSet (date + user_id combo already exists)
        when(mockResultSet.next()).thenReturn(true);  // ResultSet has a record

        // Call the init() method
        manager.init();

        // Verify the select query execution
        verify(mockSelectStatement, times(1)).setObject(1, manager.getDate()); // for the date
        verify(mockSelectStatement, times(1)).setInt(2, manager.getUser_id()); // for the user_id
        verify(mockSelectStatement, times(1)).executeQuery(); // Select query executed

        // Verify that insert query is never executed (since record already exists)
        verify(mockInsertStatement, never()).executeUpdate(); // Insert query not executed
    }
}
