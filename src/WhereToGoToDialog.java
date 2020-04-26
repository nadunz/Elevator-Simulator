
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

// Floors to go to dialog
public class WhereToGoToDialog extends JFrame implements ActionListener {

    //an array of floor buttons
    private JButton[] floorButton;

    private ControlPanel control;

    private int floorFrom;

    private int numOfFloors;

    public WhereToGoToDialog(ControlPanel control, int from) throws HeadlessException {
        super("Floors to go to");
        this.control = control;
        this.numOfFloors = control.getNumberOfFloors();
        this.floorFrom = from;
        this.setLocationByPlatform(true);
        
        update();
        pack();
    }
    
    // to update the button panel 
    private void update() {

        // floor button arrays
        this.floorButton = new JButton[numOfFloors];

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        buttonPanel.setLayout(new GridLayout(3, numOfFloors / 3, 15, 15));
        
        for (int i = 0; i < numOfFloors; i++) {

            // create a new button
            JButton button = new JButton();
            button.setFont(new Font("Tahoma", 1, 11));
            
            if(i == floorFrom) {
                button.setEnabled(false);
            }
            
            button.setBackground(new Color(204, 204, 204));

            // add the panel with centered button
            buttonPanel.add(button);
            floorButton[i] = button;

            // add the action listners
            button.addActionListener(this);
            button.setFocusPainted(false);
            // set the button text
            updateButtonText(i);
        }
        
        this.add(buttonPanel);
    }

    /**
     * This method returns the index(floor index) of the given button
     *
     * @param button the given button
     * @return the floor index, if not return -1
     */
    private int getButtonIndex(JButton button) {
        for (int i = 0; i < numOfFloors; i++) {
            if (floorButton[i].equals(button)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * This method update the button text and color
     *
     * @param buttonIndex
     */
    public void updateButtonText(int buttonIndex) {

        String floorOutStr = "";

        if (control.floorOut[buttonIndex] > 0) {
            floorOutStr = String.format(" (%d)", control.floorOut[buttonIndex]);
            // change the button foreground
            changeButtonForeground(buttonIndex, new Color(100, 200, 100));
        } else {
            // change the button forground
            changeButtonForeground(buttonIndex, Color.BLACK);
        }
        // set the button text
        String txt = String.format("%5s%2d%-5s", "F", (buttonIndex + 1), floorOutStr);
        floorButton[buttonIndex].setText(txt);
    }

    // helper method to change the button text color
    private void changeButtonForeground(int index, Color color) {
        floorButton[index].setForeground(color);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // handle button pressing events
        JButton clickedButton = (JButton) e.getSource();

        // get the index of the clicked button
        int buttonIndex = getButtonIndex(clickedButton);

        Passenger passenger = new Passenger(floorFrom, buttonIndex);
        
        // increase the floor in count 
        control.floorOut[buttonIndex]++;
        
        // increase the floor in count 
        control.floorIn[floorFrom].add(passenger);

        // update the button text
        updateButtonText(buttonIndex);
        control.updateButtonText(floorFrom);
        
        control.updateButtonText(buttonIndex);
        
        this.dispose();
    }

}
