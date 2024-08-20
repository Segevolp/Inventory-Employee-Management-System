package test.mockClasses;

import DataAccessLayer.HR.ControllerClasses.RoleController;
import DataAccessLayer.DTO;

import java.sql.*;

public class RoleControllerMock extends RoleController {
    public RoleControllerMock() throws ClassNotFoundException {
    }

    @Override
    public boolean insert(Object[] attributesValues) throws Exception {
        return true;
    }

    @Override
    public boolean update(Object[] identifiersValues, String varToUpdate, Object valueToUpdate) throws Exception {
        return true;
    }

    @Override
    public boolean delete(Object[] identifiersValues) throws Exception {
        return true;
    }

    @Override
    public DTO convertReaderToObject(ResultSet resultSet) throws SQLException {
        return null;

    }
    public ResultSet getAll() throws SQLException{
        return null;
    }
}
