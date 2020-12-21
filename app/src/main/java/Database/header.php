<?php
function returnClassBytype($type){
    switch ($type){
        case "login":
            return new login();
        case "repository":
            return new repository();
        case "customer":
            return new customer();
        case "order":
            return new order();
    }
}
class login implements header
{
    public $count=6;
    public $id="";
    public $user_name="";
    public $user_password="";
    public $phonenum="";
    public $authority="";
    public $belongto="";
    function getName($idNum)
    {
        switch ($idNum){
            case 1:
               return"id";
            case 2:
                return"user_name";
            case 3:
                return"user_password";
            case 4:
                return"phonenum";
            case 5:
                return"authority";
            case 6:
                return "belongto";
            default:break;
        }
    }
    public function setData($idNum,$value){
        switch ($idNum){
            case 1:
                $this->id=$value;
                break;
            case 2:
                $this->user_name=$value;
                break;
            case 3:
                $this->user_password=$value;
                break;
            case 4:
                $this->phonenum=$value;
                break;
            case 5:
                $this->authority=$value;
                break;
            case 6:
                $this->belongto=$value;
                break;
            default:break;
        }
    }
}
class customer implements header
{
    public $count=4;
    public $id="";
    public $name="";
    public $phonenum="";
    public $classification="";
    function getName($idNum)
    {
        switch ($idNum){
            case 1:
                return"id";
            case 2:
                return"name";
            case 3:
                return"phonenum";
            case 4:
                return"classification";
            default:break;
        }
    }
    public function setData($idNum,$value){
        switch ($idNum){
            case 1:
                $this->id=$value;
                break;
            case 2:
                $this->name=$value;
                break;
            case 3:
                $this->phonenum=$value;
                break;
            case 4:
                $this->classification=$value;
                break;
            default:break;
        }
    }
}
class order implements header
{
    public $count=4;
    public $id="";
    public $name="";
    public $price_all="";
    public $state="";
    function getName($idNum)
    {
        switch ($idNum){
            case 1:
                return"id";
            case 2:
                return"name";
            case 3:
                return"price_all";
            case 4:
                return"state";
            default:break;
        }
    }
    public function setData($idNum,$value){
        switch ($idNum){
            case 1:
                $this->id=$value;
                break;
            case 2:
                $this->name=$value;
                break;
            case 3:
                $this->price_all=$value;
                break;
            case 4:
                $this->state=$value;
                break;
            default:break;
        }
    }
}
class repository implements header {
    public $count=6;
    public $id="";
    public $name="";
    public $num="";
    public $inprice="";
    public $outprice="";
    public $outprice_wholesale="";

    function setData($idNum, $value)
    {
        switch ($idNum) {
            case 1:
                $this->id = $value;
                break;
            case 2:
                $this->name = $value;
                break;
            case 3:
                $this->num = $value;
                break;
            case 4:
                $this->inprice = $value;
                break;
            case 5:
                $this->outprice = $value;
                break;
            case 6:
                $this->outprice_wholesale = $value;
                break;
            default:break;
        }
    }

    function getName($idNum)
    {
        switch ($idNum) {
            case 1:
                return "id";
            case 2:
                return "name";
            case 3:
                return "num";
            case 4:
                return "inprice";
            case 5:
                return "outprice";
            case 6:
                return "outprice_wholesale";
            default:break;
        }
    }
}
interface header{
    function setData($idNum,$value);
    function getName($idNum);
}