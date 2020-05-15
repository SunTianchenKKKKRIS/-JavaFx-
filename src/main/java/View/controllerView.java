package View;

import java.util.ArrayList;

public interface controllerView {

    public void getHomepage();

    public void getLevel1Select(ArrayList<String> systemName_list);

    public void  getLevel1End();

    public void getLevel2(int questionNum);

    public void getLevel3(int questionNum);
}
