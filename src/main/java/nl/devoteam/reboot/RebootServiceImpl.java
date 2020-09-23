package nl.devoteam.reboot;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RebootServiceImpl implements RebootService {

    @Override
    public String triggerReboot() {

        killServices();
        deleteImages();
        startServices();


        return "";
    }

    @Override
    public void killServices() {

        // podman rmi --force air-quality-edge
        // podman rmi --force air-quality-sensor
    }

    @Override
    public void deleteImages() {

    }

    @Override
    public void startServices() {

    }
}
