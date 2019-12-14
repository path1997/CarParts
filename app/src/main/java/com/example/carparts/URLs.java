package com.example.carparts;

public class URLs {

    public static final String URL_PPHOTO= "http://13.80.137.25/photo/";
    private static final String ROOT_URL = "http://13.80.137.25/android/Api.php?apicall=";

    //-------------------------------------------ORDER AND CART---------------------------------------------------
    public static final String URL_GETCART ="http://13.80.137.25/android/cart.php?apicall=getcart";
    public static final String URL_MINUSITEM ="http://13.80.137.25/android/cart.php?apicall=minusitem";
    public static final String URL_PLUSITEM ="http://13.80.137.25/android/cart.php?apicall=plusitem";
    public static final String URL_REMOVEITEM ="http://13.80.137.25/android/cart.php?apicall=removeitem";
    public static final String URL_CONFIRMORDER ="http://13.80.137.25/android/order.php?apicall=confirmorder";
    public static final String URL_GETMYORDER ="http://13.80.137.25/android/order.php?apicall=getmyorder";
    public static final String URL_GETMYORDERDETAIL ="http://13.80.137.25/android/order.php?apicall=getmyorderdetail";
    public static final String URL_ADDRESERVATION ="http://13.80.137.25/android/order.php?apicall=addreservation";
    public static final String URL_REMOVERESERVATION ="http://13.80.137.25/android/order.php?apicall=removereservation";

    //-------------------------------------------------USER-------------------------------------------------------
    public static final String URL_REGISTER = ROOT_URL + "signup";
    public static final String URL_LOGIN = ROOT_URL + "login";
    public static final String URL_CHANGEDATA ="http://13.80.137.25/android/Api.php?apicall=changedata";
    public static final String URL_CHANGEPASSWORD ="http://13.80.137.25/android/Api.php?apicall=changepassword";

    //------------------------------------------------PRODUCT-----------------------------------------------------
    public static final String URL_CATEGORY= "http://13.80.137.25/android/products.php?apicall=category";
    public static final String URL_PRODUCTLIST= "http://13.80.137.25/android/products.php?apicall=productlist";
    public static final String URL_PRODUCTLISTFORHOME= "http://13.80.137.25/android/products.php?apicall=productlistforhome";
    public static final String URL_PRODUCTDETAIL= "http://13.80.137.25/android/products.php?apicall=productdetail";
    public static final String URL_ADDPRODUCT ="http://13.80.137.25/android/cart.php?apicall=addproduct";

    //------------------------------------------------ANNOUNCEMENT------------------------------------------------
    public static final String URL_ALLANNOUNCEMENT ="http://13.80.137.25/android/announcement.php?apicall=allannouncement";
    public static final String URL_MYANNOUNCEMENT ="http://13.80.137.25/android/announcement.php?apicall=myannouncement";
    public static final String URL_ANNOUNCEMENTDETAIL ="http://13.80.137.25/android/announcement.php?apicall=announcementdetail";
    public static final String URL_ADDANNOUNCEMENT= "http://13.80.137.25/android/announcement.php?apicall=addannouncement";
    public static final String URL_DELETEANNOUNCEMENT= "http://13.80.137.25/android/announcement.php?apicall=deleteannouncement";
    public static final String URL_GETMYANNOUNCEMENT= "http://13.80.137.25/android/announcement.php?apicall=getmyannouncement";
    public static final String URL_EDITMYANNOUNCEMENT= "http://13.80.137.25/android/announcement.php?apicall=editmyannouncement";
}