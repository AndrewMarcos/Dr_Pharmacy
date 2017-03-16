package pharmacy.morcos.andrew.drpharmacy.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andre on 05-Mar-17.
 */

public class data_delevery {

    String sender,time,newText,orderNo,deleveryKey,personId;
    Boolean seen;



    public data_delevery(String sender, String time, String newText, String orderNo, String deleveryKey, String personId, Boolean seen) {
        this.sender = sender;
        this.time = time;
        this.newText = newText;
        this.orderNo = orderNo;
        this.deleveryKey = deleveryKey;
        this.personId = personId;
        this.seen = seen;
    }

    public String getSender() {
        return sender;
    }

    public String getTime() {
        return time;
    }

    public String getNewText() {
        return newText;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public String getDeleveryKey() {
        return deleveryKey;
    }

    public String getPersonId() {
        return personId;
    }

    public Boolean getSeen() {
        return seen;
    }
}
