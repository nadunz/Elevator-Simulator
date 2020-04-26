
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.LineBorder;

// The elevator class draws the elevator area and simulates elevator movement
public class Elevator extends JPanel implements ActionListener {

    // Declaration of variables
    //the Elevator Simulation frame
    private ElevatorSimulation app;

    // the elevator is moving up or down
    private boolean up;

    // Elevator width
    private int width;

    // Elevator height
    private int height;

    // The x coordinate of the elevator’s upper left corner
    private int xco;

    // The y coordinate of the elevator’s upper left corner
    private int yco;

    // the time period to pricking persons
    private int pickingTime;

    // current floor 
    private int currentFloor;

    // current floorIn and floor out
    private ArrayList<Passenger> floorIn, elevatorIn;

    //the y coordinate of the top level
    private int top;

    // the y coordinate of the bottom level
    private int bottom;

    // floor height
    private int floorHeight;

    // door dx when opening or closing
    private int doorDx;

    //the timer to drive the elevator movement
    private Timer animTimer;

    private int numOfFloors;

    private boolean pickingOrDropping = false; // if picking or dropping passengers
    private boolean opening = false; // opening door
    private boolean closing = false; // opening door

    // looping when stop the timer
    private int looping = 0;

    public Elevator(ElevatorSimulation app, int widthArea, int heightArea, int numOfFloors) {
        /* necessary initialization */
        this.app = app;
        this.numOfFloors = numOfFloors;
        this.animTimer = new Timer(20, this);

        // set the size of elevator area
        this.setSize(widthArea, heightArea);

        this.elevatorIn = new ArrayList<>();
        
        // update the elevator data
        updateElevator();

        // add a border for the elevator panel
        LineBorder lineBorder = new LineBorder(new Color(204, 204, 204), 1, true);
        setBorder(BorderFactory.createTitledBorder(lineBorder, "Elevator"));
    }

    // set the given number of floors
    public void setNumOfFloors(int numOfFloors) {
        this.numOfFloors = numOfFloors;
        // update the elevator data

        updateElevator();
        // repaint the UI
        repaint();
    }

    // update the elvator details
    private void updateElevator() {

        // get elvator area size
        int elevatorAreaWidth = getWidth();
        int elevatorAreahHeight = getHeight();

        // foor height
        this.floorHeight = elevatorAreahHeight / numOfFloors;

        // bottom & top floot y co
        this.bottom = elevatorAreahHeight - 1;
        this.top = bottom - floorHeight * (numOfFloors - 1);

        // elevator size
        this.height = floorHeight * 4 / 5;
        this.width = height;

        // elevator going up side
        this.up = true;

        // current elvator position
        this.xco = elevatorAreaWidth / 2 - width / 2;
        this.yco = bottom - height;
    }

    // Paint elevator area
    @Override
    public void paintComponent(Graphics g) {

        //clear the painting canvas
        super.paintComponent(g);

        //obtain geometric values of components for drawing the elevator area
        Graphics2D g2d = (Graphics2D) g;

        //draw horizontal lines and the elevator
        g2d.setStroke(new BasicStroke(1.6f));
        g2d.setColor(Color.BLUE);
        for (int y = bottom; y >= top; y -= floorHeight) {
            g2d.drawLine(3, y, getWidth() - 3, y);
        }

        // draw doors
        g2d.setColor(Color.LIGHT_GRAY);
        // left door
        g2d.fillRect(xco, yco, width / 2 - doorDx, height);
        g2d.setColor(Color.BLUE);
        g2d.drawRect(xco, yco, width / 2 - doorDx, height);

        // right door
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.fillRect(xco + width / 2 + 1 + doorDx, yco, width / 2 - doorDx, height);
        g2d.setColor(Color.BLUE);
        g2d.drawRect(xco + width / 2 + 1 + doorDx, yco, width / 2 - doorDx, height);

        // draw elevator
        g2d.setColor(Color.BLUE);
        g2d.drawRect(xco, yco, width, height);

    }

    /**
     * This method get the current y position of the elevator and return the
     * current floor. In this section we are checking only the down level of the
     * floor because we need to stop the elevator at the down level of any floor
     *
     * @param y - current y coordinates of the elevator
     * @return the floor number when the elevator at the down level of the
     * floor, otherwise -1
     */
    private int reachedFoor(int y) {
        // get the elevator area height
        int areaHeight = getHeight();
        for (int i = 0; i < numOfFloors; i++) {
            if ((y + height) == areaHeight - 1 - i * floorHeight) {
                return i;
            }
        }
        return -1;
    }

    // Handle the timer events
    @Override
    public void actionPerformed(ActionEvent e) {

        // doing nothing if the elevator needs to be stopped for a while
        // opening 15 x 200 seconds 
        if (opening) { // openning the door
            doorDx++;
            if (doorDx == width / 2) {
                opening = false;
                pickingOrDropping = true; // then picking or dropping passengers
            }
            repaint();
            return;
        }

        if (pickingOrDropping) { // picking passsengers
            if (looping++ == pickingTime) {
                pickingOrDropping = false;
                closing = true; // then closing the door
                looping = 0;
                
                app.getControl().updateButtonText(currentFloor);
            }
            repaint();
            return;
        }

        if (closing) { // clossing the door
            doorDx--;
            if (doorDx == 0) {
                closing = false;
            }
            repaint();
            return;
        }

        //update the state of the elevator and the buttons
        if ((currentFloor = reachedFoor(yco)) >= 0) {

            floorIn = app.getControl().floorIn[currentFloor];

            int passengersCountToDroppOff = getPassengersCountToDroppOff();
            
            if (floorIn.size() > 0 || passengersCountToDroppOff > 0) {
                opening = true; // open the door
                pickingTime = 30 * (floorIn.size() + passengersCountToDroppOff); // 30 ms for 1 person
                
                if (passengersCountToDroppOff == 0) {
                    
                    app.setState("Picking up " + floorIn.size() + " passengers on floor " + (currentFloor + 1));
                    elevatorIn.addAll(floorIn);
                    app.getControl().floorIn[currentFloor] = new ArrayList<>();
                    
                } else if (floorIn.isEmpty()) {
                    
                    app.setState("Dropping off " + passengersCountToDroppOff + " passengers on floor " + (currentFloor + 1));
                    droppOffPassengers();
                    
                } else {
                    app.setState("Dropping off " + passengersCountToDroppOff + " and picking up " + floorIn.size() + 
                            " passengers on floor " + (currentFloor + 1));
                    elevatorIn.addAll(floorIn);
                    app.getControl().floorIn[currentFloor] = new ArrayList<>();
                    droppOffPassengers();
                }
                return;
            }
        }

        //adjust Y coordinate to simulate elevetor movement
        if (up) {
            yco--;
            app.setState("Moving up..");
        } else {
            yco++;
            app.setState("Moving down..");
        }

        //change moving direction when hits the top and bottom
        if (yco < top - height) {
            up = false;
        } else if (yco > bottom - height) {
            up = true;
        }
        //repaint the panel
        repaint();

    }
    
    private int getPassengersCountToDroppOff() {
        int count = 0;
        for (Passenger p : elevatorIn) {
            if(p.getFloorTo() == currentFloor) {
                count++;
            }
        }
        return count;
    }
    
    private void droppOffPassengers() {
        ArrayList<Passenger> restPassengersInElevator = new ArrayList<>();
        for (Passenger p : elevatorIn) {
            if(p.getFloorTo() != currentFloor) {
                restPassengersInElevator.add(p);
            }
        }
        app.getControl().floorOut[currentFloor] -= getPassengersCountToDroppOff();
        elevatorIn = restPassengersInElevator;
    }

    public void start() {
        animTimer.start();
    }

    public void stop() {
        animTimer.stop();
    }

}
