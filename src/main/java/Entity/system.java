package Entity;

import java.util.ArrayList;

public class system {
    private String system;

    public system(String system) {
        this.system = system;
    }

    public system() {
    }


    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    @Override
    public String toString() {
        return "system{" +
                "system='" + system + '\'' +
                '}';
    }


}

