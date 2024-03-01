package org.vaadin.example.initializers;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.vaadin.example.entity.ConnectionType;
import org.vaadin.example.repository.ConnectionTypeRepository;

@Component
public class ConnectionsInitializer implements ApplicationRunner {
    private final ConnectionTypeRepository connectionTypeRepository;

    public ConnectionsInitializer(ConnectionTypeRepository connectionTypeRepository) {
        this.connectionTypeRepository = connectionTypeRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (connectionTypeRepository.findByName("Remote") == null) {
            ConnectionType connectionType = new ConnectionType();
            connectionType.setName("Remote");
            connectionTypeRepository.save(connectionType);
        }
        if (connectionTypeRepository.findByName("Wi-Fi") == null) {
            ConnectionType connectionType = new ConnectionType();
            connectionType.setName("Wi-Fi");
            connectionTypeRepository.save(connectionType);
        }
        if (connectionTypeRepository.findByName("Ethernet") == null) {
            ConnectionType connectionType = new ConnectionType();
            connectionType.setName("Ethernet");
            connectionTypeRepository.save(connectionType);
        }
    }
}
