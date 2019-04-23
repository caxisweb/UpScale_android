package sa.upscale.coworking.MettingRoom.adapter;

/**
 * Created by bhatt on 4/27/2017.
 */

public class Custome_Map_Item {

    String Address,name;
    String Price;
    String capacity;
    String distance;
    String space_id;

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Custome_Map_Item(String name, String address, String price, String capacity, String distance,String space_id) {

        this.name=name;

        Address = address;
        Price = price;
        this.capacity = capacity;
        this.distance = distance;
        this.space_id=space_id;
    }

    public String getAddress() {

        return Address;
    }

    public String getSpace_id() {
        return space_id;
    }

    public void setSpace_id(String space_id) {
        this.space_id = space_id;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }
}
