package es.udc.ws.app.model.CompraEntrada;

import es.udc.ws.util.configuration.ConfigurationParametersManager;

public class SqlCompraEntradaDaoFactory {
    private final static String CLASS_NAME_PARAMETER = "SqlCompraEntradaDaoFactory.className";
    private static SqlCompraEntradaDao dao = null;

    private SqlCompraEntradaDaoFactory() {
    }

    @SuppressWarnings("rawtypes")
    private static SqlCompraEntradaDao getInstance() {
        try {
            String daoClassName = ConfigurationParametersManager
                    .getParameter(CLASS_NAME_PARAMETER);
            Class daoClass = Class.forName(daoClassName);
            return (SqlCompraEntradaDao) daoClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public synchronized static SqlCompraEntradaDao getDao() {

        if (dao == null) {
            dao = getInstance();
        }
        return dao;

    }
}
