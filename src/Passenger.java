
// This class holds the passenger data
public class Passenger {
    
    private int floorFrom;
    private int floorTo;

    public Passenger(int floorFrom, int floorTo) {
        this.floorFrom = floorFrom;
        this.floorTo = floorTo;
    }

    public int getFloorFrom() {
        return floorFrom;
    }

    public int getFloorTo() {
        return floorTo;
    }

    public void setFloorFrom(int floorFrom) {
        this.floorFrom = floorFrom;
    }

    public void setFloorTo(int floorTo) {
        this.floorTo = floorTo;
    }
            
}
