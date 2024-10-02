module com.hamming.hammingencoding {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.hamming.hammingencoding to javafx.fxml;
    exports com.hamming.hammingencoding;
    exports com.hamming.hammingencoding.services;
    opens com.hamming.hammingencoding.services to javafx.fxml;
    exports com.hamming.hammingencoding.utils;
    opens com.hamming.hammingencoding.utils to javafx.fxml;
}