module org.example.miniproyecto4navalbattleleprmsgismgpljpq {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.miniproyecto4navalbattleleprmsgismgpljpq to javafx.fxml;
    opens org.example.miniproyecto4navalbattleleprmsgismgpljpq.controller;

    exports org.example.miniproyecto4navalbattleleprmsgismgpljpq;
    exports org.example.miniproyecto4navalbattleleprmsgismgpljpq.controller;
    exports org.example.miniproyecto4navalbattleleprmsgismgpljpq.model;
    exports org.example.miniproyecto4navalbattleleprmsgismgpljpq.event;
    exports org.example.miniproyecto4navalbattleleprmsgismgpljpq.view;
    exports org.example.miniproyecto4navalbattleleprmsgismgpljpq.service;
    exports org.example.miniproyecto4navalbattleleprmsgismgpljpq.config;
}