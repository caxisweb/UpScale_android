package sa.upscale.coworking.model;

/**
 * Created by DELL on 21-02-2018.
 */

public class Package {

    String pack_id,pack_name,pack_rat,pack_price,pack_type,pack_detail,pack_space,str_bank_name, str_iban, str_account_no,str_bank_name_ar, str_iban_ar, str_account_no_ar,str_subscribe_status,str_company_name;

    public Package(String pack_id, String pack_name, String pack_rat, String pack_price, String pack_type, String pack_detail, String pack_space, String str_bank_name, String str_iban, String str_account_no, String str_bank_name_ar, String str_iban_ar, String str_account_no_ar,String str_subscribe_status,String str_company_name) {
        this.pack_id = pack_id;
        this.pack_name = pack_name;
        this.pack_rat = pack_rat;
        this.pack_price = pack_price;
        this.pack_type = pack_type;
        this.pack_detail = pack_detail;
        this.pack_space = pack_space;
        this.str_bank_name = str_bank_name;
        this.str_iban = str_iban;
        this.str_account_no = str_account_no;
        this.str_bank_name_ar = str_bank_name_ar;
        this.str_iban_ar = str_iban_ar;
        this.str_account_no_ar = str_account_no_ar;
        this.str_subscribe_status=str_subscribe_status;
        this.str_company_name=str_company_name;
    }

    public String getPack_id() {
        return pack_id;
    }

    public void setPack_id(String pack_id) {
        this.pack_id = pack_id;
    }

    public String getPack_name() {
        return pack_name;
    }

    public String getStr_company_name() {
        return str_company_name;
    }

    public void setStr_company_name(String str_company_name) {
        this.str_company_name = str_company_name;
    }

    public void setPack_name(String pack_name) {
        this.pack_name = pack_name;
    }

    public String getPack_rat() {
        return pack_rat;
    }

    public void setPack_rat(String pack_rat) {
        this.pack_rat = pack_rat;
    }

    public String getPack_price() {
        return pack_price;
    }

    public void setPack_price(String pack_price) {
        this.pack_price = pack_price;
    }

    public String getPack_type() {
        return pack_type;
    }

    public void setPack_type(String pack_type) {
        this.pack_type = pack_type;
    }

    public String getPack_detail() {
        return pack_detail;
    }

    public void setPack_detail(String pack_detail) {
        this.pack_detail = pack_detail;
    }

    public String getPack_space() {
        return pack_space;
    }

    public void setPack_space(String pack_space) {
        this.pack_space = pack_space;
    }

    public String getStr_bank_name() {
        return str_bank_name;
    }

    public void setStr_bank_name(String str_bank_name) {
        this.str_bank_name = str_bank_name;
    }

    public String getStr_iban() {
        return str_iban;
    }

    public void setStr_iban(String str_iban) {
        this.str_iban = str_iban;
    }

    public String getStr_account_no() {
        return str_account_no;
    }

    public void setStr_account_no(String str_account_no) {
        this.str_account_no = str_account_no;
    }

    public String getStr_bank_name_ar() {
        return str_bank_name_ar;
    }

    public void setStr_bank_name_ar(String str_bank_name_ar) {
        this.str_bank_name_ar = str_bank_name_ar;
    }

    public String getStr_iban_ar() {
        return str_iban_ar;
    }

    public void setStr_iban_ar(String str_iban_ar) {
        this.str_iban_ar = str_iban_ar;
    }

    public String getStr_account_no_ar() {
        return str_account_no_ar;
    }

    public void setStr_account_no_ar(String str_account_no_ar) {
        this.str_account_no_ar = str_account_no_ar;
    }

    public String getStr_subscribe_status() {
        return str_subscribe_status;
    }

    public void setStr_subscribe_status(String str_subscribe_status) {
        this.str_subscribe_status = str_subscribe_status;
    }
}
