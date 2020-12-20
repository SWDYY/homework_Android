<?php
function returnClassBytype($type){
    switch ($type){
        case "login":
            return new login();
        case "repository":
            return new repository();
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
class repository implements header {
    public $id="";
    public $name="";
    public $outprice="";

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
                $this->outprice = $value;
                break;
            default:break;
        }
    }
}
interface header{
    function setData($idNum,$value);
}