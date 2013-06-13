package de.atp.parser;

import java.io.IOException;

public class InvalidFormatException extends IOException {
    private static final long serialVersionUID = -1335735835127557670L;

    public InvalidFormatException() {
        super();
    }

    public InvalidFormatException(String detailMessage) {
        super(detailMessage);
    }
}
