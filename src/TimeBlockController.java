import javax.swing.*;

public class TimeBlockController implements Controller{

    TimeBlockView view;

    TimeBlockController(){
        view = new TimeBlockView();
    }
    @Override
    public JPanel getView() {
        return view;
    }
}
