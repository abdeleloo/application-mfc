package fr.mfc.desktop.ui;

import java.sql.Connection;

@FunctionalInterface
public interface ConnSupplier {
    Connection get();
}

