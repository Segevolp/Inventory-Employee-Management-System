package test.mockClasses;

import DataAccessLayer.HR.ControllerClasses.WorkScheduleController;
import DataAccessLayer.DTO;
import DataAccessLayer.HR.DTOClasses.WorkScheduleDTO;

import java.sql.*;
import java.util.List;


public class WorkScheduleControllerMock extends WorkScheduleController {

    public WorkScheduleControllerMock() throws ClassNotFoundException {
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
    public List<WorkScheduleDTO> getAll() throws SQLException{
        return null;
    }


}
