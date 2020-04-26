
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

/**
 * Floor Control Panel The ControlPanel class receives and handles button
 * pressing events
 */
public class ControlPanel extends JPanel implements ActionListener {

    //an array of floor buttons
    private JButton[] floorButton;

    //state of each button: 0 means not clicked and >=1 means how many
    public ArrayList<Passenger> floorIn[]; // floor in

    // state of each button: 0 means not clicked and >=1 means how many
    public int floorOut[]; // floor out

    private int numberOfFloors;

    // passengers are waiting on this floor
    public ControlPanel(int width, int height, int numberOfFloors) {

        this.numberOfFloors = numberOfFloors;

        // set the size of the control button panel
        this.setSize(width, height);

        // draw a tittle border for this panel
        LineBorder lineBorder = new LineBorder(new Color(204, 204, 204), 1, true);
        this.setBorder(BorderFactory.createTitledBorder(lineBorder, "Floors to enter from"));

        // set the layout for the panel
        this.setLayout(new GridLayout(0, 1, 0, 0));

        // update the button panel details and add the buttons
        update();

    }

    // set the given number of floors
    public void setNumberOfFloors(int numberOfFloors) {

        this.numberOfFloors = numberOfFloors;
        // clean the panel

        this.removeAll();
        // update the panel
        update();

        // repaint the panel with new button set
        repaint();
        revalidate();
    }

    // to update the button panel 
    private void update() {

        // floor button arrays
        this.floorButton = new JButton[numberOfFloors];

        // track floor in count
        this.floorIn = new ArrayList[numberOfFloors];
        
        // track floor out count
        this.floorOut = new int[numberOfFloors];
       
        
        for (int i = numberOfFloors; i > 0; i--) {

            // create a new button
            JButton button = new JButton();
            button.setBackground(new Color(204, 204, 204));
            button.setPreferredSize(new Dimension(122, 22));

            // panel to center the button with margines
            JPanel buttonCenter = new JPanel(new GridBagLayout());
            buttonCenter.add(button);

            // add the panel with centered button
            this.add(buttonCenter);
            floorButton[i - 1] = button;

            // add the action listners
            button.addActionListener(this);
            button.setFocusPainted(false);

            this.floorIn[i - 1] = new ArrayList<>();
            
            // set the button text
            updateButtonText(i - 1);
        }
    }
    

    /**
     * This method update the button text and color
     *
     * @param buttonIndex
     */
    public void updateButtonText(int buttonIndex) {

        String floorInOutStr = "", floorInStr = "", floorOutStr = "";
        
        if (floorOut[buttonIndex] > 0) {
            floorOutStr = String.format("out: %d", floorOut[buttonIndex]);
            changeButtonForeground(buttonIndex, Color.BLACK);
        }
        
        // if the there are passengers to be picked by the elevator
        if (floorIn[buttonIndex].size() > 0) {
            floorInStr = String.format("in: %d", floorIn[buttonIndex].size());
            // change the button foreground
            changeButtonForeground(buttonIndex, Color.red);
        }
        
        String comma = !floorInStr.isEmpty() && !floorOutStr.isEmpty() ? ", " : "";

        if (!floorInStr.isEmpty() || !floorOutStr.isEmpty()) {
            floorInOutStr = " (" + floorInStr + comma + floorOutStr + ")";
        } else {
            // change the button forground
            changeButtonForeground(buttonIndex, Color.BLACK);
        }
        // set the button text
        String txt = String.format("%s%2d%s", "F", (buttonIndex + 1), floorInOutStr);
        floorButton[buttonIndex].setText(txt);
    }

    // helper method to change the button text color
    private void changeButtonForeground(int index, Color color) {
        floorButton[index].setForeground(color);
    }

    public int getNumberOfFloors() {
        return numberOfFloors;
    }

    /**
     * This method returns the index(floor index) of the given button
     *
     * @param button the given button
     * @return the floor index, if not return -1
     */
    private int getButtonIndex(JButton button) {
        for (int i = 0; i < numberOfFloors; i++) {
            if (floorButton[i].equals(button)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // handle button pressing events
        JButton clickedButton = (JButton) e.getSource();

        // get the index of the clicked button
        int buttonIndex = getButtonIndex(clickedButton);
        
        WhereToGoToDialog whereToGoToDialog = new WhereToGoToDialog(this, buttonIndex);
        whereToGoToDialog.setVisible(true);

        // update the button text
        updateButtonText(buttonIndex);
    }
}
