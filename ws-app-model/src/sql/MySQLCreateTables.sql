-- ----------------------------------------------------------------------------
-- Model
-------------------------------------------------------------------------------
DROP TABLE CompraEntrada;
DROP TABLE Partido;


-- --------------------------------- Partido ------------------------------------

CREATE TABLE Partido (idPartido BIGINT NOT NULL AUTO_INCREMENT,
    NomEquipoVis VARCHAR(255) COLLATE latin1_bin NOT NULL,
    Precio FLOAT NOT NULL,
    NumMaxEntradasDisp INT NOT NULL,
    NumEntradasVendidas INT NOT NULL,
    FechaCelebracion DATETIME NOT NULL,
    FechaCreacion DATETIME NOT NULL,
    CONSTRAINT PartidoCP PRIMARY KEY (idPartido),
    CONSTRAINT Preciovalido CHECK (Precio >= 0),
    CONSTRAINT NumMaxEntradasDispvalido CHECK (NumMaxEntradasDisp >= 0) ) ENGINE = InnoDB;


-- --------------------------------- CompraEntrada ------------------------------------

CREATE TABLE CompraEntrada (idCompra BIGINT NOT NULL AUTO_INCREMENT,
    idPartido BIGINT NOT NULL,
    Email VARCHAR(40) COLLATE latin1_bin NOT NULL,
    NumTarjetaBancaria VARCHAR (16) COLLATE latin1_bin NOT NULL,
    Unidades INT NOT NULL,
    FechaCompra DATETIME NOT NULL,
    recogida BOOLEAN NOT NULL,
    CONSTRAINT CompraEntradaCP PRIMARY KEY(idCompra),
    CONSTRAINT CompraEntradaPartidoCF FOREIGN KEY(idPartido)
        REFERENCES Partido(idPartido) ON DELETE CASCADE )  ENGINE = InnoDB;