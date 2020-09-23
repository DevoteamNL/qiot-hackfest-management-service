package nl.devoteam;

import java.util.List;

public class HealthState {
    private String status;
    private List<HealthStateCheck> checks;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<HealthStateCheck> getChecks() {
        return checks;
    }

    public void setChecks(List<HealthStateCheck> checks) {
        this.checks = checks;
    }
}
