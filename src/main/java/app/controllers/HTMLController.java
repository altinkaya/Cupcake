package app.controllers;

import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.CupcakeMapper;

public class HTMLController {
    public static void generateCupcakeDropdowns(ConnectionPool connectionPool) throws DatabaseException {
        String dropdownHTML = CupcakeMapper.flavorDropdownsHTML(connectionPool);

    }
}
