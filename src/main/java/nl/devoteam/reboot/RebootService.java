package nl.devoteam.reboot;

public interface RebootService {

    String triggerReboot();
    void killServices();
    void deleteImages();
    void startServices();

}
