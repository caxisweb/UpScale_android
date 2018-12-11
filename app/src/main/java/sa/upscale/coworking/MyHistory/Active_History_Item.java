package sa.upscale.coworking.MyHistory;

/**
 * Created by codeclinic on 01/05/17.
 */

public class Active_History_Item {

    String Title;
    String Location;
    String DateTime,date,fromtime,totime,bookingId;
    String spce_Id, repeat_b, capacity;


    public String getSpce_Id() {
        return spce_Id;
    }

    public void setSpce_Id(String spce_Id) {
        this.spce_Id = spce_Id;
    }

    public String getRepeat_b() {
        return repeat_b;
    }

    public void setRepeat_b(String repeat_b) {
        this.repeat_b = repeat_b;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFromtime() {
        return fromtime;
    }

    public void setFromtime(String fromtime) {
        this.fromtime = fromtime;
    }

    public String getTotime() {
        return totime;
    }

    public void setTotime(String totime) {
        this.totime = totime;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public Active_History_Item(String bookId,String title, String location, String dateTime, String amount, String baseAmount, String hour, String date1, String start, String end,String spce_Id1,String repeat_b1,String capacity1) {

        bookingId=bookId;
        Title = title;
        Location = location;
        DateTime = dateTime;
        Amount = amount;
        BaseAmount = baseAmount;
        Hour = hour;
        date=date1;
        fromtime=start;
        totime=end;
        spce_Id=spce_Id1;
        repeat_b=repeat_b1;
        capacity=capacity1;


    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getDateTime() {
        return DateTime;
    }

    public void setDateTime(String dateTime) {
        DateTime = dateTime;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public String getBaseAmount() {
        return BaseAmount;
    }

    public void setBaseAmount(String baseAmount) {
        BaseAmount = baseAmount;
    }

    public String getHour() {
        return Hour;
    }

    public void setHour(String hour) {
        Hour = hour;
    }

    String Amount;
    String BaseAmount;
    String Hour;


}
