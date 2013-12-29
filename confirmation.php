
<?php


require 'facebook.php';
$facebook = new Facebook(array(
  'appId'  => '453545681427596',
  'secret' => '9885617e068739cb01b952b2ab571c01',
));


$user = $facebook->getUser();

$pixelz = $facebook->api('/pixelzcompany');

//Connect to Database
$number = $_GET['number'];
$pin = $_GET['pin'];
$username = "adminpixelz";
$password = "pixelz313";
$hostname = "mysql.pixelzexplorer.org"; 

$token = $facebook->getAccessToken();
 //echo $number."<br>". $token;

//connection to the database
$dbhandle = mysql_connect($hostname, $username, $password) 
  or die("Unable to connect to MySQL");
  
$query = mysql_query("SELECT * FROM `wikilanka`.`database` WHERE mobile='{$number}' AND PIN = '{$pin}' ");
//echo $pin." ".$number;
if($query){

mysql_query("UPDATE `wikilanka`.`database` SET `pin`='{$pin}' WHERE `mobile`='1'",$dbhandle);

echo "success";
}
else{
echo "error";
}


?>
<!DOCTYPE html>
<html lang="en">
    <head>
 

   
    </head>
    
</html>